package game;

import entities.board.Node;
import entities.board.Terrain;
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
        Tile currentTile = center;
        while (x != 0 && y != 0) {
            if (x == 1 && y == 0) {
                currentTile.attachRight(tile);
                break;
            }
            else if (x == 0 && y == 1) {
                currentTile.attachTop(tile);
                break;
            }
            else if (x == -1 && y == 0) {
                currentTile.attachLeft(tile);
                break;
            }
            else if (x == 0 & y == -1) {
                currentTile.attachLeft(tile);
                break;
            }


            boolean iterated = false;
            if (!iterated && x < 0) {
                Tile nextTile = iterateRight(currentTile);
                if (nextTile != null) {
                    currentTile = nextTile;
                }
            }
            if (!iterated && x > 0) {
                Tile nextTile = iterateLeft(currentTile);
                if (nextTile != null) {
                    currentTile = nextTile;
                }
            }
            if (!iterated && y < 0) {
                Tile nextTile = iterateDown(currentTile);
                if (nextTile != null) {
                    currentTile = nextTile;
                }
            }
            else if (!iterated) {
                Tile nextTile = iterateUp(currentTile);
                if (nextTile != null) {
                    currentTile = nextTile;
                }
            }

            if (!iterated) {
                throw new BadPlacementException("Index given is out of bounds");
            }
        }
    }

    Tile iterateUp(Tile current) {
        return current.getTile(0);
    }

    Tile iterateDown(Tile current) {
        return current.getTile(2);
    }

    Tile iterateRight(Tile current) {
        return current.getTile(1);
    }

    Tile iterateLeft(Tile current) {
        return current.getTile(3);
    }

    private void attach(Tile t) {
        Tile[] adjTiles = t.getAdjacentTiles();
        for(int i = 0; i < 4; i++) {	// for each side
            int inverseIndex = (i + 2) % 4;
            if(adjTiles[i] != null) {	// if tile on that side
                if(adjTiles[i].getEdge(inverseIndex) == t.getEdge(i)) { //Adj Node  is same type -> should always be true
                    adjTiles[i].getEdge(inverseIndex).setConnectedNode(t.getEdge(i));
                    t.getEdge(i).setConnectedNode(adjTiles[i].getEdge(inverseIndex));		// Set link both ways
                    if(t.getEdge(i).getTerrain() == Terrain.TRAIL) {
                        // This may need to change, but I believe fields need to only be linked through road sides
                        int adjCornerIndex1 = inverseIndex;
                        int adjCornerIndex2 = inverseIndex - 1;
                        if(adjCornerIndex2 == -1) adjCornerIndex2 = 3;
                        //connect corners
                    }
                }
                else {
                    System.out.println("Error: Cannot add Tile there, incompatible nodes.");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

    }

}