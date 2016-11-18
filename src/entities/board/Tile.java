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
    private boolean hasDen;
    private PreyAnimal preyAnimal;
    private Point location;
    private List<TileSection> tileSections;

    public Tile() {
        edges = new Node[COUNT];
        corners = new Node[COUNT];
        tileSections = new ArrayList<>();
        preyAnimal = null;
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

    public void addTileSections(TileSection... sections) {
        for (TileSection tileSection: sections){
            tileSection.setTile(this);
        }
        tileSections.addAll(Arrays.asList(sections));
    }

    public String toString(){
        return "Tile: " + this.hashCode() + "\n" + "TileSections: " + tileSections;
    }


    // Getters and Setters
    public boolean hasDen() {
        return hasDen;
    }

    public void setHasDen(boolean hasDen) {
        this.hasDen = hasDen;
    }

    public Node getEdge(EdgeLocation location) {
        return edges[location.ordinal()];
    }

    public void setEdge(Node node, EdgeLocation location){
        edges[location.ordinal()] = node;
    }

    public Node getCorner(CornerLocation location) {
        return corners[location.ordinal()];
    }

    public void setCorner(Node node, CornerLocation location){
        corners[location.ordinal()] = node;
    }

    public PreyAnimal getPreyAnimal() {
        return preyAnimal;
    }

    public void setPreyAnimal(PreyAnimal preyAnimal) {
        this.preyAnimal = preyAnimal;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public List<TileSection> getTileSections() {
        return tileSections;
    }
}