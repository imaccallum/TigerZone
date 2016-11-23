package game.scoring;

import entities.board.Tile;
import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TrailScorer extends Scorer {
    /**
     * Construct a scorer for a trail
     *
     * @param regionToScore,
     * The trail region to be scored
     */
    public TrailScorer(Region regionToScore) {
        this.regionToScore = regionToScore;
    }
    /**
     * Score the game trail based on the number of tiles and the number of crocodiles and prey animals
     *
     * @return
     * The score of the trail
     */
    @Override
    public int score() {
        List<TileSection> tileSections = regionToScore.getTileSections();
        Set<Tile> regionTiles = new HashSet<>();

        // Collect all of the unique tiles.
        regionTiles.addAll(tileSections.stream().map(TileSection::getTile).collect(Collectors.toList()));

        // For every prey animal, add to score, for every crocodile subtract
        int predationScore = 0;
        for(Tile tile : regionTiles){
            if (tile.getPreyAnimal() != null) {
                predationScore++;
            }
            if (tile.hasCrocodile()) {
                predationScore--;
            }
        }

        int score = (regionTiles.size() * gameTrailScorePerTile) + ((predationScore > 0) ? predationScore : 0);

        super.returnTigers();

        return score;
    }
}
