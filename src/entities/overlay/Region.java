package entities.overlay;

import entities.board.Terrain;
import entities.board.Tiger;
import entities.player.Player;
import game.scoring.DenScorer;
import game.scoring.LakeScorer;
import game.scoring.Scorer;
import game.scoring.TrailScorer;

import java.util.*;

// A region represents a specific area on the board where there is an agglomeration of a specific terrain type
// As tiles are placed on the board, new regions are generated and tilesections are added to specific regions, while
// the tilesections are given a region object (composition)
public class Region {
    private UUID regionId;
    private List<TileSection> tileSections;
    private List<Tiger> tigers;
    private List<Region> adjacentRegions;
    private Terrain terrain;

    public Region(Terrain terrain){
        regionId = UUID.randomUUID();
        tigers = new ArrayList<>();
        tileSections = new ArrayList<>();
        adjacentRegions = new ArrayList<>();
        this.terrain = terrain;
    }

    public void addTileSection(TileSection tileSection){
        tileSection.setRegion(this);
        tileSections.add(tileSection);
    }

    public boolean containsTileSection(TileSection section){
        return tileSections.contains(section);
    }

    public List<Region> getAdjacentRegions(){
        //Return the adjacent regions
        return null;
    }

    public List<TileSection> getTileSections(){
        return tileSections;
    }

    public UUID getRegionId() {
        return regionId;
    }

    public void addTiger(Tiger t){
        tigers.add(t);
    }

    public List<Tiger> getTigerList(){
        return tigers;
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

    public Player getDominantPlayer() {
        return null;  // TODO create function
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public Scorer getScorer() {
        switch (terrain) {
            case DEN: return new DenScorer();
            case TRAIL: return new TrailScorer();
            case LAKE: return new LakeScorer();
            case JUNGLE: return new LakeScorer();
        }

        return null;
    }
}

