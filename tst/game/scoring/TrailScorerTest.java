package game.scoring;

import entities.board.Board;
import entities.board.PreyAnimal;
import entities.board.Tile;
import entities.board.TileFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class TrailScorerTest {
    private Tile rightTile;
    private Tile leftTile;
    private Tile middleRightTile;
    private Tile middleLeftTile;
    private TileFactory factory;

    @Before
    public void setup() {
        factory = new TileFactory();
    }

    @Test
    public void testClosedTrailShouldBeWorthFourPoints() throws Exception{
        setupFourPointClosedTrail();
        Board board = new Board(10, leftTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(middleLeftTile, new Point(center.x + 1, center.y));
        board.place(middleRightTile, new Point(center.x + 2, center.y));
        board.place(rightTile, new Point(center.x + 3, center.y));
        Scorer trailScorer = middleLeftTile.getTileSections().get(2).getRegion().getScorer();
        Assert.assertEquals(4, trailScorer.score());
    }

    @Test
    public void testOpenTrailShouldBeWorthTwoPoints() throws Exception{
        setupTwoPointOpenTrail();
        Board board = new Board(10, leftTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        Scorer trailScorer = rightTile.getTileSections().get(2).getRegion().getScorer();
        Assert.assertEquals(2, trailScorer.score());
    }

    @Test
    public void testClosedTrailWithFourPreyAnimalsShouldBeWorthEightPoints() throws Exception{
        setupEightPointClosedTrailWithFourPreyAnimals();
        Board board = new Board(10, leftTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(middleLeftTile, new Point(center.x + 1, center.y));
        board.place(middleRightTile, new Point(center.x + 2, center.y));
        board.place(rightTile, new Point(center.x + 3, center.y));
        Scorer trailScorer = middleLeftTile.getTileSections().get(2).getRegion().getScorer();
        Assert.assertEquals(8, trailScorer.score());
    }

    @Test
    public void testOpenTrailWithOnePreyAnimalShouldBeWorthThreePoints() throws Exception{
        setupThreePointOpenTrailWithOnePreyAnimal();
        Board board = new Board(10, leftTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        Scorer trailScorer = rightTile.getTileSections().get(2).getRegion().getScorer();
        Assert.assertEquals(3, trailScorer.score());
    }

    @Test
    public void testClosedTrailWithOnePreyAnimalAndTwoCrocodilesShouldBeWorthFourPoints() throws Exception{
        setupFourPointClosedTrailWithOnePreyAnimalAndTwoCrocodiles();
        Board board = new Board(10, leftTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(middleLeftTile, new Point(center.x + 1, center.y));
        board.place(middleRightTile, new Point(center.x + 2, center.y));
        board.place(rightTile, new Point(center.x + 3, center.y));
        Scorer trailScorer = middleLeftTile.getTileSections().get(2).getRegion().getScorer();
        Assert.assertEquals(4, trailScorer.score());
    }

    @Test
    public void testClosedTrailWithTwoPreyAnimalsAndOneCrocodileShouldBeWorthFivePoints() throws Exception{
        setupFivePointClosedTrailWithTwoPreyAnimalsAndOneCrocodile();
        Board board = new Board(10, leftTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(middleLeftTile, new Point(center.x + 1, center.y));
        board.place(middleRightTile, new Point(center.x + 2, center.y));
        board.place(rightTile, new Point(center.x + 3, center.y));
        Scorer trailScorer = middleLeftTile.getTileSections().get(2).getRegion().getScorer();
        Assert.assertEquals(5, trailScorer.score());
    }


    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-||TJTJ---------------------TJTJ-||TTTT---------------------TTTT-|
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  ||  JUNGLE    JUNGLE    JUNGLE  ||    X       TRAIL       X     |
    // |  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  |
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  ||  JUNGLE    JUNGLE    JUNGLE  ||    X       TRAIL       X     |
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-||TJTJ---------------------TJTJ-||TTTT---------------------TTTT-|

    private void setupFourPointClosedTrail(){
        rightTile = factory.makeTile("TTTT-");
        leftTile = factory.makeTile("TTTT-");
        middleLeftTile = factory.makeTile("TJTJ-");
        middleLeftTile.rotateCounterClockwise(1);
        middleRightTile = factory.makeTile("TJTJ-");
        middleRightTile.rotateCounterClockwise(1);
    }

    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-|
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  |
    // |  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  |
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  |
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-|

    private void setupTwoPointOpenTrail(){
        rightTile = factory.makeTile("TJTJ-");
        rightTile.rotateCounterClockwise(1);
        leftTile = factory.makeTile("TTTT-");
    }

    //            has a Boar                    has a Deer                      has a Buffalo                   has a Deer
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-||TJTJ---------------------TJTJ-||TTTT---------------------TTTT-|
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  ||  JUNGLE    JUNGLE    JUNGLE  ||    X       TRAIL       X     |
    // |  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  |
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  ||  JUNGLE    JUNGLE    JUNGLE  ||    X       TRAIL       X     |
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-||TJTJ---------------------TJTJ-||TTTT---------------------TTTT-|


    // four tiles, four prey animals ==> [4+4 = 8 points]
    private void setupEightPointClosedTrailWithFourPreyAnimals(){
        setupFourPointClosedTrail();
        leftTile.setPreyAnimal(PreyAnimal.BOAR);
        middleLeftTile.setPreyAnimal(PreyAnimal.DEER);
        middleRightTile.setPreyAnimal(PreyAnimal.BUFFALO);
        rightTile.setPreyAnimal(PreyAnimal.DEER);
    }

    //            has a Deer
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-|
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  |
    // |  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  |
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  |
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-|

    // two tiles, one prey animal ==> [2+1 = 3 points]
    private void setupThreePointOpenTrailWithOnePreyAnimal(){
        setupTwoPointOpenTrail();
        leftTile.setPreyAnimal(PreyAnimal.DEER);
    }

    //         has a Crocodile                has a Crocodile                                                    has a Deer
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-||TJTJ---------------------TJTJ-||TTTT---------------------TTTT-|
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  ||  JUNGLE    JUNGLE    JUNGLE  ||    X       TRAIL       X     |
    // |  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  |
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  ||  JUNGLE    JUNGLE    JUNGLE  ||    X       TRAIL       X     |
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-||TJTJ---------------------TJTJ-||TTTT---------------------TTTT-|

    // crocodiles can't make the addition negative. Four tiles, two crocs, one prey ==> [4+0 = 4 points]
    private void setupFourPointClosedTrailWithOnePreyAnimalAndTwoCrocodiles(){
        setupFourPointClosedTrail();
        leftTile.placeCrocodile();
        middleLeftTile.placeCrocodile();
        rightTile.setPreyAnimal(PreyAnimal.DEER);
    }

    //         has a Crocodile                   has a Deer                                                      has a Deer
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-||TJTJ---------------------TJTJ-||TTTT---------------------TTTT-|
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  ||  JUNGLE    JUNGLE    JUNGLE  ||    X       TRAIL       X     |
    // |  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  ||  TRAIL     False      TRAIL  |
    // |    X       TRAIL       X     ||  JUNGLE    JUNGLE    JUNGLE  ||  JUNGLE    JUNGLE    JUNGLE  ||    X       TRAIL       X     |
    // |TTTT---------------------TTTT-||TJTJ---------------------TJTJ-||TJTJ---------------------TJTJ-||TTTT---------------------TTTT-|

    // Four tiles, two prey, one croc ==> [4+(2-1) = 5 points]
    private void setupFivePointClosedTrailWithTwoPreyAnimalsAndOneCrocodile(){
        setupFourPointClosedTrail();
        leftTile.placeCrocodile();
        middleLeftTile.setPreyAnimal(PreyAnimal.DEER);
        rightTile.setPreyAnimal(PreyAnimal.DEER);
    }
}