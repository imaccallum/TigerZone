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
            int newIndex = (i + COUNT - numberOfRotations) % COUNT;
            tempEdges[newIndex] = edges[i];
            tempCorners[newIndex] = corners[i];
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
        String rowOne = rowOneToString();
        String rowTwo = rowTwoToString();
        String rowThree = rowThreeToString();


        return bound + rowOne + rowTwo + rowThree + bound;
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

    //
    // Get the string that represents row one
    //
    // @return
    // the string representing row one
    //
    private String rowOneToString() {
        String rowOne = "|";
        if(corners[0] != null) {
            rowOne += spacing((corners[0].isTigerDisplayNode() ?
                    corners[0].getTileSection().getTerrain().toString().toLowerCase() :
                    corners[0].getTileSection().getTerrain().toString()));
        } else {
            rowOne += spacing("X");
        }

        rowOne += spacing((edges[0].isTigerDisplayNode() ?
                    edges[0].getTileSection().getTerrain().toString().toLowerCase() :
                    edges[0].getTileSection().getTerrain().toString()));

        if(corners[1] != null) {
            rowOne += spacing((corners[1].isTigerDisplayNode() ?
                    corners[1].getTileSection().getTerrain().toString().toLowerCase() :
                    corners[1].getTileSection().getTerrain().toString())) +
                    "|\n";
        } else {
            rowOne += spacing("X") + "|\n";
        }

        return rowOne;
    }

    //
    // Get the string that represents row two
    //
    // @return
    // the string representing row two
    //
    private String rowTwoToString() {
        String rowTwo = "|";
        rowTwo += spacing((edges[3].isTigerDisplayNode() ?
                edges[3].getTileSection().getTerrain().toString().toLowerCase() :
                edges[3].getTileSection().getTerrain().toString()));

        if(hasDen) {
            rowTwo += spacing("True");
        }
        else {
            rowTwo += spacing("False");
        }
        rowTwo += spacing((edges[1].isTigerDisplayNode() ?
                edges[1].getTileSection().getTerrain().toString().toLowerCase() :
                edges[1].getTileSection().getTerrain().toString())) +
                "|\n";
        return rowTwo;
    }

    //
    // Get the string that represents row three
    //
    // @return
    // the string representing row three
    //
    private String rowThreeToString() {
        String rowThree = "|";
        if(corners[3] != null) {
            rowThree +=  spacing((corners[3].isTigerDisplayNode() ?
                    corners[3].getTileSection().getTerrain().toString().toLowerCase() :
                    corners[3].getTileSection().getTerrain().toString()));
        } else {
            rowThree += spacing("X");
        }

        rowThree += spacing((edges[2].isTigerDisplayNode() ?
                edges[2].getTileSection().getTerrain().toString().toLowerCase() :
                edges[2].getTileSection().getTerrain().toString()));

        if(corners[2] != null) {
            rowThree += spacing((corners[2].isTigerDisplayNode() ?
                    corners[2].getTileSection().getTerrain().toString().toLowerCase() :
                    corners[2].getTileSection().getTerrain().toString())) +
                    "|\n";
        } else {
            rowThree += spacing("X") + "|\n";
        }
        return rowThree;
    }
}