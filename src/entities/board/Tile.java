package entities.board;

import entities.overlay.TileSection;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tile {

    //			    Edge[0]
    //		Corner[0]	Corner[1]
    //	Edge[3]		Center		Edge[1]
    //		Corner[3]	Corner[2]
    //			    Edge[2]
    private final int COUNT = 4; // Count of orientations
    private Node[] edges;
    private Node[] corners;
    private Node center;
    private PreyAnimal preyAnimal;
    private Point location;
    private List<TileSection> tileSections;

    public Tile() {
        edges = new Node[COUNT];
        corners = new Node[COUNT];
        tileSections = new ArrayList<>();
        preyAnimal = null;
    }

    public Node getCorner(CornerLocation location) {
        return corners[location.ordinal()];
    }

    public Node getEdge(EdgeLocation location) {
        return edges[location.ordinal()];
    }

    public Node getCenter() {
        return center;
    }

    public List<TileSection> getTileSections(){
        return tileSections;
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

    public void setEdge(Node node, int i){
        edges[i] = node;
    }
    
    public void setCorner(Node node, int i){
        corners[i] = node;
    }

    public void setCenter(Node center) {
        this.center = center;
    }

    public void addTileSections(TileSection... sections) {
        for (TileSection tileSection: sections){
            tileSection.setTile(this);
        }
        tileSections.addAll(Arrays.asList(sections));
    }

    public Node[] getEdges(){
        return edges;
    }

    public boolean hasDeer() {
        return preyAnimal == PreyAnimal.DEER;
    }

    public boolean hasBuffalo() {
        return preyAnimal == PreyAnimal.BUFFALO;
    }

    public boolean hasBoar() {
        return preyAnimal == PreyAnimal.BOAR;
    }

    public void setPreyAnimal(PreyAnimal preyAnimal) {
        this.preyAnimal = preyAnimal;
    }

    public String toString(){
        return "Tile: " + this.hashCode() + "\n" + "TileSections: " + tileSections;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }
}