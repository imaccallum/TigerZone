package entities.overlay;

import entities.board.*;
// Importing specific classes is giving me compile errors. We may need to fix that in the future if
// other people are having the same issue. -nikhil
//import entities.board.Follower;
//import entities.board.TerrainType;
//import entities.board.TileSection;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

// A region represents a specific area on the board where there is an agglomeration of a specific terrain type
// As tiles are placed on the board, new regions are generated and tilesections are added to specific regions, while
// the tilesections are given a region object (composition)
public class Region {
    private UUID tileId;
    private TerrainType terrain;
    private List<TileSection> sections;
    private List<Follower> followers;
    private boolean isFinished = false;

    public Region(TerrainType terrain){
        tileId = UUID.randomUUID();
        this.terrain = terrain;
        sections = new ArrayList<>();
        followers = new ArrayList<>();
    }

    public void addSection(TileSection section){
        sections.add(section);
    }

    public boolean containsTileSection(TileSection section){
        return sections.contains(section);
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
