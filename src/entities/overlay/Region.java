package entities.overlay;

import entities.board.Node;
import entities.board.Terrain;
import entities.board.Tiger;
import entities.player.Player;
import exceptions.IncompatibleTerrainException;
import game.scoring.JungleScorer;
import game.scoring.LakeScorer;
import game.scoring.Scorer;
import game.scoring.TrailScorer;

import java.util.*;
import java.util.function.Function;
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

    // HAS TESTS - bookkeeping
    /**
     * Add a tile section to the region
     *
     * @param tileSection,
     * The tile section to be added to the Region
     *
     * @throws IncompatibleTerrainException if the tile sectin being added is not compatible with the region.
     */
    public void addTileSection(TileSection tileSection) throws IncompatibleTerrainException {
        if (tileSection.getTerrain() != terrain) {
            throw new IncompatibleTerrainException("Tried adding a TileSection to a Region with different terrain.");
        }
        tileSection.setRegion(this);
        tileSections.add(tileSection);
    }

    // HAS TESTS
    /**
     * Combine one region with another, the first Region will be the one the second region is added to
     *
     * @param region,
     * the region to be merged in
     *
     * @throws IncompatibleTerrainException if the region has incompatible terrain.
     */
    public void combineWithRegion(Region region) throws IncompatibleTerrainException {
        for (TileSection section  : region.tileSections) {
            addTileSection(section);
        }
    }

    // HAS TESTS - Bookkeeping
    /**
     * Get whether the region is finished or not.
     * @return
     * The boolean of whether or not this region is finished.
     */
    public boolean isFinished() {
        if (tileSections.isEmpty()) {
            System.err.println("Queried isFinished for region with no tile sections!!");
        }
        for (TileSection section : tileSections) {
            if (section.hasOpenConnection()) {
                return false;
            }
        }
        return true;
    }

    // HAS TEST - Bookkeeping
    /**
     * Get whether or not the region contains any tigers
     *
     * @return
     * The boolean representing this condition
     */
    public boolean containsTigers() {
        for (TileSection tileSection : tileSections) {
            if (tileSection.getTiger() != null) {
                return true;
            }
        }
        return false;
    }

    // HAS TEST - Bookkeeping
    /**
     * Get the scorere associated with the region, switching the terrain and instantiating the correct scorer.
     *
     * @return
     * The Scorere associated with this type of region.
     */
    public Scorer getScorer() {
        switch (terrain) {
            case TRAIL: return new TrailScorer(this);
            case LAKE: return new LakeScorer(this);
            case JUNGLE: return new JungleScorer(this);
        }

        return null;
    }

    // HAS TEST - Bookkeeping
    /**
     * Get all of the tigers from all of the tile sections in the region
     *
     * @return
     * The list of tigers in the region.
     */
    public List<Tiger> getAllTigers() {
        return tileSections.stream().filter(TileSection::hasTiger)
                .map(TileSection::getTiger).collect(Collectors.toList());
    }

    /**
     * Find pout if there are multiple players with tigers on the tile.
     *
     * @return
     * A boolean representing this condition
     */
    public boolean isDisputed() {
        List<Tiger> tigers = getAllTigers();
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

    // MARK: Getters and Setters

    // Get the region id
    public UUID getRegionId() {
        return regionId;
    }

    // Get the tile sections in the region
    public List<TileSection> getTileSections() {
        return tileSections;
    }

    // Get the terrain of the region.
    public Terrain getTerrain() {
        return terrain;
    }


    public List<Player> getDominantPlayers() {
        List<Player> dominantList = new ArrayList<>();
        HashMap<Player, Integer> tigerCount = new HashMap<>();
        for(Tiger t : getAllTigers()){
            int count = tigerCount.containsKey(t.getOwningPlayer()) ? tigerCount.get(t.getOwningPlayer()) : 0;
            tigerCount.put(t.getOwningPlayer(), count + 1);
        }

        int max = Collections.max(tigerCount.values());

        for(Player p : tigerCount.keySet()){
            if(tigerCount.get(p) == max)
                dominantList.add(p);
        }

        return dominantList;
    }

}

