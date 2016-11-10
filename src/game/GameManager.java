package game;

import entities.board.Node.Node;
import entities.board.Tile;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class GameManager {

    private Stack<Tile> tileStack;			// The stack of tiles given [empty(), peek(), pop(), push(), search()]
    private Tile[][] map;					// The map of tiles

    private ArrayList<Point> openTiles;		// A list of all current open tile positions
    private Point lastTilePlaced;			// Set on insert, used for getTigerPlacementOptions()

    private int playerOneScore = 0;
    private int playerTwoScore = 0;

    public GameManager(Stack<Tile> stack) {
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
            map[x+1][y].attachLeft(t);


        if(map[x-1][y] == null)
            openTiles.add(new Point(x-1, y));
        else
            map[x-1][y].attachRight(t);


        if(map[x][y+1] == null)
            openTiles.add(new Point(x, y+1));
        else
            map[x][y+1].attachBelow(t);


        if(map[x][y-1] == null)
            openTiles.add(new Point(x, y-1));
        else
            map[x][y-1].attachAbove(t);

    }

    public ArrayList<Node> getTigerPlacementOptions() {
        return null;
    }


    public static void main(String[] args) throws IOException {

    }

}
