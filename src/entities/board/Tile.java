package entities.board;

import entities.board.Node.Node;

public class Tile {

    //			Edge[0]
    //		Corner[3]	Corner[0]
    //	Edge[3]		Center		Edge[1]
    //		Corner[2]	Corner[1]
    //			Edge[2]
    private final int ORIENTATIONS_COUNT = 4;
    private Node[] edges;
    private Node[] corners;
    private Node center;
    private Tile[] tiles; // Adjacent tiles
    private int orientation; // 0 = 0, 1 = 90, 2 = 180, 3 = 270 degrees
    private int score = 1;

    public Tile() {
        edges = new Node[ORIENTATIONS_COUNT];
        corners = new Node[ORIENTATIONS_COUNT];
        tiles = new Tile[ORIENTATIONS_COUNT];
    }

    public Tile[] getTiles() {
        return tiles;
    }
    public Tile getTile(int index) { return tiles[adjustedIndex(index)]; }
    public Node getCorner(int index) {
        return corners[adjustedIndex(index)];
    }
    public Node getEdge(int index) {
        return edges[adjustedIndex(index)];
    }

    public void setOrientation(int o) {
        orientation = o % ORIENTATIONS_COUNT;
    }
    public void rotate(int r) {
        orientation = (orientation + r) % ORIENTATIONS_COUNT;
    }

    private void setTile(Tile t, int i) {
        if (i < 0 || i >= ORIENTATIONS_COUNT) throw new RuntimeException("Illegal index");
        tiles[i] = t;
        t.getTiles()[inverse(i)] = this;
    }

    // Helpers
    private int adjustedIndex(int i) { return (i + orientation) % ORIENTATIONS_COUNT; }
    private int inverse(int i) {
        return (i + 2) % ORIENTATIONS_COUNT;
    }
}
