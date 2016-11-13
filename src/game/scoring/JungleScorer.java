package game.scoring;

import entities.overlay.Region;

public class JungleScorer extends Scorer {
    @Override
    public int score(Region region) {
        // Does not score until the end
        return 0;
    }

    @Override
    public int scoreAtEnd(Region region) {
        return 0;
    }
}
