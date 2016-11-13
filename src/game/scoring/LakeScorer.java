package game.scoring;

import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.List;

public class LakeScorer extends Scorer {
    @Override
    public int score(Region region) {
        List<TileSection> tileSections = region.getTileSections();
        int score = 0;

        for(TileSection ts : tileSections){
            if(ts.getTile().hasPenant())
                score += 4;
            else
                score += 2;
        }

        return score;
    }

    @Override
    public int scoreAtEnd(Region region) {
        List<TileSection> tileSections = region.getTileSections();
        int score = 0;

        for(TileSection ts : tileSections){
            if(ts.getTile().hasPenant())
                score += 2;
            else
                score += 1;
        }

        return score;
    }
}
