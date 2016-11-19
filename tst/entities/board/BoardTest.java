package entities.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BoardTest {
    private Board b;

    @Before
    public void setup(){
        TileFactory tileFactory = new TileFactory();
        Tile startingTile = tileFactory.makeTile('s');
        b = new Board(77, startingTile);
    }

}
