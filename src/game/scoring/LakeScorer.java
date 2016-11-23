package game.scoring;

import entities.board.PreyAnimal;
import entities.board.Tile;
import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LakeScorer extends Scorer {
    /**
     * Construct a scorer for a lake
     *
     * @param regionToScore,
     * The lake region to be scored
     */
    public LakeScorer(Region regionToScore) {
        this.regionToScore = regionToScore;
    }

    /**
     * Score the lake by getting all of the unique tiles, checking for prey animals, and accounting for crocodiles
     *
     * @return
     * The score of the lake
     */
    @Override
    public int score() {
        List<TileSection> tileSections = regionToScore.getTileSections();

        Set<Tile> regionTiles = new HashSet<>();
        Set<PreyAnimal> foundPreyAnimals = new HashSet<>();

        // Collect all of the unique tiles in the region
        regionTiles.addAll(tileSections.stream().map(TileSection::getTile).collect(Collectors.toList()));

        int scorePerTile = regionToScore.isFinished() ? completeLakeScorePerTile : incompleteLakeScorePerTile;
        int baseScore = scorePerTile * tileSections.size();

        // Add the prey animals to the found prey animals if there is one there and count the crocodiles
        int numberOfCrocodiles = 0;
        for (Tile tile : regionTiles) {
            if (tile.hasCrocodile()) {
                ++numberOfCrocodiles;
            }
            if (tile.getPreyAnimal() != null) {
                foundPreyAnimals.add(tile.getPreyAnimal());
            }
        }

        super.returnTigers();
        int uniquePreyAnimals = foundPreyAnimals.size() - numberOfCrocodiles;
        int multiplier = uniquePreyAnimals > 0 ? uniquePreyAnimals : 1;
        return baseScore * multiplier;
    }
}
