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

// A region represents a specific area on the board where there is an agglomeration of a specific terrain type
// As tiles are placed on the board, new regions are generated and tilesections are added to specific regions, while
// the tilesections are given a region object (composition)
public class Region {
    private UUID regionId;
    private List<TileSection> tileSections;
    private Terrain terrain;
    private int uniquePrey = 0;
    private int totalPrey = 0;

    public Region(Terrain terrain){
        this.terrain = terrain;
        regionId = UUID.randomUUID();
        tileSections = new ArrayList<>();
    }


    public void addTileSection(TileSection tileSection){
        tileSection.setRegion(this);
        tileSections.add(tileSection);
    }

    public void combineWithRegion(Region region) {
        for (TileSection tileSection : region.tileSections) {
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

    public Scorer getScorer() {
        switch (terrain) {
            case DEN: return new DenScorer();
            case TRAIL: return new TrailScorer();
            case LAKE: return new LakeScorer();
            case JUNGLE: return new LakeScorer();
        }

        return null;
    }

//
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





    public int getUniquePrey() {
        return uniquePrey;
    }

    public int getTotalPrey() {
        return totalPrey;
    }

    public void setUniquePrey() {

        boolean hasBoar, hasBuffalo, hasDeer;

        for(Tile t : tiles){
            if(t.getPreyAnimal() == PreyAnimal.BOAR) {
                uniquePrey++;
                break;
            } else if(t.getPreyAnimal() == PreyAnimal.BUFFALO) {
                uniquePrey++;
                break;
            }

        }
        for(Tile t : tiles){
        }
        for(Tile t : tiles){
            if(t.getPreyAnimal() == ) {
                uniquePrey++;
                break;
            }
        }
    }

    public void setTotalPrey() {
        for(Tile t : tiles){
            if(t.hasBoar() || t.hasDeer() || t.hasBuffalo())
                totalPrey++;
        }
    }
}

