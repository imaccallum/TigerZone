package entities.board;

import entities.board.Node;
import entities.overlay.TileSection;
import game.BadPlacementException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private boolean hasDeer;
    private boolean hasBuffalo;
    private boolean hasBoar;
    private List<TileSection> tileSections;

    public Tile() {
        edges = new Node[COUNT];
        corners = new Node[COUNT];
        adjacentTiles = new Tile[COUNT];
        tileSections = new ArrayList<>();
        hasDeer = false;
        hasBoar = false;
        hasBuffalo = false;
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

    public List<TileSection> getTileSections(){
        return tileSections;
    }

    public void rotateClockwise(int numberOfRotations) {

        Node[] tempEdges = new Node[COUNT];
        Node[] tempCorners = new Node[COUNT];
        int index;

        for (int i = 0; i < COUNT; i++) {
            index = (i + numberOfRotations) % COUNT;
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
        this.getEdge(EdgeLocation.TOP).setConnectedNode(t.getEdge(EdgeLocation.BOTTOM));
        t.getEdge(EdgeLocation.BOTTOM).setConnectedNode(this.getEdge(EdgeLocation.TOP));
    }

    public void setBottomTile(Tile t) throws BadPlacementException {
        setTile(t, 2);
        this.getEdge(EdgeLocation.BOTTOM).setConnectedNode(t.getEdge(EdgeLocation.TOP));
        t.getEdge(EdgeLocation.TOP).setConnectedNode(this.getEdge(EdgeLocation.BOTTOM));
    }

    public void setLeftTile(Tile t) throws BadPlacementException {
        setTile(t, 3);
        this.getEdge(EdgeLocation.LEFT).setConnectedNode(t.getEdge(EdgeLocation.RIGHT));
        t.getEdge(EdgeLocation.RIGHT).setConnectedNode(this.getEdge(EdgeLocation.LEFT));
    }

    public void setRightTile(Tile t) throws BadPlacementException {
        setTile(t, 1);
        this.getEdge(EdgeLocation.RIGHT).setConnectedNode(t.getEdge(EdgeLocation.LEFT));
        t.getEdge(EdgeLocation.LEFT).setConnectedNode(this.getEdge(EdgeLocation.RIGHT));
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

    public void setCenter(Node center) {
        this.center = center;
    }

    public void addTileSections(TileSection... sections){
        for (TileSection tileSection: sections){
            tileSection.setTile(this);
        }
        tileSections.addAll(Arrays.asList(sections));

    }

    public Node[] getEdges(){
        return edges;
    }

    public boolean hasDeer() {
        return hasDeer;
    }

    public boolean hasBuffalo() {
        return hasBuffalo;
    }

    public boolean hasBoar() {
        return hasBoar;
    }

    public void setHasDeer(boolean hasDeer) {
        this.hasDeer = hasDeer;
    }

    public void setHasBuffalo(boolean hasBuffalo) {
        this.hasBuffalo = hasBuffalo;
    }

    public void setHasBoar(boolean hasBoar) {
        this.hasBoar = hasBoar;
    }

    public List<TileSection> getTileSections(){
        return tileSections;
    }
}