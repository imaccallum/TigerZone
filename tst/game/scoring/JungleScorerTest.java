package game.scoring;

import entities.board.Board;
import entities.board.Tile;
import entities.board.TileFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class JungleScorerTest {
    private Tile topLeftTile;
    private Tile topRightTile;
    private Tile bottomRightTile;
    private Tile bottomLeftTile;
    private Tile rightTile;
    private Tile leftTile;
    private Tile centerTile;
    private Tile topTile;
    private Tile bottomTile;
    private TileFactory factory;

    @Before
    public void setup() {
        factory = new TileFactory();
    }

    @Test  // Should not throw
    public void testTileSetupShouldBeWorthThreePoints() throws Exception {
        setupThreePointsTiles();
        Board board = new Board(10, topLeftTile);
        Point lastLocation = board.getLastPlacedTile().getLocation();
        board.place(topRightTile, new Point(lastLocation.x + 1, lastLocation.y));
        board.place(bottomLeftTile, new Point(lastLocation.x + 1, lastLocation.y + 1));
        board.place(bottomRightTile, new Point(lastLocation.x, lastLocation.y + 1));
        Scorer jungleScorer = bottomRightTile.getTileSections().get(0).getRegion().getScorer();
        Assert.assertEquals(3, jungleScorer.score());
    }

    @Test
    public void testTileSetupShouldBeWorthEightPoints() throws Exception {
        setupElevenPointsTiles();
        Board board = new Board(10, centerTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(topTile, new Point(center.x, center.y - 1));
        board.place(topLeftTile, new Point(center.x - 1, center.y - 1));
        board.place(topRightTile, new Point(center.x + 1, center.y - 1));
        board.place(leftTile, new Point(center.x - 1, center.y));
        board.place(rightTile, new Point(center.x + 1, center.y));
        board.place(bottomLeftTile, new Point(center.x - 1, center.y + 1));
        board.place(bottomTile, new Point(center.x, center.y + 1));
        board.place(bottomRightTile, new Point(center.x + 1, center.y + 1));
        Scorer jungleScorer = bottomRightTile.getTileSections().get(0).getRegion().getScorer();
        Assert.assertEquals(11, jungleScorer.score());
    }

    // |TLLTB--------------------TLLTB||LJLJ---------------------LJLJ-|
    // |  JUNGLE    TRAIL     JUNGLE  ||    X       JUNGLE      X     |
    // |  TRAIL     False      LAKE   ||   LAKE     False      LAKE   |
    // |  JUNGLE     LAKE       X     ||    X       JUNGLE      X     |
    // |TLLTB--------------------TLLTB||LJLJ---------------------LJLJ-|
    // |LJLJ---------------------LJLJ-||JJJJX--------------------JJJJX|
    // |    X        LAKE       X     ||    X       JUNGLE      X     |
    // |  JUNGLE    False     JUNGLE  ||  JUNGLE     True     JUNGLE  |
    // |    X        LAKE       X     ||    X       JUNGLE      X     |
    // |LJLJ---------------------LJLJ-||JJJJX--------------------JJJJX|

    // Has one completed lake, should be 3 points
    private void setupThreePointsTiles() {
        topLeftTile = factory.makeTile("TLLTB");
        topRightTile = factory.makeTile("LJLJ-");
        topRightTile.rotateCounterClockwise(1);
        bottomRightTile = factory.makeTile("LJLJ-");
        bottomLeftTile = factory.makeTile("JJJJX");
    }

    // |TLLTB--------------------TLLTB||LJLJ---------------------LJLJ-||LJLJ---------------------LJLJ-|
    // |  JUNGLE    TRAIL     JUNGLE  ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |  TRAIL     False      LAKE   ||   LAKE     False      LAKE   ||   LAKE     False      LAKE   |
    // |  JUNGLE     LAKE       X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |TLLTB--------------------TLLTB||LJLJ---------------------LJLJ-||LJLJ---------------------LJLJ-|
    // |LJLJ---------------------LJLJ-||JJJJX--------------------JJJJX||JJJJ---------------------JJJJ-|
    // |    X        LAKE       X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |  JUNGLE    False     JUNGLE  ||  JUNGLE     True     JUNGLE  ||  JUNGLE     False     JUNGLE |
    // |    X        LAKE       X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |LJLJ---------------------LJLJ-||JJJJX--------------------JJJJX||JJJJ---------------------JJJJ-|
    // |LLLL---------------------LLLL-||LJJJ---------------------LJJJ-||JJJJ---------------------JJJJ-|
    // |    X        LAKE       X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |   LAKE     False      LAKE   ||   LAKE     False     JUNGLE  ||  JUNGLE     False     JUNGLE |
    // |    X        LAKE       X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |LLLL---------------------LLLL-||LJJJ---------------------LJJJ-||JJJJ---------------------JJJJ-|

    // Has two completed lakes and a completed den, should be 11 points
    private void setupElevenPointsTiles() {
        topRightTile = factory.makeTile("LJLJ-");
        topRightTile.rotateCounterClockwise(1);
        topTile = factory.makeTile("LJLJ-");
        topTile.rotateCounterClockwise(1);
        topLeftTile = factory.makeTile("TLLTB");
        centerTile = factory.makeTile("JJJJX");
        rightTile = factory.makeTile("JJJJ-");
        leftTile = factory.makeTile("LJLJ-");
        bottomLeftTile = factory.makeTile("LLLL-");
        bottomTile = factory.makeTile("LJJJ-");
        bottomTile.rotateCounterClockwise(1);
        bottomRightTile = factory.makeTile("JJJJ-");
    }
}
