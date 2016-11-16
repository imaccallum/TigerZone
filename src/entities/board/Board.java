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
        setTileForPoint(stack.pop(), new Point(49, 49));
        openTileLocations.add(new Point(39, 40));
        openTileLocations.add(new Point(38, 39));
        openTileLocations.add(new Point(40, 39));
        openTileLocations.add(new Point(39, 38));
    }

    public void insert(Tile tile, int i, int j) throws BadPlacementException {
        Tile leftTile = getTile(new Point(i, j - 1));
        Tile rightTile = getTile(new Point(i, j + 1));
        Tile bottomTile = getTile(new Point(i + 1, j));
        Tile topTile = getTile(new Point(i - 1, j));
        if (leftTile == null && rightTile == null && topTile == null && bottomTile == null) {
            throw new BadPlacementException("Index given is out of bounds");
        }

        board[i][j] = tile;
        openTileLocations.remove(new Point(i, j));

        // Else statements to add open tiles next to the tile being placed if currently null
        if (leftTile != null) {
            attemptLateralConnection(tile, leftTile);
            leftTile.setRightTile(tile);
        } else {
            openTileLocations.add(new Point(i, j - 1));
        }
        if (rightTile != null) {
            attemptLateralConnection(rightTile, tile);
            rightTile.setLeftTile(tile);
        } else {
            openTileLocations.add(new Point(i, j + 1));
        }
        if (topTile != null) {
            attemptVerticalConnection(tile, topTile);
            topTile.setBottomTile(tile);
        } else {
            openTileLocations.add(new Point(i - 1, j));
        }
        if (bottomTile != null) {
            attemptVerticalConnection(bottomTile, tile);
            bottomTile.setTopTile(tile);
        } else {
            openTileLocations.add(new Point(i + 1, j));
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

    public List<Point> returnValidPlacements(Tile tile) {
        List<Point> validPlacements = new ArrayList<>();

        for(Point p : openTileLocations) {   // for each open tile
            Tile top = board[p.x-1][p.y];
            Tile right = board[p.x][p.y+1];
            Tile bottom = board[p.x+1][p.y];
            Tile left = board[p.x][p.y-1];

            System.out.println("For openTile " + p + ": ");
            System.out.println("    Top = " + (top != null));
            System.out.println("    Right = " + (right != null));
            System.out.println("    Bottom = " + (bottom != null));
            System.out.println("    Left = " + (left != null));


            if(top != null) {
                Node adjEdge = top.getEdge(EdgeLocation.BOTTOM);
                Node topEdge = tile.getEdge(EdgeLocation.TOP);

                System.out.println("topEdge = " + topEdge.getTileSection().getTerrain());
                System.out.println("adj@Bottom = " + adjEdge.getTileSection().getTerrain());

                //If adj edge is incompatible, go to next open tile
                if(adjEdge.getTileSection().getTerrain() != topEdge.getTileSection().getTerrain()){
                    System.out.println("Aborted " + p );
                    continue;
                }
            }
            if(right != null) {
                Node adjEdge = right.getEdge(EdgeLocation.LEFT);
                Node rightEdge = tile.getEdge(EdgeLocation.RIGHT);

                System.out.println("rightEdge = " + rightEdge.getTileSection().getTerrain());
                System.out.println("adj@Left = " + adjEdge.getTileSection().getTerrain());

                //If adj edge is incompatible, go to next open tile
                if(adjEdge.getTileSection().getTerrain() != rightEdge.getTileSection().getTerrain()){
                    System.out.println("Aborted " + p );
                    continue;
                }
            }
            if(bottom != null) {
                Node adjEdge = bottom.getEdge(EdgeLocation.TOP);
                Node bottomEdge = tile.getEdge(EdgeLocation.BOTTOM);

                System.out.println("bottomEdge = " + bottomEdge.getTileSection().getTerrain());
                System.out.println("adj@Top = " + adjEdge.getTileSection().getTerrain());

                //If adj edge is incompatible, go to next open tile
                if(adjEdge.getTileSection().getTerrain() != bottomEdge.getTileSection().getTerrain()){
                    System.out.println("Aborted " + p );
                    continue;
                }
            }
            if(left != null) {
                Node adjEdge = left.getEdge(EdgeLocation.RIGHT);
                Node leftEdge = tile.getEdge(EdgeLocation.LEFT);

                System.out.println("leftEdge = " + leftEdge.getTileSection().getTerrain());
                System.out.println("adj@Right = " + adjEdge.getTileSection().getTerrain());

                //If adj edge is incompatible, go to next open tile
                if(adjEdge.getTileSection().getTerrain() != leftEdge.getTileSection().getTerrain()){
                    System.out.println("Aborted " + p );
                    continue;
                }
            }
            System.out.println("Added " + p);
            validPlacements.add(p);
        }

        return validPlacements;
    }

    public Stack<Tile> getTileStack(){
        return tileStack;
    }

    public List<Point> getTileOptions() {
        return openTileLocations;
    }

    private void attemptLateralConnection(Tile rightTile, Tile leftTile) throws BadPlacementException {
        Node leftEdge = rightTile.getEdge(EdgeLocation.LEFT);
        Node rightEdge = leftTile.getEdge(EdgeLocation.RIGHT);
        attemptNodeConnection(leftEdge, rightEdge);

        if (leftEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node topLeftCorner = rightTile.getCorner(CornerLocation.TOP_LEFT);
            Node topRightCorner = leftTile.getCorner(CornerLocation.TOP_RIGHT);
            attemptNodeConnection(topLeftCorner, topRightCorner);

            Node bottomLeftCorner = rightTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node bottomRightCorner = leftTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            attemptNodeConnection(bottomLeftCorner, bottomRightCorner);
        }
    }

    private void attemptVerticalConnection(Tile bottomTile, Tile topTile) throws BadPlacementException {
        Node bottomEdge = topTile.getEdge(EdgeLocation.BOTTOM);
        Node topEdge = bottomTile.getEdge(EdgeLocation.TOP);
        attemptNodeConnection(topEdge, bottomEdge);

        if (bottomEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node bottomRightCorner = topTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            Node topRightCorner = bottomTile.getCorner(CornerLocation.TOP_RIGHT);
            attemptNodeConnection(topRightCorner, bottomRightCorner);

            Node bottomLeftCorner = topTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node topleftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
            attemptNodeConnection(topleftCorner, bottomLeftCorner);
        }
    }

    private void attemptNodeConnection(Node first, Node second) throws BadPlacementException {
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