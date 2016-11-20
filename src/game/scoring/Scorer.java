package game.scoring;

import entities.board.Tiger;
import entities.overlay.Region;

import java.util.ArrayList;
import java.util.List;

public abstract class Scorer {
    public abstract int score(Region region);
    public abstract int scoreAtEnd(Region region);
    public void returnTigers(Region region){
        List<Tiger> tigerList = region.getAllTigers();

        for(Tiger t : tigerList){
            t.getOwningPlayer().incrementRemainingTigers();
        }

        tigerList.clear();
    }
}
