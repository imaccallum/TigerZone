package entities.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TileTest {
    private Tile testTile;

    @Before
    public void setup() {
        TileFactory f = new TileFactory();
        testTile = f.makeTile('a');
    }

    @Test
    public void test() {

        testTile.rotateClockwise(1);
    }
}