package entities.overlay;

import entities.board.Node.Node;
import entities.board.Tiger;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

// A region represents a specific area on the board where there is an agglomeration of a specific terrain type
// As tiles are placed on the board, new regions are generated and tilesections are added to specific regions, while
// the tilesections are given a region object (composition)
public class Region {
    private UUID tileId;
    private Node terrainType;
    private List<Node> nodes;
    private List<Tiger> tigers;
    private boolean isFinished = false;
    private boolean isDisputed = false;

    public Region(Node terrainType){
        tileId = UUID.randomUUID();
        this.terrainType = terrainType;
        nodes = new ArrayList<>();
        tigers = new ArrayList<>();
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public boolean containsNode(Node node){
        return nodes.contains(node);
    }

    public int calculatePointValue(){
        if(isFinished){
            //Do calculations
        }
        return 0;
    }

    public void addRegion(Region region){
        // **TODO If two regions are connected, have functionality to combine the two regions in one and update corresponding Nodes
    }

    public List<Region> getAdjacentRegions(){
        //Return the adjacent regions
        return null;
    }

    public UUID getTileId() {
        return tileId;
    }

    public Node getTerrain() {
        return terrainType;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
