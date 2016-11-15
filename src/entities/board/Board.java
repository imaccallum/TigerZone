package entities.board;

import entities.overlay.Region;
import entities.player.Player;
import game.BadPlacementException;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {
    private Stack<Tile> tileStack;			// The stack of tiles given [empty(), peek(), pop(), push(), search()]
    private Tile center;					// The map of tiles
    private List<Point> openTiles;		    // A list of all current open tile positions
    private Map<UUID, Region> regions;
    private List<Tiger> tigers;

    public Board(Stack<Tile> stack) {
        tileStack = stack;
        center = tileStack.pop();
        openTiles = new ArrayList<>();
        regions = new HashMap<>();
        tigers = new ArrayList<>();
    }

    public void insert(Tile tile, int x, int y) throws BadPlacementException {
        Tile leftTile = getTile(new Point(x + 1, y));
        Tile rightTile = getTile(new Point(x - 1, y));
        Tile bottomTile = getTile(new Point(x, y - 1));
        Tile topTile = getTile(new Point(x, y + 1));
        if (leftTile == null && rightTile == null && topTile == null && bottomTile == null) {
            throw new BadPlacementException("Index given is out of bounds");
        }

        if (leftTile != null) {
            attemptLateralConnection(tile, leftTile);
            leftTile.setRightTile(tile);
        }
        if (rightTile != null) {
            attemptLateralConnection(rightTile, tile);
            rightTile.setLeftTile(tile);
        }
        if (topTile != null) {
            attemptVerticalConnection(tile, topTile);
            topTile.setBottomTile(tile);
        }
        if (bottomTile != null) {
            attemptVerticalConnection(bottomTile, tile);
            bottomTile.setTopTile(tile);
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

    private Tile getTile(Point point) {
        Tile currentTile = center;
        int x = point.x;
        int y = point.y;
        while (x != 0 && y != 0) {
            boolean iterated = false;
            if (x < 0) {
                Tile nextTile = iterateRight(currentTile);
                if (nextTile != null) {
                    currentTile = nextTile;
                    iterated = true;
                    ++x;
                }
            }
            if (!iterated && x > 0) {
                Tile nextTile = iterateLeft(currentTile);
                if (nextTile != null) {
                    currentTile = nextTile;
                    iterated = true;
                    --x;
                }
            }
            if (!iterated && y < 0) {
                Tile nextTile = iterateDown(currentTile);
                if (nextTile != null) {
                    currentTile = nextTile;
                    iterated = true;
                    ++y;
                }
            }
            if (!iterated) {
                Tile nextTile = iterateUp(currentTile);
                if (nextTile != null) {
                    currentTile = nextTile;
                    iterated = true;
                    --y;
                }
            }

            if (currentTile == null) {
                return null;
            }
        }
        return currentTile;
    }


    public List<Point> getTileOptions() {
        return openTiles;
    }

    private Tile iterateUp(Tile current) {
        return current.getTile(0);
    }

    private Tile iterateDown(Tile current) {
        return current.getTile(2);
    }

    private Tile iterateRight(Tile current) {
        return current.getTile(1);
    }

    private Tile iterateLeft(Tile current) {
        return current.getTile(3);
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
            throw new BadPlacementException("Nodes have a mismatch of terrain.");
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
}
