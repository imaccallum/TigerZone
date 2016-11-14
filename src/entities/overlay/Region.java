package entities.overlay;

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
    private HashSet<Tile> tiles;
    private List<Tiger> tigers;
    private List<Region> adjacentRegions;
    private Terrain terrain;
    private int uniquePrey = 0;
    private int totalPrey = 0;

    public Region(Terrain terrain){
        regionId = UUID.randomUUID();
        tigers = new ArrayList<>();
        tileSections = new ArrayList<>();
        adjacentRegions = new ArrayList<>();
        this.terrain = terrain;
        tiles = new HashSet<>();
    }

    public void addTileSection(TileSection tileSection){
        tileSection.setRegion(this);
        tileSections.add(tileSection);
        tiles.add(tileSection.getTile());
    }

    public boolean containsTileSection(TileSection section){
        return tileSections.contains(section);
    }

    public List<Region> getAdjacentRegions(){
        return adjacentRegions;
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

    public boolean hasTigers() {
        return !tigers.isEmpty();
    }

    public List<Player> getDominantPlayers() {
        List<Player> dominantList = new ArrayList<>();
        HashMap<Player, Integer> tigerCount = new HashMap<>();
        for(Tiger t : tigers){
            int count = tigerCount.containsKey(t.getOwningPlayer()) ? tigerCount.get(t.getOwningPlayer()) : 0;
            tigerCount.put(t.getOwningPlayer(), count);
        }

        int max = Collections.max(tigerCount.values());

        for(Player p : tigerCount.keySet()){
            if(tigerCount.get(p) == max)
                dominantList.add(p);
        }

        return dominantList;
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

    public int getUniquePrey() {
        return uniquePrey;
    }

    public int getTotalPrey() {
        return totalPrey;
    }

    public void setUniquePrey() {
        for(Tile t : tiles){
            if(t.hasBoar()) {
                uniquePrey++;
                break;
            }
        }
        for(Tile t : tiles){
            if(t.hasBuffalo()) {
                uniquePrey++;
                break;
            }
        }
        for(Tile t : tiles){
            if(t.hasDeer()) {
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

