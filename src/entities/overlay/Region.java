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
    private List<Node> nodes;
    private List<Tiger> tigers;
    private boolean isFinished = false;
    private boolean isDisputed = false;

    public Region(){
        tileId = UUID.randomUUID();
        nodes = new ArrayList<>();
        tigers = new ArrayList<>();
    }

    public void addNode(Node n){
        n.region = this;
        nodes.add(n);
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

    public UUID getTileId() {
        return tileId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void addTiger(Tiger t){
        tigers.add(t);
    }

    public void combineWithRegion(Region r) {
        for (Node n : r.nodes) {
            if (n.tiger != null) {
                this.addTiger(n.tiger);
            }
            this.addNode(n);
        }
    }
}

