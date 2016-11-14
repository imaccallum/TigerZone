package game.scoring;

import entities.overlay.Region;

public abstract class Scorer {
    public abstract int score(Region region);
    public abstract int scoreAtEnd(Region region);
}
