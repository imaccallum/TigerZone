package entities.board;

import entities.overlay.TigerDen;
import entities.overlay.TileSection;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Tile {

    //			    Edge[0]
    //		Corner[0]	Corner[1]
    //	Edge[3]		    		Edge[1]
    //		Corner[3]	Corner[2]
    //			    Edge[2]

    private final int COUNT = 4; // Count of orientations
    private Node[] edges;
    private Node[] corners;
    private TigerDen den;
    private PreyAnimal preyAnimal;
    private Point location;
    private List<TileSection> tileSections;
    private String type;
    private boolean hasCrocodile;
    private int orientation;
    private Point serverLocation;

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
            setOrientation(orientation + numberOfRotations);
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
    public String toString() {
        String bound = "|" + type + "--------------------" + type + "|\n";
        String rowOne = rowOneToString();
        String rowTwo = rowTwoToString();
        String rowThree = rowThreeToString();

        if (location != null) {
            String locHeader = "|" + spacing("x: " + location.x) + spacing(" ") + spacing("y: " + location.y) + "|\n";
            return bound + locHeader + rowOne + rowTwo + rowThree + bound;
        }
        else {
            return bound + rowOne + rowTwo + rowThree + bound;
        }
    }

    /**
     * Gets the adjacent tile sections to the current tile section
     *
     * @param tileSection,
     * the tile section we want to find adjacent tile sections to
     *
     * @return
     * The list of tile sections that are adjacent to this tile section, returns empty list if there are none.
     */
    public List<TileSection> getAdjacentTileSectionsForTileSection(TileSection tileSection) {
        if (!tileSections.contains(tileSection)) {
            // The tile section is not on this tile, return empty list
            return new ArrayList<>();
        }

        List<Node> tileSectionNodes = tileSection.getNodes();
        List<Node> tileNodes = nodesClockwise();
        List<Integer> clockwiseNodeLocations = new ArrayList<>();
        for (int i = 0; i < tileNodes.size(); ++i) {
            if (tileNodes.get(i) != null) {
                // Null nodes aren't important for adjacency.
                if (tileSectionNodes.contains(tileNodes.get(i))) {
                    // Use positive numbers to indicate nodes that are part of the tile section
                    clockwiseNodeLocations.add(i);
                } else if (tileNodes.get(i) != null) {
                    // Use negative numbers to represent others
                    clockwiseNodeLocations.add(-i);
                }
            }
        }

        Set<Node> adjacentNodes = new HashSet<>();
        for (int i = 1; i < clockwiseNodeLocations.size(); ++i) {
            int current = clockwiseNodeLocations.get(i);
            int prev = clockwiseNodeLocations.get(i - 1);
            Node adjacentNode = null;
            if (current < 0 && prev > 0) {
                // The previous node is in the tile section, so the current is adjacent if it is not in it
                adjacentNode = tileNodes.get(current * -1);
            }
            else if (current > 0 && prev < 0) {
                // The current node is in the tile section, so the previous is adjacent if it is not in it
                adjacentNode = tileNodes.get(prev * -1);
            }

            // Check for wrap around adjacency
            if (i == clockwiseNodeLocations.size() - 1) {
                if (clockwiseNodeLocations.get(0) > 0 && current < 0) {
                    // First node is in the tile section and last node is not, last node adjacent
                    adjacentNode = tileNodes.get(current * -1);
                }
                else if (clockwiseNodeLocations.get(0) < 0 && current > 0) {
                    // Last node is in the tile section and first node is not, first node adjacent
                    adjacentNode = tileNodes.get(clockwiseNodeLocations.get(0) * -1);
                }
            }

            if (adjacentNode != null && !adjacentNodes.contains(adjacentNode)) {
                adjacentNodes.add(adjacentNode);
            }
        }

        // Create a list to return
        List<TileSection> adjacentTileSections = new ArrayList<>();
        for (Node adjacentNode : adjacentNodes) {
            // For each adjacent node, add its tile section if the section is not in the array
            TileSection adjacentTileSection = adjacentNode.getTileSection();
            if (!adjacentTileSections.contains(adjacentTileSection)) {
                adjacentTileSections.add(adjacentTileSection);
            }
        }

        return adjacentTileSections;
    }

    /**
     * Gets the nodes on the tile in a clockwise position, starting with the top left corner and going to the left edge.
     *
     * @return
     * The list of nodes in the clockwise direction around the tile.
     */
    public List<Node> nodesClockwise() {
        // Start at top left corner and end at left edge
        List<Node> nodesClockwise = new ArrayList<>();
        nodesClockwise.add(corners[0]);
        nodesClockwise.add(edges[0]);
        nodesClockwise.add(corners[1]);
        nodesClockwise.add(edges[1]);
        nodesClockwise.add(corners[2]);
        nodesClockwise.add(edges[2]);
        nodesClockwise.add(corners[3]);
        nodesClockwise.add(edges[3]);
        return nodesClockwise;
    }

    /**
     * Gets the nodes on the tile in the pattern Dave wants for Nodes on a Tile
     *
     * @return
     * The list of nodes in the pattern Dave wants
     */
    public List<Node> nodesByRow(){
        List<Node> nodesByRow = new ArrayList<>();
        nodesByRow.add(corners[0]);
        nodesByRow.add(edges[0]);
        nodesByRow.add(corners[1]);
        nodesByRow.add(edges[3]);
        nodesByRow.add(null);   //can't add the TigerDen
        nodesByRow.add(edges[1]);
        nodesByRow.add(corners[3]);
        nodesByRow.add(edges[2]);
        nodesByRow.add(corners[2]);
        return nodesByRow;
    }

    /**
     * Get whether a crocodile can be placed on a given tile
     *
     * @return
     * The boolean state
     */
    public boolean canPlaceCrocodile() {
        boolean hasLake = hasTerrain(Terrain.LAKE);
        boolean hasJungle = hasTerrain(Terrain.JUNGLE);
        boolean hasTrail = hasTerrain(Terrain.TRAIL);

        if (hasJungle && (hasTrail || hasLake)) {
            boolean canPlace = true;
            // Can place if neither the trails or the lakes have crocs
            for (TileSection tileSection : tileSections) {
                if (tileSection.getTerrain()  == Terrain.TRAIL) {
                    canPlace = canPlace && !tileSection.getRegion().hasCrocodile();
                }
                else if (tileSection.getTerrain() == Terrain.LAKE) {
                    canPlace = canPlace && !tileSection.getRegion().hasCrocodile();
                }
            }
            return canPlace;
        }
        else {
            // The entire tile is lake, cannot place
            return false;
        }
    }

    /**
     * Place a crocodile on the tile, updates all lake and trail regions to have crocodiles
     */
    public void placeCrocodile() {
        hasCrocodile = true;
        for (TileSection tileSection : tileSections) {
            if (tileSection.getTerrain() == Terrain.TRAIL) {
                tileSection.getRegion().setHasCrocodile(true);
            }
            else if (tileSection.getTerrain() == Terrain.LAKE) {
                tileSection.getRegion().setHasCrocodile(true);
            }
        }
    }

    /**
     * Get the tile section that the server zone is talking about
     *
     * @param zone,
     * The integer representing the zone
     *
     * @return
     * The TileSection
     */
    public TileSection tileSectionForZone(int zone) {
        switch (zone) {
            case 1: {
                Node corner = getCorner(CornerLocation.TOP_LEFT);
                if (corner == null) {
                    if (getEdge(EdgeLocation.LEFT).getTileSection()
                            .equals(getEdge(EdgeLocation.TOP).getTileSection())) {
                        return getEdge(EdgeLocation.LEFT).getTileSection();
                    }
                    else if (getEdge(EdgeLocation.LEFT).getTileSection().getTerrain() == Terrain.JUNGLE) {
                        return getEdge(EdgeLocation.LEFT).getTileSection();
                    }
                    else {
                        return getEdge(EdgeLocation.TOP).getTileSection();
                    }
                }
                else {
                    return corner.getTileSection();
                }
            }
            case 2: return getEdge(EdgeLocation.TOP).getTileSection();
            case 3: {
                Node corner = getCorner(CornerLocation.TOP_RIGHT);
                if (corner == null) {
                    if (getEdge(EdgeLocation.RIGHT).getTileSection()
                            .equals(getEdge(EdgeLocation.TOP).getTileSection())) {
                        return getEdge(EdgeLocation.RIGHT).getTileSection();
                    }
                    else if (getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain() == Terrain.JUNGLE) {
                        return getEdge(EdgeLocation.RIGHT).getTileSection();
                    }
                    else {
                        return getEdge(EdgeLocation.TOP).getTileSection();
                    }
                }
                else {
                    return corner.getTileSection();
                }
            }
            case 4: return getEdge(EdgeLocation.LEFT).getTileSection();
            case 5: return null;
            case 6: return getEdge(EdgeLocation.RIGHT).getTileSection();
            case 7: {
                Node corner = getCorner(CornerLocation.BOTTOM_LEFT);
                if (corner == null) {
                    if (getEdge(EdgeLocation.LEFT).getTileSection()
                            .equals(getEdge(EdgeLocation.BOTTOM).getTileSection())) {
                        return getEdge(EdgeLocation.LEFT).getTileSection();
                    }
                    else if (getEdge(EdgeLocation.LEFT).getTileSection().getTerrain() == Terrain.JUNGLE) {
                        return getEdge(EdgeLocation.LEFT).getTileSection();
                    }
                    else {
                        return getEdge(EdgeLocation.BOTTOM).getTileSection();
                    }
                }
                else {
                    return corner.getTileSection();
                }
            }
            case 8: return getEdge(EdgeLocation.BOTTOM).getTileSection();
            case 9: {
                Node corner = getCorner(CornerLocation.BOTTOM_RIGHT);
                if (corner == null) {
                    if (getEdge(EdgeLocation.RIGHT).getTileSection()
                            .equals(getEdge(EdgeLocation.BOTTOM).getTileSection())) {
                        return getEdge(EdgeLocation.RIGHT).getTileSection();
                    }
                    else if (getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain() == Terrain.JUNGLE) {
                        return getEdge(EdgeLocation.RIGHT).getTileSection();
                    }
                    else {
                        return getEdge(EdgeLocation.BOTTOM).getTileSection();
                    }
                }
                else {
                    return corner.getTileSection();
                }
            }
            default: return null;
        }
    }

    /*
     * Get whether a tiger is on any of the TileSections of the Tile
     *
     * @return
     * boolean that shows if there's a tiger on the tile
     */
    public boolean hasTiger(){
        for(TileSection tilesection: tileSections){
            if(tilesection.hasTiger()){
                return true;
            }
        }
        return false;
    }

    /*
     * Get the int for the Node where the Tiger should be placed on in the tileSection on the tile
     *
     * @return
     * int for the Node that should have the Tiger placed on it
     */
    public int getTigerZone(TileSection tilesection){
        int min = 9;
        for(Node nodeInTileSection: tilesection.getNodes()){
            if(nodeInTileSection.equals(corners[0])){
                return 1;
            }
            if(nodeInTileSection.equals(edges[0]) && min > 2){
                if(tilesection.getTerrain() == Terrain.LAKE && tilesection.getNodes().size()>=2 &&
                        edges[3].getTileSection().getTerrain() == Terrain.LAKE){
                    return 1;
                }
                else if(corners[0] == null && edges[0].getTileSection().getTerrain() != Terrain.LAKE &&
                    (edges[0].getTileSection().getTerrain().equals(edges[3].getTileSection().getTerrain()) ||
                        (edges[0].getTileSection().getTerrain() == Terrain.JUNGLE ||
                                edges[3].getTileSection().getTerrain()==Terrain.JUNGLE))) {
                    return 1;
                }
                else{
                    min = 2;
                }
            }
            else if(nodeInTileSection.equals(corners[1]) && min > 3){
                if(corners[0] == null && !(edges[0].getTileSection().getTerrain() == Terrain.TRAIL ||
                        (edges[0].getTileSection().getNodes().size()>=2 && edges[0].getTileSection().getTerrain() == Terrain.LAKE))){
                    return 1;
                }
                else {
                    min = 3;
                }
            }
            else if(nodeInTileSection.equals(edges[3]) && min > 4){
                if(tilesection.getTerrain() == Terrain.JUNGLE){
                    min = 1;
                }
                else {
                    min = 4;
                }
            }
            else if(nodeInTileSection.equals(edges[1]) && min > 6){
                if(tilesection.getTerrain().equals(Terrain.LAKE) && tilesection.getNodes().size()==1){
                    min = 6;
                }
                else if(corners[1] == null && edges[0].getTileSection().getTerrain()
                        .equals(edges[1].getTileSection().getTerrain())) {
                    min = 3;
                }
                else if(tilesection.getTerrain() == Terrain.JUNGLE){
                    min = 3;
                }
                else if(den == null && edges[1].getTileSection().getTerrain() == Terrain.TRAIL &&
                        edges[2].getTileSection().getTerrain()==Terrain.TRAIL &&
                        edges[3].getTileSection().getTerrain()!=Terrain.TRAIL){
                    min = 5;
                }
                else{
                    min = 6;
                }
            }
            else if(nodeInTileSection.equals(corners[3]) && min > 7){
                min = 7;
            }
            else if(nodeInTileSection.equals(edges[2]) && min > 8){
                if(tilesection.getTerrain().equals(Terrain.LAKE) && tilesection.getNodes().size()>1){
                    min = 8;
                }
                else if(corners[3] == null && edges[2].getTileSection().getTerrain() != Terrain.LAKE &&
                        (edges[2].getTileSection().getTerrain().equals(edges[3].getTileSection().getTerrain()) ||
                                (edges[2].getTileSection().getTerrain() == Terrain.JUNGLE ||
                                        edges[3].getTileSection().getTerrain()==Terrain.JUNGLE))) {
                    min = 7;
                }
                else{
                    min = 8;
                }
            }
        }
        return min;
    }


    // MARK: Getters and Setters

    // Is there a den in the tile
    public TigerDen getDen() {
        return den;
    }

    // Set if the tile has a den
    public void setDen(TigerDen den) {
        this.den = den;
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

    // Get the location for the tile in terms of the Server start tile
    public Point getServerLocation() {
        return serverLocation;
    }

    // Set the location for a tile
    public void setLocation(Point location, Point boardcenter) {
        this.location = location;
        if(location == null){
            serverLocation = null;
        }
        else {
            this.serverLocation = new Point(location.x - boardcenter.x, location.y - boardcenter.y);
        }
    }

    // Get the lost of tile sections on a tile.
    public List<TileSection> getTileSections() {
        return tileSections;
    }

    // Get whether the tile has a crocodile.
    public boolean hasCrocodile() {
        return hasCrocodile;
    }

    // Set the type
    public void setType(String type) {
        this.type = type;
    }

    // Get the type
    public String getType() {
        return type;
    }

    public int getOrientation(){
        return orientation;
    }

    private void setOrientation(int orientation){
        this.orientation = orientation % COUNT;
    }

    // MARK: Private methods

    //
    // Get the string spacing for given terrain
    //
    // @return
    // The set of spaces for the given terrain
    //
    private String spacing(String input) {
        String out = "";
        int size = 10 - input.length();
        for(int i = 0; i < 2; i++) {
            for (int x = 0; x < size / 2; x++) {
                out += " ";
            }
            if(i == 0) out += input;
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

        if(den != null) {
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

    //
    // Get if the tile has a particular terrain on it
    //
    // @param terrain,
    // The terrain type we are looking for
    //
    // @return
    // The boolean representing this state
    //
    private boolean hasTerrain(Terrain terrain) {
        for (TileSection tileSection : tileSections) {
            if (tileSection.getTerrain() == terrain) {
                return true;
            }
        }
        return false;
    }
}