package entities.board;

import entities.board.Node.Node;

public class Tile {

    //			Edge[0]
    //		Corner[3]	Corner[0]
    //	Edge[3]		Center		Edge[1]
    //		Corner[2]	Corner[1]
    //			Edge[2]
<<<<<<< HEAD

	private final int COUNT = 4;
=======
    private final int COUNT = 4;
>>>>>>> master
    private Node[] edges;
    private Node[] corners;
    private Node center;
    private Tile[] tiles; // Adjacent tiles
    private int orientation; // 0 = 0, 1 = 90, 2 = 180, 3 = 270 degrees
    private int score = 1;



    public Tile() {
        edges = new Node[COUNT];
        corners = new Node[COUNT];
        tiles = new Tile[COUNT];
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public Tile getTile(int index) {
        return null;
    }

    public Node getCorner(int index) {
        return null;
    }

    public  Node getEdge(int index) {
        return null;
    }

    public void setOrientation(int o) {
        orientation = o;
    }

    public void rotate(int r) {
        orientation = (orientation + r) % COUNT;
    }


    public void attachLeft(Tile t) {
        // link corresponding nodes
    }

    public void attachRight(Tile t) {
        // link corresponding nodes
    }

    public void attachBelow(Tile t) {
        // link corresponding nodes
    }
    public void attachAbove(Tile t) {
        // link corresponding nodes
    }

    private void setTile(Tile t, int i) {
        if (i < 0 || i >= COUNT) throw new RuntimeException("Illegal index");
        tiles[i] = t;
        t.getTiles()[inverse(i)] = this;
    }
    private int inverse(int i) {
        return (i + 2) % COUNT;
    }
}
