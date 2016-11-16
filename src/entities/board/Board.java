package entities.board;

import entities.overlay.Region;
import game.BadPlacementException;
import game.OpenTileLocation;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {

    private Tile[][] board = new Tile[80][80];
    private Stack<Tile> tileStack;

    private List<Point> openTileLocations;
    private Map<UUID, Region> regions;
    private List<Tiger> tigers;

    public Board(Stack<Tile> stack) {
        tileStack = stack;
        openTileLocations = new ArrayList<>();
        regions = new HashMap<>();
        tigers = new ArrayList<>();
        setTileForPoint(stack.pop(), new Point(39, 39));
        openTileLocations.add(new Point(39, 40));
        openTileLocations.add(new Point(38, 39));
        openTileLocations.add(new Point(40, 39));
        openTileLocations.add(new Point(39, 38));
    }

    public void insert(Tile tile, int row, int col) throws BadPlacementException {
        Tile leftTile = getTile(new Point(row, col - 1));
        Tile rightTile = getTile(new Point(row, col + 1));
        Tile bottomTile = getTile(new Point(row + 1, col));
        Tile topTile = getTile(new Point(row - 1, col));
        if (leftTile == null && rightTile == null && topTile == null && bottomTile == null) {
            throw new BadPlacementException("Index given is out of bounds");
        }

        board[row][col] = tile;
        openTileLocations.remove(new Point(row, col));

        // Else statements to add open tiles next to the tile being placed if currently null
        if (leftTile != null) {
            connectLaterally(tile, leftTile);
        } else {
            openTileLocations.add(new Point(row, col - 1));
        }
        if (rightTile != null) {
            connectLaterally(rightTile, tile);
        } else {
            openTileLocations.add(new Point(row, col + 1));
        }
        if (topTile != null) {
            connectVertically(tile, topTile);
        } else {
            openTileLocations.add(new Point(row - 1, col));
        }
        if (bottomTile != null) {
            connectVertically(bottomTile, tile);
        } else {
            openTileLocations.add(new Point(row + 1, col));
        }
    }

    public void placeTiger(UUID regionId, Tiger tiger) throws BadPlacementException {
        Region region = regions.get(regionId);
        if (region.hasTigers()) {
            throw new BadPlacementException("Tried to place a tiger on a region that already has one.");
        }
        region.addTiger(tiger);
        tigers.add(tiger);
    }

    public Tile getTile(Point p){
        return board[p.x][p.y];
    }

    public Tile[][] getBoard() {
        return board;
    }

    public List<OpenTileLocation> returnValidPlacements(Tile tile) {
        List<OpenTileLocation> validPlacements = new ArrayList<>();
        for (int tileOrientation = 0; tileOrientation < 4; ++tileOrientation) {
            for (Point p : openTileLocations) {   // for each open tile
                Tile top = board[p.x - 1][p.y];
                Tile right = board[p.x][p.y + 1];
                Tile bottom = board[p.x + 1][p.y];
                Tile left = board[p.x][p.y - 1];

                System.out.println("For openTile " + p + ": ");
                System.out.println("    Top = " + (top != null));
                System.out.println("    Right = " + (right != null));
                System.out.println("    Bottom = " + (bottom != null));
                System.out.println("    Left = " + (left != null));


                if (top != null && !verticalConnectionIsValid(tile, top)) {
                    System.out.println("Vertical connection to top tile is invalid.");
                    continue;
                }
                if (right != null && !lateralConnectionIsValid(right, tile)) {
                    System.out.println("Lateral connection to right tile is invalid.");
                    continue;
                }
                if (bottom != null && !verticalConnectionIsValid(bottom, tile)) {
                    System.out.println("Vertical connection to bottom tile is invalid.");
                    continue;
                }
                if (left != null && !lateralConnectionIsValid(tile, left)) {
                    System.out.println("Lateral connection to left tile is invalid.");
                    continue;
                }

                System.out.println("Adding point " + p +
                        " with tile orientation " + tileOrientation +
                        " to valid tile placements.");
                OpenTileLocation validTileLocation = new OpenTileLocation(new Point(p.x, p.y), tileOrientation);
                validPlacements.add(validTileLocation);
            }
            // By placing this at the end the tile is rotated 4 times and thus comes back to original position
            tile.rotateClockwise(1);  // Rotate the tile 1 to check next orientation
        }
        return validPlacements;
    }

    public Stack<Tile> getTileStack(){
        return tileStack;
    }

    public List<Point> getTileOptions() {
        return openTileLocations;
    }

    private boolean lateralConnectionIsValid(Tile rightTile, Tile leftTile) {
        Node leftEdge = rightTile.getEdge(EdgeLocation.LEFT);
        Node rightEdge = leftTile.getEdge(EdgeLocation.RIGHT);
        boolean result = nodeConnectionIsValid(leftEdge, rightEdge);

        if (leftEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node topLeftCorner = rightTile.getCorner(CornerLocation.TOP_LEFT);
            Node topRightCorner = leftTile.getCorner(CornerLocation.TOP_RIGHT);
            result = result && nodeConnectionIsValid(topLeftCorner, topRightCorner);

            Node bottomLeftCorner = rightTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node bottomRightCorner = leftTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            result = result && nodeConnectionIsValid(bottomLeftCorner, bottomRightCorner);
        }

        return result;
    }

    private void connectLaterally(Tile rightTile, Tile leftTile) throws BadPlacementException {
        Node leftEdge = rightTile.getEdge(EdgeLocation.LEFT);
        Node rightEdge = leftTile.getEdge(EdgeLocation.RIGHT);
        connectNodes(leftEdge, rightEdge);

        if (leftEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node topLeftCorner = rightTile.getCorner(CornerLocation.TOP_LEFT);
            Node topRightCorner = leftTile.getCorner(CornerLocation.TOP_RIGHT);
            connectNodes(topLeftCorner, topRightCorner);

            Node bottomLeftCorner = rightTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node bottomRightCorner = leftTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            connectNodes(bottomLeftCorner, bottomRightCorner);
        }
    }

    private boolean verticalConnectionIsValid(Tile bottomTile, Tile topTile) {
        Node bottomEdge = topTile.getEdge(EdgeLocation.BOTTOM);
        Node topEdge = bottomTile.getEdge(EdgeLocation.TOP);
        boolean result = nodeConnectionIsValid(topEdge, bottomEdge);

        if (bottomEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node bottomRightCorner = topTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            Node topRightCorner = bottomTile.getCorner(CornerLocation.TOP_RIGHT);
            result = result && nodeConnectionIsValid(topRightCorner, bottomRightCorner);

            Node bottomLeftCorner = topTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node topleftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
            result = result && nodeConnectionIsValid(topleftCorner, bottomLeftCorner);
        }

        return result;
    }

    private void connectVertically(Tile bottomTile, Tile topTile) throws BadPlacementException {
        Node bottomEdge = topTile.getEdge(EdgeLocation.BOTTOM);
        Node topEdge = bottomTile.getEdge(EdgeLocation.TOP);
        connectNodes(topEdge, bottomEdge);

        if (bottomEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node bottomRightCorner = topTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            Node topRightCorner = bottomTile.getCorner(CornerLocation.TOP_RIGHT);
            connectNodes(topRightCorner, bottomRightCorner);

            Node bottomLeftCorner = topTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node topleftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
            connectNodes(topleftCorner, bottomLeftCorner);
        }
    }

    private boolean nodeConnectionIsValid(Node first, Node second) {
        if ((first == null && second != null) || (first != null && second == null) ||
            first.getTileSection().getTerrain() != second.getTileSection().getTerrain()) {
            return false;
        }
        else {
            return true;
        }
    }

    private void connectNodes(Node first, Node second) throws BadPlacementException {
        if ((first == null && second != null) || (first != null && second == null)) {
            throw new BadPlacementException("One corner is null and another is not");
        }

        if (first.getTileSection().getTerrain() != second.getTileSection().getTerrain()) {
            throw new BadPlacementException("Nodes have a mismatch of terrain: " +
                    first.getTileSection().getTerrain() +
                    " != " +
                    second.getTileSection().getTerrain());
        }

        if (first.getTileSection().getRegion() != null && second.getTileSection().getRegion() != null) {
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            first.getTileSection().getRegion().combineWithRegion(second.getTileSection().getRegion());
            regions.remove(second.getTileSection().getRegion().getRegionId());
        }
        else if (first.getTileSection().getRegion() != null) {
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            first.getTileSection().getRegion().addTileSection(second.getTileSection());
        }
        else if (second.getTileSection().getRegion() != null) {
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            second.getTileSection().getRegion().addTileSection(first.getTileSection());
        }
        else {
            Region newRegion = new Region(first.getTileSection().getTerrain());
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            newRegion.addTileSection(first.getTileSection());
            newRegion.addTileSection(second.getTileSection());
            regions.put(newRegion.getRegionId(), newRegion);
        }
    }

    private void setTileForPoint(Tile tile, Point point) {
        board[point.x][point.y] = tile;
    }
}
