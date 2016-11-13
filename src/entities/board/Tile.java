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


    public Tile() {
        edges = new Node[COUNT];
        corners = new Node[COUNT];
        adjacentTiles = new Tile[COUNT];
    }

    public Tile[] getAdjacentTiles() {
        return adjacentTiles;
    }

    public Tile getTile(int index) { return adjacentTiles[index]; }

    public Node getCorner(int index) {
        return corners[index];
    }

    public Node getEdge(int index) {
        return edges[index];
    }

    public Node getCenter(int index) {
        return center;
    }

    public void rotateClockwise(int numberOfRotations) {

        Node[] tempEdges = new Node[COUNT];
        Node[] tempCorners = new Node[COUNT];

        for (int i = 0; i < COUNT; i++) {
            int index = (i + numberOfRotations) % COUNT;
            tempEdges[index] = edges[i];
            tempCorners[index] = corners[i];
        }

        edges = tempEdges;
        corners = tempCorners;
    }

    private void setTile(Tile t, int index) {
        if (index < 0 || index >= COUNT) throw new BadPlacementException("Illegal index");
        adjacentTiles[index] = t;
    }

    public void setTopTile(Tile t) {
        setTile(t, 0);
    }

    public void setRightTile(Tile t) {
        setTile(t, 1);
    }

    public void setBottomTile(Tile t) {
        setTile(t, 2);
    }

    public void setLeftTile(Tile t) {
        setTile(t, 3);
    }

    public void setEdge(Node node, int i){
        edges[i] = node;
    }
    
    public void setCorner(Node node, int i){
        corners[i] = node;
    }
}
