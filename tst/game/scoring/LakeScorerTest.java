package game.scoring;

import entities.board.Board;
import entities.board.Tile;
import entities.board.TileFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;


public class LakeScorerTest {
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

    @Test
    public void testClosedLakeShouldBeWorthFourPoints() throws Exception{
        setupFourPointClosedLake();
        Board board = new Board(10, leftTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        Scorer lakeScorer = leftTile.getTileSections().get(1).getRegion().getScorer();
        Assert.assertEquals(4, lakeScorer.score());
    }

    @Test
    public void testOpenLakeShouldBeWorthTwoPoints() throws Exception{
        setupTwoPointOpenLake();
        Board board = new Board(10, leftTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        Scorer lakeScorer = leftTile.getTileSections().get(2).getRegion().getScorer();
        Assert.assertEquals(2, lakeScorer.score());
    }


    // |LJJJ---------------------LJJJ-||LJJJ---------------------LJJJ-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |  JUNGLE    False      LAKE   ||   LAKE     False      JUNGLE |
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |LJJJ---------------------LJJJ-||LJJJ---------------------LJJJ-|

    public void setupFourPointClosedLake(){
        rightTile = factory.makeTile("LJJJ-");
        leftTile = factory.makeTile("LJJJ-");
        rightTile.rotateCounterClockwise(1);
        leftTile.rotateCounterClockwise(3);
    }

    // |JLJL---------------------JLJL-||LJJJ---------------------LJJJ-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |   LAKE     False      LAKE   ||   LAKE     False      JUNGLE |
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |JLJL---------------------JLJL-||LJJJ---------------------LJJJ-|

    public void setupTwoPointOpenLake(){
        rightTile = factory.makeTile("LJJJ-");
        leftTile = factory.makeTile("JLJL-");
        rightTile.rotateCounterClockwise(1);
    }
}
//LJJJ-