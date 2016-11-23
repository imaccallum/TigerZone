package game.scoring;

import entities.board.Board;
import entities.board.Terrain;
import entities.board.Tile;
import entities.overlay.Region;
import entities.overlay.TigerDen;
import entities.overlay.TileSection;

import java.util.List;

public class JungleScorer extends Scorer {
    @Override
    public int score(Region region) {
        return 0;
    }

    @Override
    public int scoreAtEnd(Region region) {
        int score = 0;

        for(TileSection tileSection : region.getTileSections()){
            Tile tile = tileSection.getTile();
            for(TileSection tileTileSection : tile.getTileSections()){
                Region tileRegion = tileTileSection.getRegion();
                if(tileRegion.getTerrain() == Terrain.LAKE && tileRegion.isFinished())
                    score += 3;
            }
            if(tile.hasDen()){
                //Do scoring for TigerDen
            }
        }
        return score;
    }
}
