package game.scoring;

import entities.board.Tile;
import entities.board.TileFactory;
import org.junit.Before;

public class JungleScorerTest {
    private Tile topRightTile;
    private Tile topLeftTile;
    private Tile bottomRightTile;
    private Tile bottomLeftTile;


    // |TLLTB--------------------TLLTB|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False      LAKE   |
    // |  JUNGLE     LAKE       X     |
    // |TLLTB--------------------TLLTB|

    @Before
    public void setup() {
        TileFactory factory = new TileFactory();

        bottomLeftTile = factory.makeTile("TLLTB");
    }
}
