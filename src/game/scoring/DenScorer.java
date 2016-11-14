package game.scoring;

import entities.board.Tile;
import entities.overlay.Region;

public class DenScorer extends Scorer {
    @Override
    public int score(Region region) {
        // If there's a completed den, return a full 9 points
        super.returnMeeples(region);
        return 9;
    }

    @Override
    public int scoreAtEnd(Region region) {
        // Gets the tile from the region that is passed in. Since it's a den, we get the tile that the den is in
        Tile tile = region.getTileSections().get(0).getTile();
        Tile[] adjacent = tile.getAdjacentTiles();
        Tile[] corner = tile.getCornerTiles();

        int points = 1 + (adjacent[0] == null ? 0 : 1) + (adjacent[1] == null ? 0 : 1) + (adjacent[2] == null ? 0 : 1)
                + (adjacent[3] == null ? 0 : 1) + (corner[0] == null ? 0 : 1) + (corner[1] == null ? 0 : 1) +
                (corner[2] == null ? 0 : 1) + (corner[3] == null ? 0 : 1);

        return points;
    }
}
