package game.scoring;

import entities.board.Tiger;
import entities.overlay.Region;

import java.util.ArrayList;
import java.util.List;

public abstract class Scorer {
    public abstract int score(Region region);
    public abstract int scoreAtEnd(Region region);
    public void returnMeeples(Region region){
        List<Tiger> tigerList = region.getTigerList();

        for(Tiger t : tigerList){
            t.getOwningPlayer().incrementRemainingFollowers();
        }

        tigerList.clear();
    }
}
