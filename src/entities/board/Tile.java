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
    public String type;
    private boolean hasCrocodile;

    public Tile(String s) {
        edges = new Node[COUNT];
        corners = new Node[COUNT];
        tileSections = new ArrayList<>();
        preyAnimal = null;
        this.type = s;
    }

    // HAS TESTS - Bookkeeping
    public void rotateCounterClockwise(int numberOfRotations) {

        Node[] tempEdges = new Node[COUNT];
        Node[] tempCorners = new Node[COUNT];

        for (int i = 0; i < COUNT; i++) {
            int index = (((i - numberOfRotations) % COUNT) < 0) ?
                    ((i - numberOfRotations) % COUNT) + COUNT : (i - numberOfRotations) % COUNT;
            tempEdges[index] = edges[i];
            tempCorners[index] = corners[i];
        }
        edges = tempEdges;
        corners = tempCorners;
    }

    // HAS TEST - Bookkeeping
    /**
     * Add the given tile sections to the tile
     * 
     * @param tileSections,
     * the comma separated list of tile sections to add.
     */
    public void addTileSections(TileSection... tileSections) {
        for (TileSection tileSection: tileSections){
            tileSection.setTile(this);
        }
        this.tileSections.addAll(Arrays.asList(tileSections));
    }

    /**
     * Create the string representing the tile
     * Will print a lowercase terrain string if a tiger is placed in a location.
     *
     * @return
     * The string representation of the tile.
     */
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

    // Is there a den in the tile
    public boolean hasDen() {
        return hasDen;
    }

    // Set if the tile has a den
    public void setHasDen(boolean hasDen) {
        this.hasDen = hasDen;
    }

    // Get the edge at a given edge location
    public Node getEdge(EdgeLocation location) {
        return edges[location.ordinal()];
    }

    // Set the edge in a given edge location
    public void setEdge(Node node, EdgeLocation location){
        edges[location.ordinal()] = node;
    }

    // Get the coirner in a given corner location
    public Node getCorner(CornerLocation location) {
        return corners[location.ordinal()];
    }

    // Set the corner in a given corner location
    public void setCorner(Node node, CornerLocation location){
        corners[location.ordinal()] = node;
    }

    // Get the prey animal on the tile
    public PreyAnimal getPreyAnimal() {
        return preyAnimal;
    }

    // Set the prey animal on a tile.
    public void setPreyAnimal(PreyAnimal preyAnimal) {
        this.preyAnimal = preyAnimal;
    }

    // Get the location for a tile
    public Point getLocation() {
        return location;
    }

    // Set the location for a tile
    public void setLocation(Point location) {
        this.location = location;
    }

    // Get the lost of tile sections on a tile.
    public List<TileSection> getTileSections() {
        return tileSections;
    }

    // Get whether the tile has a crocodile.
    public boolean isHasCrocodile() {
        return hasCrocodile;
    }

    // Set whether the tile has a crocodile.
    public void setHasCrocodile(boolean hasCrocodile) {
        this.hasCrocodile = hasCrocodile;
    }

    // MARK: Private methods

    //
    // Get the string spacing for given terrain
    //
    // @return
    // The set of spaces for the given terrain
    //

    //
    // Get the string that represents row one
    //
    // @return
    // the string representing row one
    //

}