package entities.overlay;

import entities.board.Node.Node;
import entities.board.TerrainType;
import entities.board.Tiger;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

// A region represents a specific area on the board where there is an agglomeration of a specific terrain type
// As tiles are placed on the board, new regions are generated and tilesections are added to specific regions, while
// the tilesections are given a region object (composition)
public class Region {
    private UUID tileId;
    private TerrainType terrain;
    private List<Node> nodes;
    private List<Tiger> tigers;
    private boolean isFinished = false;
    private boolean isDisputed = false;

    public Region(TerrainType terrain){
        tileId = UUID.randomUUID();
        this.terrain = terrain;
        nodes = new ArrayList<>();
        tigers = new ArrayList<>();
    }

    public void addSection(Node section){
        nodes.add(section);
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

    public TerrainType getTerrain() {
        return terrain;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
