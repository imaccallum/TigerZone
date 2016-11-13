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
    private UUID regionId;
    private List<Node> nodes;
    private List<Tiger> tigers;
    private List<Region> adjacentRegions;
    private boolean isFinished = false;
    private boolean isDisputed = false;

    public Region(){
        regionId = UUID.randomUUID();
        tigers = new ArrayList<>();
        nodes = new ArrayList<>();
        adjacentRegions = new ArrayList<>();
    }

    public void addNode(Node node){
        node.setRegion(this);
        nodes.add(node);
    }

    public boolean containsTileSection(Node section){
        return nodes.contains(section);
    }

    public int calculatePointValue(){
        if(isFinished){
            //Do calculations
        }
        return 0;
    }

    public List<Region> getAdjacentRegions(){
        //Return the adjacent regions
        return null;
    }

    public UUID getRegionId() {
        return regionId;
    }

    public void addTiger(Tiger t){
        tigers.add(t);
    }

    public void combineWithRegion(Region region) {
        for (Node node : region.nodes) {
            if (node.getTiger() != null) {
                this.addTiger(node.getTiger());
            }
            this.addNode(node);
        }
        region = null;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isDisputed() {
        return isDisputed;
    }

    public void setDisputed(boolean disputed) {
        isDisputed = disputed;
    }
}

