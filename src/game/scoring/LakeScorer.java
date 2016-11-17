package game.scoring;

import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.List;

public class LakeScorer extends Scorer {
    @Override
    public int score(Region region) {
//        List<TileSection> tileSections = region.getTileSections();
//        int score = 0;
//        region.setUniquePrey();
//        int multiplier = 1 + region.getUniquePrey();
//
//        for(TileSection tileSection : tileSections){
//            score += 2;
//        }
//        super.returnMeeples(region);
//
//        return score * multiplier;
        return 0;
    }

    @Override
    public int scoreAtEnd(Region region) {
//        List<TileSection> tileSections = region.getTileSections();
//        int score = 0;
//
//        region.setUniquePrey();
//        int multiplier = 1 + region.getUniquePrey();
//
//        for(TileSection ts : tileSections){
//            score += 1;
//        }
//
//        super.returnMeeples(region);
//
//        return score * multiplier;
        return 0;
    }
}
