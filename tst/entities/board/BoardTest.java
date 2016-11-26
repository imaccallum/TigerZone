package entities.board;

import entities.player.Player;
import exceptions.BadPlacementException;
import exceptions.TigerAlreadyPlacedException;
import game.LocationAndOrientation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BoardTest {

    private TileFactory factory;
    private Board testBoard;
    private Tile startingTile;
    private Tile secondTile;

    @Before
    public void setup(){
        factory = new TileFactory();
        startingTile = factory.makeTile("TLTJ-");
        secondTile = factory.makeTile("JLTTB");
        testBoard = new Board(77 , startingTile);
    }

    @Test (expected = BadPlacementException.class)
    public void testInsertAtLocationWithNoTilesAroundShouldThrowException() throws BadPlacementException{
        testBoard.place(secondTile, new Point(80,80));
    }

    @Test // Should not throw
    public void testInsertAtValidLocationShouldAddTileToListAndRemoveLocationFromOpenLocation() throws Exception{
        Point point = new Point(76,75);
        testBoard.place(secondTile, point);
        Assert.assertEquals(testBoard.getTile(point), secondTile);
        Assert.assertEquals(testBoard.getNumTiles(), 2);
        Assert.assertFalse(testBoard.getOpenTileLocations().contains(point));

        boolean isOpenToTheLeft = false;
        boolean isOpenToTheRight = false;
        boolean isOpenToTheTop = false;
        boolean isOpenToTheBottom = false;

        for (Point location: testBoard.getOpenTileLocations()) {
            if (location.x == 75 && location.y == 75){
                isOpenToTheLeft = true;
            } else if(location.x == 77 && location.y == 75){
                isOpenToTheRight = true;
            } else if(location.x == 76 && location.y == 74){
                isOpenToTheTop = true;
            } else if(location.x == 76 && location.y == 76){
                isOpenToTheBottom = true;
            }
        }
        Assert.assertTrue(isOpenToTheLeft);
        Assert.assertTrue(isOpenToTheRight);
        Assert.assertTrue(isOpenToTheTop);
        Assert.assertFalse(isOpenToTheBottom);
        Assert.assertEquals(testBoard.getLastPlacedTile(), secondTile);
    }

    @Test
    public void testFindValidTilePlacementsShouldReturnAllValidPlacements(){
        List<LocationAndOrientation> locationsAndOrientations = testBoard.findValidTilePlacements(secondTile);

        boolean rOnTopOfS_rotate0 = false;
        boolean rToTheRightOfS_rotate2 = false;
        boolean rUnderS_rotate3 = false;
        boolean rUnderS_rotate2 =false;
        boolean rToTheLeftOfS_rotate3 = false;
        boolean rOnTopOfS_rotate1 = false;

        for (LocationAndOrientation position: locationsAndOrientations) {
            if (position.getLocation().x == 76 && position.getLocation().y == 75 && position.getOrientation() == 0){
                rOnTopOfS_rotate0 = true;
            } else if (position.getLocation().x == 77 && position.getLocation().y == 76 && position.getOrientation() == 2){
                rToTheRightOfS_rotate2 = true;
            } else if (position.getLocation().x == 76 && position.getLocation().y == 77 && position.getOrientation() == 3){
                rUnderS_rotate3 = true;
            } else if (position.getLocation().x == 76 && position.getLocation().y == 77 && position.getOrientation() == 2){
                rUnderS_rotate2 = true;
            } else if (position.getLocation().x == 75 && position.getLocation().y == 76 && position.getOrientation() == 3){
                rToTheLeftOfS_rotate3 = true;
            } else if(position.getLocation().x == 76 && position.getLocation().y == 75 && position.getOrientation() == 1){
                rOnTopOfS_rotate1 = true;
            }

        }

        Assert.assertTrue(rOnTopOfS_rotate0);
        Assert.assertTrue(rToTheRightOfS_rotate2);
        Assert.assertTrue(rUnderS_rotate3);
        Assert.assertTrue(rUnderS_rotate2);
        Assert.assertTrue(rToTheLeftOfS_rotate3);
        Assert.assertTrue(rOnTopOfS_rotate1);
    }

    @Test
    public void testFindValidTilePlacementsWhenThereAreNoneShouldReturnEmptyList(){
        Tile tileA = factory.makeTile("JJJJ-");
        Tile tileB = factory.makeTile("LLLL-");

        Board secondBoard = new Board(77, tileA);
        Assert.assertTrue(secondBoard.findValidTilePlacements(tileB).size() == 0);
    }

    @Test // Should not throw
    public void testGetTileShouldReturnProperTileAtGivenCoordinates()throws Exception{
        Point point = new Point(76,75);
        testBoard.place(secondTile, point);
        Assert.assertEquals(testBoard.getTile(point), secondTile);
    }

    @Test // Should not throw
    public void testGetPossibleTileSectionTigerPlacementsShouldReturnListWithTileSectionsYouCanPlaceATigerOn() throws Exception{
        Assert.assertTrue(testBoard.getPossibleTileSectionTigerPlacements().size() == 4);
        Point point = new Point(76,75);
        testBoard.place(secondTile, point);
        Assert.assertTrue(testBoard.getPossibleTileSectionTigerPlacements().size() == 4);
    }

    @Test // Should not throw
    public void testCanPlaceTigerShouldReturnTrueIfTileSectionIsEmpty() throws TigerAlreadyPlacedException {
        Assert.assertTrue(testBoard.canPlaceTiger(startingTile.getTileSections().get(0)));
        startingTile.getTileSections().get(0).placeTiger(new Tiger("Diego", false));
        Assert.assertFalse(testBoard.canPlaceTiger(startingTile.getTileSections().get(0)));
    }
}
