package game.scoring;

import entities.board.Tile;
import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.HashSet;
import java.util.List;

public class TrailScorer extends Scorer {
    @Override
    public int score(Region region) {
        List<TileSection> tileSections = region.getTileSections();
        HashSet<Tile> regionTiles = new HashSet<>();

        for(TileSection tileSection : tileSections){
            regionTiles.add(tileSection.getTile());
        }

        int predationScore = 0;
        for(Tile tile : regionTiles){
            if(tile.getPreyAnimal() != null)
                predationScore++;
            else if(tile.hasCrocodile()){
                predationScore--;
            }
        }

        int score = regionTiles.size() + ((predationScore > 0) ? predationScore : 0);

        super.returnTigers(region);

        return score;
    }

    @Override
    public int scoreAtEnd(Region region) {
        return score(region);
    }
}
