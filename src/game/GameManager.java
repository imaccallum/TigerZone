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
    private Tile[][] map;					// The map of tiles

    private ArrayList<Point> openTiles;		// A list of all current open tile positions
    private Point lastTilePlaced;			// Set on insert, used for getTigerPlacementOptions()

    private List<Player> players;
    private int playerTurn;

    // *TODO PlayerNotifier notifier;
    // *TODO RegionLinker regionLinker;

    public GameManager(Stack<Tile> stack, Player... p) {
        for(Player player : p)
            players.add(player);

        tileStack = stack;
        map = new Tile[80][80]; 			// .648 Kb of overhead for array + storage = not a problem

        insert(tileStack.pop(), 40, 40);	// Assuming starting tile is placed at top of stack
    }

    public ArrayList<Point> getTileOptions() {
        return openTiles;
    }
    public void insert(Tile t, int x, int y) {

        // update openTiles & attach to adjacent tiles

        if(map[x+1][y] == null)
            openTiles.add(new Point(x+1, y));
        else
            map[x+1][y].setTile(t, 3);


        if(map[x-1][y] == null)
            openTiles.add(new Point(x-1, y));
        else
            map[x-1][y].setTile(t, 1);


        if(map[x][y+1] == null)
            openTiles.add(new Point(x, y+1));
        else
            map[x][y+1].setTile(t, 2);


        if(map[x][y-1] == null)
            openTiles.add(new Point(x, y-1));
        else
            map[x][y-1].setTile(t, 0);

        attach(t);

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

    public ArrayList<Node> getTigerPlacementOptions() {
        return null;
    }

    public static void main(String[] args) throws IOException {

    }

}