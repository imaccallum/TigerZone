package entities.board;

import entities.board.Node;
import game.BadPlacementException;

public class Tile {

    //			Edge[0]
    //		Corner[0]	Corner[1]
    //	Edge[3]		Center		Edge[1]
    //		Corner[3]	Corner[2]
    //			Edge[2]
    private final int COUNT = 4; // Count of orientations
    private Node[] edges;
    private Node[] corners;
    private Node center;
    private Tile[] adjacentTiles; // Adjacent tiles
    private boolean hasPenant;


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

        if (adjacentTiles[0] != null) {
            cornerTiles[0] = adjacentTiles[0].getAdjacentTiles()[3];
            cornerTiles[1] = adjacentTiles[0].getAdjacentTiles()[1];
        }

        if (adjacentTiles[1] != null) {
            cornerTiles[1] = adjacentTiles[1].getAdjacentTiles()[0];
            cornerTiles[2] = adjacentTiles[1].getAdjacentTiles()[2];
        }

        if (adjacentTiles[2] != null) {
            cornerTiles[2] = adjacentTiles[2].getAdjacentTiles()[1];
            cornerTiles[3] = adjacentTiles[2].getAdjacentTiles()[3];
        }
        return cornerTiles;
    }

    public Tile getTile(int index) {
        return adjacentTiles[index];
    }

    public Node getCorner(CornerLocation index) {
        return corners[index.ordinal()];
    }

    public Node getEdge(EdgeLocation index) {
        return edges[index.ordinal()];
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

    private void setTile(Tile t, int i) throws BadPlacementException {
        if (i < 0 || i >= COUNT) throw new BadPlacementException("Illegal index");
        adjacentTiles[i] = t;
    }

    public void setTopTile(Tile t) throws BadPlacementException {
        setTile(t, 0);
    }

    public void setBottomTile(Tile t) throws BadPlacementException {
        setTile(t, 2);
    }

    public void setLeftTile(Tile t) throws BadPlacementException {
        setTile(t, 3);
    }

    public void setRightTile(Tile t) throws BadPlacementException {
        setTile(t, 1);
    }

    public void setEdge(Node node, int i){
        edges[i] = node;
    }
    
    public void setCorner(Node node, int i){
        corners[i] = node;
    }

    private int inverse(int i) {
        return (i + 2) % COUNT;
    }

    public boolean hasPenant() {
        return hasPenant;
    }

    public void setCenter(Node center) {
        this.center = center;
    }
}
