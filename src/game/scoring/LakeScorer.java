package game.scoring;

import entities.board.PreyAnimal;
import entities.board.Tile;
import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.HashSet;
import java.util.List;

public class LakeScorer extends Scorer {
    @Override
    public int score(Region region) {
        List<TileSection> tileSections = region.getTileSections();

        HashSet<Tile> regionTiles = new HashSet<>();

        for(TileSection tileSection : tileSections){
            regionTiles.add(tileSection.getTile());
        }

        int score = tileSections.size()*2, multiplier = 1;

        for(Tile tile : regionTiles){
            if(tile.getPreyAnimal() == PreyAnimal.BOAR){
                multiplier++;
                break;
            }
        }

        for(Tile tile : regionTiles){
            if(tile.getPreyAnimal() == PreyAnimal.DEER) {
                multiplier++;
                break;
            }
        }

        for(Tile tile : regionTiles){
            if(tile.getPreyAnimal() == PreyAnimal.BUFFALO){
                multiplier++;
                break;
            }
        }
        for (Tile tile: regionTiles) {
            if (tile.hasCrocodile()){
                multiplier--;
            }
        }

        super.returnTigers(region);

        return score * multiplier;
    }

    @Override
    public int scoreAtEnd(Region region) {
        List<TileSection> tileSections = region.getTileSections();

        HashSet<Tile> regionTiles = new HashSet<>();

        for(TileSection tileSection : tileSections){
            regionTiles.add(tileSection.getTile());
        }

        int score = tileSections.size(), multiplier = 1;

        for(Tile tile : regionTiles){
            if(tile.getPreyAnimal() == PreyAnimal.BOAR){
                multiplier++;
                break;
            }
        }

        for(Tile tile : regionTiles){
            if(tile.getPreyAnimal() == PreyAnimal.DEER) {
                multiplier++;
                break;
            }
        }

        for(Tile tile : regionTiles){
            if(tile.getPreyAnimal() == PreyAnimal.BUFFALO){
                multiplier++;
                break;
            }
        }

        for (Tile tile: regionTiles) {
            if (tile.hasCrocodile()){
                multiplier--;
            }
        }

        super.returnTigers(region);

        return score * (multiplier < 1 ? 1 : multiplier);
    }
}
