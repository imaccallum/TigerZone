package entities.overlay;

import entities.board.Node;
import entities.board.Terrain;
import entities.board.Tiger;
import entities.player.Player;
import exceptions.IncompatibleTerrainException;
import game.scoring.JungleScorer;
import game.scoring.LakeScorer;
import game.scoring.TrailScorer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class RegionTest {
    private TileSection testTileSectionA;
    private TileSection testTileSectionB;
    private TileSection testTileSectionC;
    private Region testRegionA;
    private Region testRegionB;
    private Region testRegionC;
    private Tiger diegoTiger;
    private Tiger trevorTiger;

    @Before
    public void setup() {
        testTileSectionA = new TileSection(Terrain.JUNGLE);
        testTileSectionA.addNodes(new Node(), new Node());
        testTileSectionB = new TileSection(Terrain.JUNGLE);
        testTileSectionB.addNodes(new Node(), new Node());
        testTileSectionC = new TileSection(Terrain.LAKE);
        testTileSectionC.addNodes(new Node(), new Node());
        testRegionA = new Region(Terrain.JUNGLE);
        testRegionB = new Region(Terrain.JUNGLE);
        testRegionC = new Region(Terrain.LAKE);
        Player diego = new Player("Diego");
        Player trevor = new Player("Trevor");
        diegoTiger = new Tiger(diego);
        trevorTiger = new Tiger(trevor);
    }

    @Test
    public void testAddTileSectionShouldAddATileSection() throws IncompatibleTerrainException {
        testRegionA.addTileSection(testTileSectionA);
        Assert.assertEquals(testRegionA.getTileSections().get(0), testTileSectionA);
    }

    @Test (expected = IncompatibleTerrainException.class)
    public void testAddIncompatibleTerrainThrowsException() throws IncompatibleTerrainException {
        testRegionC.addTileSection(testTileSectionA);
    }

    @Test // Should not throw
    public void testCombineRegionShouldHaveAllTigers() throws Exception {
        testTileSectionA.placeTiger(diegoTiger);
        testTileSectionB.placeTiger(trevorTiger);
        testRegionA.addTileSection(testTileSectionA);
        testRegionB.addTileSection(testTileSectionB);
        testRegionB.combineWithRegion(testRegionA);
        Assert.assertTrue(testRegionB.getAllTigers().contains(diegoTiger));
        Assert.assertTrue(testRegionB.getAllTigers().contains(trevorTiger));
    }

    @Test
    public void testCombineRegionShouldHaveAllTileSections() throws IncompatibleTerrainException {
        testRegionA.addTileSection(testTileSectionA);
        testRegionB.addTileSection(testTileSectionB);
        testRegionA.combineWithRegion(testRegionB);
        Assert.assertTrue(testRegionA.getTileSections().contains(testTileSectionA));
        Assert.assertTrue(testRegionA.getTileSections().contains(testTileSectionB));
    }

    @Test (expected = IncompatibleTerrainException.class)
    public void testCombineIncompatibleRegionsShouldThrowException() throws IncompatibleTerrainException {
        testRegionA.addTileSection(testTileSectionA);
        testRegionC.addTileSection(testTileSectionC);
        testRegionA.combineWithRegion(testRegionC);
    }

    @Test
    public void testCompleteAndIncompleteRegionsShouldReturnTrueIfCompleteFalseIfNot()
            throws IncompatibleTerrainException {
        Region testCompletedRegion = createCompletedRegion();
        Assert.assertTrue(testCompletedRegion.isFinished());
        testTileSectionA.addNodes(new Node());
        testRegionA.addTileSection(testTileSectionA);
        Assert.assertFalse(testRegionA.isFinished());
    }

    @Test // Should not throw
    public void testContainsTigersReturnsTrueIfTigersArePresentInRegion() throws Exception {
        testRegionA.addTileSection(testTileSectionA);
        testTileSectionA.placeTiger(diegoTiger);
        Assert.assertTrue(testRegionA.containsTigers());
        testRegionB.addTileSection(testTileSectionB);
        Assert.assertFalse(testRegionB.containsTigers());
    }

    @Test
    public void testGetScorerReturnsCorrectScorerClass() {
        Region jungle = new Region(Terrain.JUNGLE);
        Region lake = new Region(Terrain.LAKE);
        Region trail = new Region(Terrain.TRAIL);
        Assert.assertTrue(jungle.getScorer().getClass()== JungleScorer.class);
        Assert.assertTrue(lake.getScorer().getClass() == LakeScorer.class);
        Assert.assertTrue(trail.getScorer().getClass() == TrailScorer.class);
    }

    @Test
    public void testGetAllTigersContainsAllTigersInRegion() throws Exception {
        testTileSectionA.placeTiger(diegoTiger);
        testTileSectionB.placeTiger(trevorTiger);
        testRegionA.addTileSection(testTileSectionA);
        testRegionA.addTileSection(testTileSectionB);
        Assert.assertTrue(testRegionA.getAllTigers()
                .containsAll(Arrays.asList(diegoTiger, trevorTiger)));
    }

    private Region createCompletedRegion() throws IncompatibleTerrainException {
        Region region = new Region(Terrain.TRAIL);
        Node node1 = new Node();
        Node node2 = new Node();
        node1.setConnectedNode(node2);
        node2.setConnectedNode(node1);
        TileSection tileSection1 = new TileSection(Terrain.TRAIL);
        TileSection tileSection2 = new TileSection(Terrain.TRAIL);
        tileSection1.addNodes(node1);
        tileSection2.addNodes(node2);
        region.addTileSection(tileSection1);
        region.addTileSection(tileSection2);
        return region;
    }
}
