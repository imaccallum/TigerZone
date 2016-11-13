package entities.overlay;

import entities.board.Node;
import entities.board.Tiger;
import entities.player.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

// A region represents a specific area on the board where there is an agglomeration of a specific terrain type
// As tiles are placed on the board, new regions are generated and tilesections are added to specific regions, while
// the tilesections are given a region object (composition)
public class Region {
    private UUID regionId;
    private List<TileSection> tileSections;
    private List<Tiger> tigers;
    private List<Region> adjacentRegions;

    public Region(){
        regionId = UUID.randomUUID();
        tigers = new ArrayList<>();
        tileSections = new ArrayList<>();
        adjacentRegions = new ArrayList<>();
    }

    public void addTileSection(TileSection tileSection){
        tileSection.setRegion(this);
        tileSections.add(tileSection);
    }

    public boolean containsTileSection(Node section){
        return tileSections.contains(section);
    }

    public int calculatePointValue(){
        if(isFinished()){
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
        for (TileSection tileSection : region.tileSections) {
            if (tileSection.getTiger() != null) {
                this.addTiger(tileSection.getTiger());
            }
            this.addTileSection(tileSection);
        }
        region = null;
    }

    public boolean isFinished() {
        for (TileSection section : tileSections) {
            if (section.hasOpenConnection()) {
                return false;
            }
        }
        return true;
    }

    public boolean isDisputed() {
        if (!tigers.isEmpty()) {
            Player player = tigers.get(0).getOwningPlayer();
            for (Tiger tiger : tigers) {
                if (tiger.getOwningPlayer() != player) {
                    return true;
                }
            }
        }
        return false;
    }
}

