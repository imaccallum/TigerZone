package entities.board;

import entities.overlay.Region;
import entities.overlay.TigerDen;
import entities.overlay.TileSection;
import exceptions.BadPlacementException;
import exceptions.IncompatibleTerrainException;
import game.LocationAndOrientation;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    private Tile[][] boardMatrix;
    private List<Point> openTileLocations;
    private Map<UUID, Region> regions;
    private List<TigerDen> tigerDens;
    private Tile lastTilePlaced;


    public Board(int stackSize, Tile firstTile) {
        boardMatrix = new Tile[stackSize * 2][stackSize * 2];
        openTileLocations = new ArrayList<>();
        regions = new HashMap<>();

        // Put the first tile down and set all of the open tile locations
        setTileForPoint(firstTile, new Point(stackSize - 1, stackSize - 1));
        openTileLocations.add(new Point(stackSize - 1, stackSize));
        openTileLocations.add(new Point(stackSize - 2, stackSize - 1));
        openTileLocations.add(new Point(stackSize, stackSize - 1));
        openTileLocations.add(new Point(stackSize - 1, stackSize - 2));
    }

    public void insert(Tile tile, Point location) throws BadPlacementException {
        // For naming consistent with orientation of tile matrix, get x and y as row, col integers
        int row = location.x;
        int col = location.y;

        // Get the surrounding tiles of the placement.
        Tile leftTile = getTile(new Point(row, col - 1));
        Tile rightTile = getTile(new Point(row, col + 1));
        Tile bottomTile = getTile(new Point(row + 1, col));
        Tile topTile = getTile(new Point(row - 1, col));

        // If they are all null, we are trying to place a tile that will not be adjacent to any other tile and thus
        // throw a bad tile placement exception.
        if (leftTile == null && rightTile == null && topTile == null && bottomTile == null) {
            throw new BadPlacementException("Index given is out of bounds");
        }

        // Put the tile in the matrix, get ready to connect the tiles.
        boardMatrix[row][col] = tile;
        openTileLocations.remove(new Point(row, col));

        // For each non-null tile, connect the tile's tileSections / regions / nodes so that the overlay is updated
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

        if (tile.hasDen()) {
            tigerDens.add(new TigerDen(location, this));
        }

        lastTilePlaced = tile;
    }

    public List<LocationAndOrientation> findValidTilePlacements(Tile tile) {
        List<LocationAndOrientation> validPlacements = new ArrayList<>();
        for (Point openTileLocation : openTileLocations) {   // for each open tile
            for (int tileOrientation = 0; tileOrientation < 4; ++tileOrientation) {
                // By placing this at the end the tile is rotated 4 times and thus comes back to original position
                tile.rotateClockwise(1);  // Rotate the tile 1 to check next orientation
                int row = openTileLocation.x;
                int col = openTileLocation.y;
                Tile top = boardMatrix[row - 1][col];
                Tile right = boardMatrix[row][col + 1];
                Tile bottom = boardMatrix[row + 1][col];
                Tile left = boardMatrix[row][col - 1];

                System.out.println("For openTile " + openTileLocation + ": ");
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

                System.out.println("Adding point " + openTileLocation +
                        " with tile orientation " + tileOrientation +
                        " to valid tile placements.");
                Point current = new Point(openTileLocation.x, openTileLocation.y);
                LocationAndOrientation locationAndOrientation = new LocationAndOrientation(current, tileOrientation);
                validPlacements.add(locationAndOrientation);
            }
        }
        return validPlacements;
    }

    public Tile getTile(Point tileLocation) {
        return boardMatrix[tileLocation.x][tileLocation.y];
    }

    public List<TileSection> getPossibleTileSectionTigerPlacements() {
        List<TileSection> tigerPlacementPossibilities = new ArrayList<>();
        List<TileSection> lastTileSections = lastTilePlaced.getTileSections();
        tigerPlacementPossibilities.addAll(lastTileSections.parallelStream().filter(this::canPlaceTiger)
                                           .collect(Collectors.toList()));
        return tigerPlacementPossibilities;
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
            Node topLeftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
            connectNodes(topLeftCorner, bottomLeftCorner);
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
            try {
                first.getTileSection().getRegion().combineWithRegion(second.getTileSection().getRegion());
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
            regions.remove(second.getTileSection().getRegion().getRegionId());
        }
        else if (first.getTileSection().getRegion() != null) {
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            try {
                first.getTileSection().getRegion().addTileSection(second.getTileSection());
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
        }
        else if (second.getTileSection().getRegion() != null) {
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            try {
                second.getTileSection().getRegion().addTileSection(first.getTileSection());
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
        }
        else {
            Region newRegion = new Region(first.getTileSection().getTerrain());
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            try {
                newRegion.addTileSection(first.getTileSection());
                newRegion.addTileSection(second.getTileSection());
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
            regions.put(newRegion.getRegionId(), newRegion);
        }
    }

    private void setTileForPoint(Tile tile, Point point) {
        boardMatrix[point.x][point.y] = tile;
    }

    // Checks to see if a tiger can be placed
    private boolean canPlaceTiger(TileSection section) {
        Region region = section.getRegion();
        if (region.containsTigers()) {
            return false;
        }
        else {
            return true;
        }
    }
}
