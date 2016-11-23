package game.scoring;

import entities.board.Terrain;
import entities.board.Tile;
import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JungleScorer extends Scorer {

    /**
     * Construct a jungle scorer
     *
     * @param regionToScore,
     * The jungle region that will eventually be scored
     */
    public JungleScorer(Region regionToScore) {
        this.regionToScore = regionToScore;
    }

    /**
     * Score the jungle region, held by the scorer, counting adjacent lakes and dens and adding their value
     *
     * @return
     * The score of the jungle region
     */
    @Override
    public int score() {
        int score = 0;
        // To avoid repeated adjacent den bonueses or repeated adjacent lake bonuses, keep track of what we've counted
        Set<Region> adjacentLakes = new HashSet<>();
        Set<Tile> denTiles = new HashSet<>();
        for (TileSection tileSection : regionToScore.getTileSections()) {
            // For each tile section find adjacent tile sections
            Tile tile = tileSection.getTile();
            List<TileSection> adjacentTileSections = tile.getAdjacentTileSectionsForTileSection(tileSection);
            for (TileSection adjacentTileSection : adjacentTileSections) {
                // For each adjacent tile section get the region and see if it is a lake, complete, and not already
                // counted
                Region tileSectionRegion = adjacentTileSection.getRegion();
                boolean regionIsLake = tileSectionRegion.getTerrain() == Terrain.LAKE;
                boolean regionIsFinished = tileSectionRegion.isFinished();
                if (regionIsLake && regionIsFinished && !adjacentLakes.contains(tileSectionRegion)) {
                    // Lake meets all criteria, add to score and counted lakes
                    adjacentLakes.add(tileSectionRegion);
                    score += jungleAdjacentLakeBonus;
                }
            }
            if (tile.hasDen() && !denTiles.contains(tile)) {
                // Tile has a den and the tile has not been counted yet, add to score and counted den tiles
                denTiles.add(tile);
                score += jungleAdjacentDenBonus;
            }
        }
        return score;
    }
}
