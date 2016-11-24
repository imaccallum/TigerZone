package game.scoring;

import entities.board.Board;
import entities.board.PreyAnimal;
import entities.board.Tile;
import entities.board.TileFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class LakeScorerTest {
    private Tile rightTile;
    private Tile leftTile;
    private Tile centerTile;
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

    @Test
    public void testOpenLakeWithThreeUniquePreyAnimalsShouldBeWorthSixteenPoints() throws Exception{
        setupSixteenPointOpenLakeWithThreeUniquePreyAnimals();
        Board board = new Board(10,centerTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        board.place(leftTile, new Point(center.x - 1, center.y));
        board.place(bottomTile, new Point(center.x, center.y + 1));
        Scorer lakeScorer = rightTile.getTileSections().get(0).getRegion().getScorer();
        Assert.assertEquals(16, lakeScorer.score());
    }

    @Test
    public void testOpenLakeWithOneUniquePreyAnimalsButTwoDuplicatePreyAnimalsShouldBeWorthEightPoints() throws Exception{
        // it's the same thing as testOpenLakeWithThreeUniquePreyAnimalsShouldBeWorthSixteenPoints()
        // but it has three boars instead of one of each PreyAnimal, to check that it only counts UNIQUE prey animals
        setupEightPointOpenLakeWithOneUniquePreyAnimalButTwoDuplicatePreyAnimals();
        Board board = new Board(10,centerTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        board.place(leftTile, new Point(center.x - 1, center.y));
        board.place(bottomTile, new Point(center.x, center.y + 1));
        Scorer lakeScorer = rightTile.getTileSections().get(0).getRegion().getScorer();
        Assert.assertEquals(8, lakeScorer.score());
    }

    @Test
    public void testClosedLakeWithTwoUniquePreyAnimalsShouldBeWorthEighteenPoints() throws Exception{
        setupEighteenPointClosedLakeWithTwoUniquePreyAnimals();
        Board board = new Board(10,centerTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        board.place(leftTile, new Point(center.x - 1, center.y));
        Scorer lakeScorer = rightTile.getTileSections().get(1).getRegion().getScorer();
        Assert.assertEquals(18, lakeScorer.score());
    }

    @Test
    public void testOpenLakeWithCrocodileButNoPreyAnimalsShouldStillBeWorthTwoPoints() throws Exception{
        // testOpenLakeShouldBeWorthTwoPoints() with rightTile having a crocodile
        // ensure that crocodile doesn't make the multiplier zero/negative
        setupCrocodiledOpenLakeWithNoUniquePrey();
        Board board = new Board(10, leftTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        Scorer lakeScorer = rightTile.getTileSections().get(1).getRegion().getScorer();
        Assert.assertEquals(2, lakeScorer.score());
    }

    @Test
    public void testOpenLakeWithThreeUniquePreyAnimalsAndOneCrocodileShouldBeWorthEightPoints() throws Exception{
        setupCrocodiledOpenLakeWithSomeUniquePrey();
        Board board = new Board(10,centerTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        board.place(leftTile, new Point(center.x - 1, center.y));
        board.place(bottomTile, new Point(center.x, center.y + 1));
        Scorer lakeScorer = rightTile.getTileSections().get(0).getRegion().getScorer();
        Assert.assertEquals(8, lakeScorer.score());
    }

    @Test
    public void testClosedLakeWithTwoUniquePreyAnimalsAndOneCrocodileShouldBeWorthTwelvePoints() throws Exception{
        setupCrocodiledClosedLakeWithSomeUniquePrey();
        Board board = new Board(10,centerTile);
        Point center = board.getLastPlacedTile().getLocation();
        board.place(rightTile, new Point(center.x + 1, center.y));
        board.place(leftTile, new Point(center.x - 1, center.y));
        Scorer lakeScorer = rightTile.getTileSections().get(1).getRegion().getScorer();
        Assert.assertEquals(12, lakeScorer.score());
    }

    // |LJJJ---------------------LJJJ-||LJJJ---------------------LJJJ-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |  JUNGLE    False      LAKE   ||   LAKE     False      JUNGLE |
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |LJJJ---------------------LJJJ-||LJJJ---------------------LJJJ-|

    private void setupFourPointClosedLake(){
        rightTile = factory.makeTile("LJJJ-");
        leftTile = factory.makeTile("LJJJ-");
        rightTile.rotateCounterClockwise(1);
        leftTile.rotateCounterClockwise(3);
    }

    // |JLLL---------------------JLLL-||LJJJ---------------------LJJJ-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |   LAKE     False      LAKE   ||   LAKE     False      JUNGLE |
    // |    X        LAKE       X     ||    X       JUNGLE      X     |
    // |JLLL---------------------JLLL-||LJJJ---------------------LJJJ-|

    private void setupTwoPointOpenLake(){
        rightTile = factory.makeTile("LJJJ-");
        leftTile = factory.makeTile("JLLL-");
        rightTile.rotateCounterClockwise(1);
    }

    //            has a Deer                      has a Boar                    has a Buffalo
    // |LJJJ---------------------LJJJ-||JLLL---------------------JLLL-||JLLL---------------------JLLL-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |  JUNGLE    False      LAKE   ||   LAKE     False      LAKE   ||   LAKE     False      LAKE   |
    // |    X       JUNGLE      X     ||    X        LAKE       X     ||    X        LAKE       X     |
    // |LJJJ---------------------LJJJ-||JLLL---------------------JLLL-||JLLL---------------------JLLL-|
    //                                 |LJJJ---------------------LJJJ-|
    //                                 |    X        LAKE       X     |
    //                                 |  JUNGLE    False     JUNGLE  |
    //                                 |    X       JUNGLE      X     |
    //                                 |LJJJ---------------------LJJJ-|

    private void setupSixteenPointOpenLakeWithThreeUniquePreyAnimals(){
        // includes three unique prey animals and four city tiles [4*(1+3)])
        centerTile = factory.makeTile("JLLL-");
        centerTile.setPreyAnimal(PreyAnimal.BOAR);
        rightTile = factory.makeTile("JLLL-");
        rightTile.setPreyAnimal(PreyAnimal.BUFFALO);
        leftTile = factory.makeTile("LJJJ-");
        leftTile.setPreyAnimal(PreyAnimal.DEER);
        leftTile.rotateCounterClockwise(3);
        bottomTile = factory.makeTile("LJJJ-");
    }

    //            has a Boar                      has a Boar                    has a Boar
    // |LJJJ---------------------LJJJ-||JLLL---------------------JLLL-||JLLL---------------------JLLL-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |  JUNGLE    False      LAKE   ||   LAKE     False      LAKE   ||   LAKE     False      LAKE   |
    // |    X       JUNGLE      X     ||    X        LAKE       X     ||    X        LAKE       X     |
    // |LJJJ---------------------LJJJ-||JLLL---------------------JLLL-||JLLL---------------------JLLL-|
    //                                 |LJJJ---------------------LJJJ-|
    //                                 |    X        LAKE       X     |
    //                                 |  JUNGLE    False     JUNGLE  |
    //                                 |    X       JUNGLE      X     |
    //                                 |LJJJ---------------------LJJJ-|

    private void setupEightPointOpenLakeWithOneUniquePreyAnimalButTwoDuplicatePreyAnimals(){
        // includes three unique prey animals and four city tiles [4*(1+1)])
        setupSixteenPointOpenLakeWithThreeUniquePreyAnimals();
        rightTile.setPreyAnimal(PreyAnimal.BOAR);
        leftTile.setPreyAnimal(PreyAnimal.BOAR);
    }

    //                                            has a Boar
    // |LJJJ---------------------LJJJ-||JLJL---------------------JLJL-||JLLL---------------------JLLL-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |  JUNGLE    False      LAKE   ||   LAKE     False      LAKE   ||   LAKE     False     JUNGLE  |
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |LJJJ---------------------LJJJ-||JLJL---------------------JLJL-||JLLL---------------------JLLL-|

    private void setupEighteenPointClosedLakeWithTwoUniquePreyAnimals() {
        // includes one unique prey animal and three city tiles [(3*2)*(1+1)]
        centerTile = factory.makeTile("JLJL-");
        centerTile.setPreyAnimal(PreyAnimal.BOAR);
        rightTile = factory.makeTile("LJJJ-");
        rightTile.rotateCounterClockwise(1);
        leftTile = factory.makeTile("LJJJ-");
        leftTile.rotateCounterClockwise(3);
        leftTile.setPreyAnimal(PreyAnimal.DEER);
    }

    //                                        has a Crocodile
    // |JLLL---------------------JLLL-||LJJJ---------------------LJJJ-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |   LAKE     False      LAKE   ||   LAKE     False      JUNGLE |
    // |    X        LAKE       X     ||    X       JUNGLE      X     |
    // |JLLL---------------------JLLL-||LJJJ---------------------LJJJ-|

    private void setupCrocodiledOpenLakeWithNoUniquePrey() {
        // make sure crocodile doesn't reduce multiplier to negative (two tiles, one croc ==> (2*1) = 2 Points
        setupTwoPointOpenLake();
        rightTile.setHasCrocodile(true);
    }

    //            has a Deer                                                    has a Buffalo
    // |LJJJ---------------------LJJJ-||JLLL---------------------JLLL-||JLLL---------------------JLLL-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |  JUNGLE    False      LAKE   ||   LAKE     False      LAKE   ||   LAKE     False      LAKE   |
    // |    X       JUNGLE      X     ||    X        LAKE       X     ||    X        LAKE       X     |
    // |LJJJ---------------------LJJJ-||JLLL---------------------JLLL-||JLLL---------------------JLLL-|
    //                                 |LJJJ---------------------LJJJ-|
    //                                 |    X        LAKE       X     |
    //                                 |  JUNGLE    False     JUNGLE  |
    //                                 |    X       JUNGLE      X     |
    //                                 |LJJJ---------------------LJJJ-|
    //                                          has a Crocodile

    private void setupCrocodiledOpenLakeWithSomeUniquePrey() {
        // (four tiles, two unique prey, one croc ==> [4*(1+2-1)] = 8 Points)
        setupSixteenPointOpenLakeWithThreeUniquePreyAnimals();
        centerTile.setPreyAnimal(null);
        bottomTile.setHasCrocodile(true);
    }

    //            has a Deer                       has a Boar                   has a Crocodile
    // |LJJJ---------------------LJJJ-||JLJL---------------------JLJL-||JLLL---------------------JLLL-|
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |  JUNGLE    False      LAKE   ||   LAKE     False      LAKE   ||   LAKE     False     JUNGLE  |
    // |    X       JUNGLE      X     ||    X       JUNGLE      X     ||    X       JUNGLE      X     |
    // |LJJJ---------------------LJJJ-||JLJL---------------------JLJL-||JLLL---------------------JLLL-|

    private void setupCrocodiledClosedLakeWithSomeUniquePrey(){
        // three tiles, two unique prey, one croc ==> [(3*2)*(1+2-1)] = 12 Points)
        setupEighteenPointClosedLakeWithTwoUniquePreyAnimals();
        rightTile.setHasCrocodile(true);
    }
}