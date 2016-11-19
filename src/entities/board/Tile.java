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
    public char type;

    public Tile(char t) {
        edges = new Node[COUNT];
        corners = new Node[COUNT];
        tileSections = new ArrayList<>();
        preyAnimal = null;
        this.type = t;
    }

    // HAS TESTS
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

    // HAS TEST
    public void addTileSections(TileSection... sections) {
        for (TileSection tileSection: sections){
            tileSection.setTile(this);
        }
        tileSections.addAll(Arrays.asList(sections));
    }

    //			    Edge[0]
    //		Corner[0]	Corner[1]
    //	Edge[3]		Center		Edge[1]
    //		Corner[3]	Corner[2]
    //			    Edge[2]
    public String toString(){

        String bound = "|" + type + "----------------------------" + type + "|\n";
        String one = "|";
        String two = "|";
        String three = "|";


        if(corners[0] != null) {
            one +=  spacing(corners[0].getTileSection().getTerrain().toString()) +  spacing(edges[0].getTileSection().getTerrain().toString());
        } else {
            one += spacing("X") + spacing(edges[0].getTileSection().getTerrain().toString());
        }
        if(corners[1] != null) {
           one += spacing(corners[1].getTileSection().getTerrain().toString()) + "|\n";
        } else {
            one += spacing("X") + "|\n";
        }

        if(hasDen)
            two += spacing(edges[3].getTileSection().getTerrain().toString()) + spacing("True") + spacing(edges[1].getTileSection().getTerrain().toString()) + "|\n";
        else
            two += spacing(edges[3].getTileSection().getTerrain().toString())+ spacing("False") + spacing(edges[1].getTileSection().getTerrain().toString()) + "|\n";


        if(corners[2] != null) {
            three +=  spacing(corners[2].getTileSection().getTerrain().toString()) +  spacing(edges[2].getTileSection().getTerrain().toString());
        } else {
            three += spacing("X") + spacing(edges[2].getTileSection().getTerrain().toString());
        }
        if(corners[3] != null) {
            three += spacing(corners[3].getTileSection().getTerrain().toString()) + "|\n";
        } else {
            three += spacing("X") +"|\n";
        }

    //    System.out.print(one.length() + " " + two.length() + " " + three.length() + "\n");

        return bound + one + two + three + bound;
    }

    private String spacing(String terrain) {
        String out = "";
        int size = 10 - terrain.length();
        for(int i = 0; i < 2; i++) {
            for (int x = 0; x < size / 2; x++) {
                out += " ";
            }
            if(i == 0) out += terrain;
        }
        if(out.length() == 9)
            out += " ";

        return out;
    }

    // MARK: Getters and Setters

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