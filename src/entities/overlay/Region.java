package entities.overlay;

import entities.board.PreyAnimal;
import entities.board.Terrain;
import entities.board.Tiger;
import entities.board.Tile;
import entities.player.Player;
import game.scoring.DenScorer;
import game.scoring.LakeScorer;
import game.scoring.Scorer;
import game.scoring.TrailScorer;

import java.util.*;
import java.util.stream.Collectors;

// A region represents a specific area on the board where there is an agglomeration of a specific terrain type
// As tiles are placed on the board, new regions are generated and tilesections are added to specific regions, while
// the tilesections are given a region object (composition)
public class Region {
    private UUID regionId;
    private List<TileSection> tileSections;
    private Terrain terrain;

    public Region(Terrain terrain) {
        this.terrain = terrain;
        regionId = UUID.randomUUID();
        tileSections = new ArrayList<>();
    }


    public void addTileSection(TileSection tileSection) {
        tileSection.setRegion(this);
        tileSections.add(tileSection);
    }

    public void combineWithRegion(Region region) {
        region.tileSections.forEach(this::addTileSection);
    }

    public boolean isFinished() {
        for (TileSection section : tileSections) {
            if (section.hasOpenConnection()) {
                return false;
            }
        }
        return true;
    }

    public boolean containsTigers() {
        for (TileSection section : tileSections) {
            if (section.getTiger() != null) {
                return true;
            }
        }
        return false;
    }

    public Scorer getScorer() {
        switch (terrain) {
            case TRAIL: return new TrailScorer();
            case LAKE: return new LakeScorer();
            case JUNGLE: return new LakeScorer();
        }

        return null;
    }

    // MARK: Getters and Setters

    public UUID getRegionId() {
        return regionId;
    }

    public List<TileSection> getTileSections(){
        return tileSections;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public List<Tiger> getAllTigers() {
        List<Tiger> tigers = new ArrayList<>();
        tigers.addAll(tileSections.stream().filter(section -> section.getTiger() != null)
                .map(TileSection::getTiger).collect(Collectors.toList()));
        return tigers;
    }

    // MARK: NEED TO IMPLEMENT ELSEWHERE TODO

    //    public boolean isDisputed() {
//        if (!tigers.isEmpty()) {
//            Player player = tigers.get(0).getOwningPlayer();
//            for (Tiger tiger : tigers) {
//                if (tiger.getOwningPlayer() != player) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public List<Player> getDominantPlayers() {
//        List<Player> dominantList = new ArrayList<>();
//        HashMap<Player, Integer> tigerCount = new HashMap<>();
//        for(Tiger t : tigers){
//            int count = tigerCount.containsKey(t.getOwningPlayer()) ? tigerCount.get(t.getOwningPlayer()) : 0;
//            tigerCount.put(t.getOwningPlayer(), count + 1);
//        }
//
//        int max = Collections.max(tigerCount.values());
//
//        for(Player p : tigerCount.keySet()){
//            if(tigerCount.get(p) == max)
//                dominantList.add(p);
//        }
//
//        return dominantList;
//    }

}

