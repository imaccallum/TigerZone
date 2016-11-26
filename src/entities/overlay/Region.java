package entities.overlay;

import entities.board.Node;
import entities.board.Terrain;
import entities.board.Tiger;
import entities.player.Player;
import exceptions.IncompatibleTerrainException;
import game.messaging.info.RegionInfo;
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
    private boolean hasCrocodile;

    public Region(Terrain terrain) {
        this.terrain = terrain;
        regionId = UUID.randomUUID();
        tileSections = new ArrayList<>();
        hasCrocodile = false;
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
            String playerName = tigers.get(0).getOwningPlayerName();
            for (Tiger tiger : tigers) {
                if (!tiger.getOwningPlayerName().equals(playerName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the list of players that are dominant, ie have the same number of tigers and have the max number of tigers
     * in the region
     *
     * @return
     * The list of dominant players
     */
    public List<String> getDominantPlayerNames() {
        List<String> dominantList = new ArrayList<>();
        HashMap<String, Integer> tigerCount = new HashMap<>();
        for(Tiger tiger : getAllTigers()){
            int count = 0;
            if (tigerCount.containsKey(tiger.getOwningPlayerName())) {
                count = tigerCount.get(tiger.getOwningPlayerName());
            }
            tigerCount.put(tiger.getOwningPlayerName(), count + 1);
        }

        int max = Collections.max(tigerCount.values());

        for(String playerName : tigerCount.keySet()){
            if(tigerCount.get(playerName) == max)
                dominantList.add(playerName);
        }

        return dominantList;
    }

    /**
     * Get the immutable object representing the region info for the AI to make decisions with
     *
     * @return
     * The RegionInfo object representing this region.
     */
    public RegionInfo getRegionInfo() {
        int projectedScore = getScorer().scoreIfCompletedNow();
        int countUnconnectedNodes = 0;
        for (TileSection tileSection : tileSections) {
            for (Node node : tileSection.getNodes()) {
                if (!node.isConnected()) {
                    ++countUnconnectedNodes;
                }
            }
        }
        return new RegionInfo(regionId, countUnconnectedNodes, projectedScore);
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

    // Get whether the region has a crocodile
    public boolean hasCrocodile() {
        return hasCrocodile;
    }

    // Set if the region has a crocodile
    public void setHasCrocodile(boolean hasCrocodile) {
        this.hasCrocodile = hasCrocodile;
    }
}

