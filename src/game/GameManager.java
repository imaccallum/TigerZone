package game;

import entities.board.Node.Terrain;
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
                currentTile.attachTop(tile);
            }
            else if (x == 0 && y == 1) {

            }
            else if (x == -1 && y == 0) {

            }
            else if (x == 0 & y == -1) {

            }

            if (x < 0) {

            }
            else {

            }

            if (y < 0) {

            }
            else  {

            }

            if (currentTile == null) {
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
        Tile[] adjTiles = t.getTiles();
        for(int i = 0; i < 4; i++) {	// for each side
            int inverseIndex = (i + 2) % 4;
            if(adjTiles[i] != null) {	// if tile on that side
                if(adjTiles[i].getEdge(inverseIndex) == t.getEdge(i)) { //Adj Node  is same type -> should always be true
                    adjTiles[i].getEdge(inverseIndex).setConnection(t.getEdge(i));
                    t.getEdge(i).setConnection(adjTiles[i].getEdge(inverseIndex));		// Set link both ways
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