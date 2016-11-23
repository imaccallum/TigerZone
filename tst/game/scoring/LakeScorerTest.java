package game.scoring;

import entities.board.Board;
import entities.board.Tile;
import entities.board.TileFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

// Sorry this is really lackluster right now, family's taking over my life for a little bit again.
// I'll definitely be doing more of these tonight, though.  You can count on it.

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
        Scorer lakeScorer = rightTile.getTileSections().get(1).getRegion().getScorer();
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
    // |    X        LAKE       X     ||    X       JUNGLE      X     |
    // |JLJL---------------------JLJL-||LJJJ---------------------LJJJ-|

    public void setupTwoPointOpenLake(){
        rightTile = factory.makeTile("LJJJ-");
        leftTile = factory.makeTile("JLLL-");
        rightTile.rotateCounterClockwise(1);
    }

    //TODO public void setupSixteenPointOpenLake() (will include three unique prey animals and four city tiles [4*(1+3)])
        // using Tile.setPreyAnimal(PreyAnimal.BOAR);
        // or DEER or BUFFALO

    //TODO public void setupTwelvePointClosedLake() (will include one unique prey animal and three city tiles [(3*2)*(1+1)])

    //TODO public void setupCrocodiledOpenLakeWithNoUniquePrey()    (make sure crocodile doesn't reduce multiplier to negative) (two tiles, one croc ==> (2*1) = 2 Points)
        // using Tile.setHasCrocodile()

    //TODO public void setupCrocodiledClosedLakeWithNoUniquePrey()  (same reason as above, but with closed) (two tiles, one croc ==> [(2*2)*1] = 4 Points)

    //TODO public void setupCrocodiledOpenLakeWithSomeUniquePrey()  (three tiles, two unique prey, one croc ==> [3*(1+2-1)] = 6 Points)

    //TODO public void setupCrocodiledClosedLakeWithSomeUniquePrey()    (three tiles, two unique prey, one croc ==> [(3*2)*(1+2-1)] = 12 Points)
}