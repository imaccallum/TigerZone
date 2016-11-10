package entities.board;

import entities.board.Node.Node;

public class Tile {

    //			Edge[0]
    //		Corner[3]	Corner[0]
    //	Edge[3]		Center		Edge[1]
    //		Corner[2]	Corner[1]
    //			Edge[2]
    private final int COUNT = 4;
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
    public Tile getTile(int index) { return tiles[adjustedIndex(i)]; }
    public Node getCorner(int index) {
        return corners[adjustedIndex(i)];
    }
    public Node getEdge(int index) {
        return edges[adjustedIndex(i)];
    }

    public void setOrientation(int o) {
        orientation = o % COUNT;
    }
    public void rotate(int r) {
        orientation = (orientation + r) % COUNT;
    }

    private void setTile(Tile t, int i) {
        if (i < 0 || i >= COUNT) throw new RuntimeException("Illegal index");
        tiles[i] = t;
        t.getTiles()[inverse(i)] = this;
    }

    // Helpers
    private int adjustedIndex(int i) { return (i + orientation) % COUNT }
    private int inverse(int i) {
        return (i + 2) % COUNT;
    }
}
