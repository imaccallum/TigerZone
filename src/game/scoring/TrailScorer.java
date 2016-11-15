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

        region.setTotalPrey();
        score += region.getTotalPrey();

        super.returnMeeples(region);

        return score;
    }

    @Override
    public int scoreAtEnd(Region region) {
        List<TileSection> tileSections = region.getTileSections();
        int score = 0;

        for(TileSection ts : tileSections){
            score += 1;
        }

        score += region.getTigerList().size();

        super.returnMeeples(region);

        return score;
    }
}
