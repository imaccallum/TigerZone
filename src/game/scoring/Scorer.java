package game.scoring;

import entities.board.Tiger;
import entities.overlay.Region;

import java.util.List;

public abstract class Scorer {
    protected static int jungleAdjacentDenBonus = 5;
    protected static int jungleAdjacentLakeBonus = 3;
    protected static int completeLakeScorePerTile = 2;
    protected static int incompleteLakeScorePerTile = 1;
    protected static int gameTrailScorePerTile = 1;
    protected Region regionToScore;

    /**
     * Score the region held by the scorer, overridden by relevant subclass for the region
     *
     * @return
     * The score of the region
     */
    public abstract int score();
}
