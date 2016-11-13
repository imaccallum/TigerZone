package game.scoring;

import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.List;

public class TrailScorer extends Scorer {
    @Override
    public int score(Region region) {
        List<TileSection> tileSections = region.getTileSections();
        int score = 0;

        for(TileSection ts : tileSections){
            score += 1;
        }

        return score;
    }

    @Override
    public int scoreAtEnd(Region region) {
        return score(region);
    }
}
