package entities.board;

import entities.board.Node;
import game.BadPlacementException;

public class Tile {

    //			Edge[0]
    //		Corner[3]	Corner[0]
    //	Edge[3]		Center		Edge[1]
    //		Corner[2]	Corner[1]
    //			Edge[2]
    private final int COUNT = 4; // Count of orientations
    private Node[] edges;
    private Node[] corners;
    private Node center;
    private Tile[] adjacentTiles; // Adjacent tiles
    private int orientation; // 0 = 0, 1 = 90, 2 = 180, 3 = 270 degrees]


    public Tile() {
        edges = new Node[COUNT];
        corners = new Node[COUNT];
        adjacentTiles = new Tile[COUNT];
    }

    public Tile[] getAdjacentTiles() {
        return adjacentTiles;
    }

    public Tile[] getCornerTiles() {
        Tile[] cornerTiles = new Tile[4];

        if(adjacentTiles[0] != null){
            cornerTiles[0] = adjacentTiles[0].getAdjacentTiles()[3];
            cornerTiles[1] = adjacentTiles[0].getAdjacentTiles()[1];
        }

        if(adjacentTiles[1] != null){
            cornerTiles[1] = adjacentTiles[1].getAdjacentTiles()[0];
            cornerTiles[2] = adjacentTiles[1].getAdjacentTiles()[2];
        }

        if(adjacentTiles[2] != null){
            cornerTiles[2] = adjacentTiles[2].getAdjacentTiles()[1];
            cornerTiles[3] = adjacentTiles[2].getAdjacentTiles()[3];
        }

        if(adjacentTiles[3] != null){
            cornerTiles[3] = adjacentTiles[3].getAdjacentTiles()[2];
            cornerTiles[0] = adjacentTiles[3].getAdjacentTiles()[0];
        }

        return cornerTiles;
    }

    public Tile getTile(int index) { return adjacentTiles[adjustedIndex(index)]; }

    public Node getCorner(CornerLocation index) {
        return corners[adjustedIndex(index.ordinal())];
    }

    public Node getEdge(EdgeLocation index) {
        return edges[adjustedIndex(index.ordinal())];
    }

    public Node getCenter(int index) {
        return center;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation % COUNT;
    }

    public void rotateClockwise(int numberOfRotations) {
        setOrientation((orientation + numberOfRotations) % COUNT);
    }

    private void setTile(Tile t, int i) {
        if (i < 0 || i >= COUNT) throw new BadPlacementException("Illegal index");
        adjacentTiles[adjustedIndex(i)] = t;
        t.getAdjacentTiles()[inverse(i)] = this;
    }

    public void setTopTile(Tile t) {
        setTile(t, 0);
    }

    public void setBottomTile(Tile t) {
        setTile(t, 2);
    }

    public void setLeftTile(Tile t) {
        setTile(t, 3);
    }

    public void setRightTile(Tile t) {
        setTile(t, 1);
    }

    public void setedge(Node node, int i){
        edges[i] = node;
    }
    
    public void setcorner(Node node, int i){
        corners[i] = node;
    }

    // Helpers
    private int adjustedIndex(int i) {
        return (i + orientation) % COUNT;
    }

    private int inverse(int i) {
        return (i + 2) % COUNT;
    }
}
