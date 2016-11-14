package game;

import entities.board.CornerLocation;
import entities.board.EdgeLocation;
import entities.board.Node;
import entities.board.Tile;
import entities.overlay.Region;
import entities.player.Player;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameManager {

    private Stack<Tile> tileStack;			// The stack of tiles given [empty(), peek(), pop(), push(), search()]
    private Tile center;					// The map of tiles
    private List<Point> openTiles;		    // A list of all current open tile positions
    private Point lastTilePlaced;
    private List<Region> regions;

    private List<Player> players;
    private int playerTurn;

    // *TODO PlayerNotifier notifier;
    // *TODO RegionLinker regionLinker;

    public GameManager(Stack<Tile> stack, Player... players) {
        for(Player player : players) {
            this.players.add(player);
        }

        tileStack = stack;
        center = tileStack.pop();
        openTiles = new ArrayList<>();
    }

    public List<Point> getTileOptions() {
        return openTiles;
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
        }
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

        Node topLeftCorner = rightTile.getCorner(CornerLocation.TOP_LEFT);
        Node topRightCorner = leftTile.getCorner(CornerLocation.TOP_RIGHT);
        attemptNodeConnection(topLeftCorner, topRightCorner);

        Node bottomLeftCorner = rightTile.getCorner(CornerLocation.BOTTOM_LEFT);
        Node bottomRightCorner = leftTile.getCorner(CornerLocation.BOTTOM_RIGHT);
        attemptNodeConnection(bottomLeftCorner, bottomRightCorner);
    }

    private void attemptVerticalConnection(Tile bottomTile, Tile topTile) throws BadPlacementException {
        Node bottomEdge = topTile.getEdge(EdgeLocation.BOTTOM);
        Node topEdge = bottomTile.getEdge(EdgeLocation.TOP);
        attemptNodeConnection(topEdge, bottomEdge);

        Node bottomRightCorner = topTile.getCorner(CornerLocation.BOTTOM_RIGHT);
        Node topRightCorner = bottomTile.getCorner(CornerLocation.TOP_RIGHT);
        attemptNodeConnection(topRightCorner, bottomRightCorner);

        Node bottomLeftCorner = topTile.getCorner(CornerLocation.BOTTOM_LEFT);
        Node topleftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
        attemptNodeConnection(topleftCorner, bottomLeftCorner);
    }

    private void attemptNodeConnection(Node first, Node second) throws BadPlacementException {
        if ((first == null && second != null) || (first != null && second == null)) {
            throw new BadPlacementException("One corner is null and another is not");
        }

        if (first.getTileSection().getTerrain() != second.getTileSection().getTerrain()) {
            throw new BadPlacementException("Nodes have a mismatch of terrain.");
        }

        if (first.getTileSection().getRegion() != null && second.getTileSection().getRegion() != null) {
            first.getTileSection().getRegion().combineWithRegion(second.getTileSection().getRegion());
            regions.remove(second.getTileSection().getRegion());
        }
        else if (first.getTileSection().getRegion() != null) {
            first.getTileSection().getRegion().addTileSection(second.getTileSection());
        }
        else if (second.getTileSection().getRegion() != null) {
            second.getTileSection().getRegion().addTileSection(first.getTileSection());
        }
        else {
            Region newRegion = new Region();
            newRegion.addTileSection(first.getTileSection());
            newRegion.addTileSection(second.getTileSection());
            regions.add(newRegion);
        }
    }

    public static void main(String[] args) throws IOException {

    }

}