package game;

import entities.board.Tile;
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
            leftTile.setRightTile(tile);
            tile.setLeftTile(leftTile);
            tile.getEdge(3).setConnectedNode(leftTile.getEdge(1));
            if (leftTile.getRight(3).get)
        }
        if (rightTile != null) {
            rightTile.setLeftTile(tile);
            tile.setRightTile(rightTile);
        }
        if (topTile != null) {
            topTile.setBottomTile(tile);
            tile.setTopTile(topTile);
        }
        if (bottomTile != null) {
            bottomTile.setTopTile(tile);
            tile.setBottomTile(bottomTile);
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

    public static void main(String[] args) throws IOException {

    }

}