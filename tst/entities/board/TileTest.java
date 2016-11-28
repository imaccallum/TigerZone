package entities.board;

import entities.overlay.TileSection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

public class TileTest {
    private Tile testTile;

    @Before
    public void setup() {
        TileFactory factory = new TileFactory();
        testTile = factory.makeTile("JLTTB");
    }

    @Test
    public void testShouldContainCorrectEdgesAfterRotation() {
        testTile.rotateCounterClockwise(3);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.LAKE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.TRAIL);

        testTile.rotateCounterClockwise(3);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.LAKE);

        testTile.rotateCounterClockwise(3);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.LAKE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);

        testTile.rotateCounterClockwise(3);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.LAKE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.TRAIL);
    }

    @Test
    public void testShouldContainCorrectCornersAfterRotation() {
        testTile.rotateCounterClockwise(3);
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertNull(testTile.getCorner(CornerLocation.BOTTOM_RIGHT));
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);

        testTile.rotateCounterClockwise(3);
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertNull(testTile.getCorner(CornerLocation.BOTTOM_LEFT));

        testTile.rotateCounterClockwise(3);
        Assert.assertNull(testTile.getCorner(CornerLocation.TOP_LEFT));
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);

        testTile.rotateCounterClockwise(3);
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertNull(testTile.getCorner(CornerLocation.TOP_RIGHT));
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);
    }

    @Test
    public void testShouldContainCorrectAmountOfSections() {
        int jungles = 0, lakes = 0, trail = 0;
        for (TileSection t: testTile.getTileSections()) {
            switch (t.getTerrain()){
                case JUNGLE:
                    jungles++;
                    break;
                case LAKE:
                    lakes++;
                    break;
                case TRAIL:
                    trail++;
                    break;
            }
        }

        Assert.assertEquals(jungles, 2);
        Assert.assertEquals(lakes, 1);
        Assert.assertEquals(trail, 1);
        Assert.assertTrue(testTile.getPreyAnimal() == PreyAnimal.BUFFALO);
    }

    @Test
    public void testShouldContainCorrectAmountOfNodes() {
        int numberOfNodes = 0;
        for (TileSection tileSection: testTile.getTileSections()) {
            numberOfNodes += tileSection.getNodes().size();
        }
        Assert.assertEquals(numberOfNodes, 7);
    }

    @Test
    public void testClockwisenodeListShouldBeInCorrectOrder() {
        List<Node> clockwiseNodes = testTile.nodesClockwise();
        if (testTile.getCorner(CornerLocation.TOP_LEFT) != null) {
            Assert.assertEquals(clockwiseNodes.get(0), testTile.getCorner(CornerLocation.TOP_LEFT));
        }
        if (testTile.getEdge(EdgeLocation.TOP) != null) {
            Assert.assertEquals(clockwiseNodes.get(1), testTile.getEdge(EdgeLocation.TOP));
        }
        if (testTile.getCorner(CornerLocation.TOP_RIGHT) != null) {
            Assert.assertEquals(clockwiseNodes.get(2), testTile.getCorner(CornerLocation.TOP_RIGHT));
        }
        if (testTile.getEdge(EdgeLocation.RIGHT) != null) {
            Assert.assertEquals(clockwiseNodes.get(3), testTile.getEdge(EdgeLocation.RIGHT));
        }
        if (testTile.getCorner(CornerLocation.BOTTOM_RIGHT) != null) {
            Assert.assertEquals(clockwiseNodes.get(4), testTile.getCorner(CornerLocation.BOTTOM_RIGHT));
        }
        if (testTile.getEdge(EdgeLocation.BOTTOM) != null) {
            Assert.assertEquals(clockwiseNodes.get(5), testTile.getEdge(EdgeLocation.BOTTOM));
        }
        if (testTile.getCorner(CornerLocation.BOTTOM_LEFT) != null) {
            Assert.assertEquals(clockwiseNodes.get(6), testTile.getCorner(CornerLocation.BOTTOM_LEFT));
        }
        if (testTile.getEdge(EdgeLocation.LEFT) != null) {
            Assert.assertEquals(clockwiseNodes.get(7), testTile.getEdge(EdgeLocation.LEFT));
        }
    }

    @Test
    public void testGettingAdjacentTileSectionsShouldReturnCorrectTileSections() {
        //  |JLTTB--------------------JLTTB|
        //  |  JUNGLE    JUNGLE      X     |
        //  |  TRAIL     False      LAKE   |
        //  |  JUNGLE    TRAIL     JUNGLE  |
        //  |JLTTB--------------------JLTTB|
        // Lets get the top node, we should get the Lake, and one trail as adjacent

        TileSection jungle = testTile.getEdge(EdgeLocation.TOP).getTileSection();
        Assert.assertEquals(jungle.getTerrain(), Terrain.JUNGLE);  // Make sure we got the right one
        List<TileSection> tileSectionsAdjacent = testTile.getAdjacentTileSectionsForTileSection(jungle);
        Assert.assertEquals(2, tileSectionsAdjacent.size());  // Two adjacent ones
        TileSection lake = testTile.getEdge(EdgeLocation.RIGHT).getTileSection();
        TileSection trail = testTile.getEdge(EdgeLocation.LEFT).getTileSection();
        Assert.assertTrue(tileSectionsAdjacent.contains(lake) && tileSectionsAdjacent.contains(trail));
    }

    @Test
    public void testHasTigerReturnsAppropriateBoolean(){
        Assert.assertFalse(testTile.hasTiger());
        try {
            testTile.getTileSections().get(0).placeTiger(new Tiger("John", false));
        }
        catch(Exception e){}
        Assert.assertTrue(testTile.hasTiger());
    }

    @Test
    public void testServerLocationReturnsCorrectPoint(){
        Board board = new Board(7, testTile);
        TileFactory tileFactory = new TileFactory();
        Tile testTile2 = tileFactory.makeTile("JJJJ-");
        Tile testTile3 = tileFactory.makeTile("JJJJ-");
        Point testTileLocation = testTile.getLocation();
        try {
            board.place(testTile2, new Point(testTileLocation.x, testTileLocation.y+1));
        }
        catch(Exception e){}
        try {
            board.place(testTile3, new Point(testTileLocation.x+1, testTileLocation.y+1));
        }
        catch(Exception e){}
        Point testTile2ServerLocation = testTile2.getServerLocation();
        Assert.assertEquals(testTile2ServerLocation.x, 0);
        Assert.assertEquals(testTile2ServerLocation.y, 1);
        Point testTile3ServerLocation = testTile3.getServerLocation();
        Assert.assertEquals(testTile3ServerLocation.x, 1);
        Assert.assertEquals(testTile3ServerLocation.y, 1);
    }

    @Test
    public void testGetTigerZoneReturnsTheCorrectNumberToPlaceATigerOn(){
        int nodeToPlaceTigerForTileSection1 = testTile.getTigerZone(testTile.getTileSections().get(0));
        Assert.assertEquals(nodeToPlaceTigerForTileSection1, 1);
    }
}