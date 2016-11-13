package game.scoring;

import entities.board.Terrain;
import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.List;

public class JungleScorer extends Scorer {
    @Override
    public int score(Region region) {
        return 0;
    }

    @Override
    public int scoreAtEnd(Region region) {
        List<Region> regions = region.getAdjacentRegions();

        int score = 0;
        for(Region r : regions){
            if(r.getTerrain() == Terrain.LAKE && r.isFinished())
                score += 3;
        }

        return score;
    }
}
