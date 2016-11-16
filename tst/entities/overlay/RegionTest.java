package entities.overlay;

import entities.board.Terrain;
import entities.board.Tiger;
import entities.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RegionTest {
    private Region testRegionA;
    private Region testRegionB;
    private Region testRegionC;
    private Tiger diegoTiger;
    private Tiger trevorTiger0;
    private Tiger trevorTiger;
    private Player Diego;
    private Player Trevor;

    @Before
    public void setup() {
        testRegionA = new Region(Terrain.JUNGLE);
        testRegionB = new Region(Terrain.JUNGLE);
        testRegionC = new Region(Terrain.LAKE);
        Diego = new Player("Diego");
        Trevor = new Player("Trevor");
        diegoTiger = new Tiger(Diego);
        trevorTiger0 = new Tiger(Trevor);
        trevorTiger = new Tiger(Trevor);


    }

    @Test
    public void testCombineRegionShouldHaveCorrectNumberOfTigers(){
        testRegionA.addTiger(diegoTiger);
        testRegionB.combineWithRegion(testRegionA);
        Assert.assertTrue(testRegionB.getTigerList().contains(diegoTiger));
    }

    @Test
    public void testisDisputedShouldReturnTrueIfRegionIsShared(){
        testRegionB.addTiger(diegoTiger);
        Assert.assertFalse(testRegionA.isDisputed());
        testRegionB.addTiger(trevorTiger);
        Assert.assertTrue(testRegionB.isDisputed());
    }

    @Test
    public void testgetDominantPlayersShouldReturnPlayerWithMostTigers(){
        testRegionA.addTiger(diegoTiger, trevorTiger0, trevorTiger);
        Assert.assertEquals(testRegionA.getDominantPlayers().get(0), Trevor);
    }
}
