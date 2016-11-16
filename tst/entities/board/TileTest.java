package entities.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;

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