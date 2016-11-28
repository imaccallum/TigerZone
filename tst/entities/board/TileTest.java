package entities.board;

import entities.overlay.TileSection;
import exceptions.BadPlacementException;
import exceptions.TigerAlreadyPlacedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

public class TileTest {
    private Tile testTile;

    @Before
    public void setup() {
        testTile = TileFactory.makeTile("JLTTB");
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

    @Test   //should not throw
    public void testHasTigerReturnsAppropriateBoolean() throws TigerAlreadyPlacedException{
        Assert.assertFalse(testTile.hasTiger());
        testTile.getTileSections().get(0).placeTiger(new Tiger("John", false));
        Assert.assertTrue(testTile.hasTiger());
    }

    @Test
    public void testServerLocationReturnsCorrectPoint() throws BadPlacementException{
        Board board = new Board(7, testTile);
        Tile testTile2 = TileFactory.makeTile("TJTJ-");
        Tile testTile3 = TileFactory.makeTile("TJTJ-");
        Point testTileLocation = testTile.getLocation();

        board.place(testTile2, new Point(testTileLocation.x, testTileLocation.y+1));
        board.place(testTile3, new Point(testTileLocation.x+1, testTileLocation.y+1));

        Point testTile2ServerLocation = testTile2.getServerLocation();
        Assert.assertEquals(testTile2ServerLocation.x, 0);
        Assert.assertEquals(testTile2ServerLocation.y, 1);
        Point testTile3ServerLocation = testTile3.getServerLocation();
        Assert.assertEquals(testTile3ServerLocation.x, 1);
        Assert.assertEquals(testTile3ServerLocation.y, 1);
    }

    @Test
    public void testGetTigerZoneReturnsTheCorrectNumberToPlaceATigerOn(){
        //bigger jungle
        Assert.assertEquals(1, testTile.getTigerZone(testTile.getTileSections().get(0)));
        //smaller jungle
        Assert.assertEquals(7, testTile.getTigerZone(testTile.getTileSections().get(1)));
        //lake
        Assert.assertEquals(6, testTile.getTigerZone(testTile.getTileSections().get(2)));
        //trail
        Assert.assertEquals(4, testTile.getTigerZone(testTile.getTileSections().get(3)));

        testTile.rotateCounterClockwise(1);
        //ORDER = bigger jungle     smaller jungle      lake        trail
        Assert.assertEquals(1, testTile.getTigerZone(testTile.getTileSections().get(0)));
        Assert.assertEquals(9, testTile.getTigerZone(testTile.getTileSections().get(1)));
        Assert.assertEquals(2, testTile.getTigerZone(testTile.getTileSections().get(2)));
        Assert.assertEquals(5, testTile.getTigerZone(testTile.getTileSections().get(3)));

        testTile.rotateCounterClockwise(1);
        //ORDER = bigger jungle     smaller jungle      lake        trail
        Assert.assertEquals(1, testTile.getTigerZone(testTile.getTileSections().get(0)));
        Assert.assertEquals(3, testTile.getTigerZone(testTile.getTileSections().get(1)));
        Assert.assertEquals(4, testTile.getTigerZone(testTile.getTileSections().get(2)));
        Assert.assertEquals(2, testTile.getTigerZone(testTile.getTileSections().get(3)));

        testTile.rotateCounterClockwise(1);
        //ORDER = bigger jungle     smaller jungle      lake        trail
        Assert.assertEquals(3, testTile.getTigerZone(testTile.getTileSections().get(0)));
        Assert.assertEquals(1, testTile.getTigerZone(testTile.getTileSections().get(1)));
        Assert.assertEquals(8, testTile.getTigerZone(testTile.getTileSections().get(2)));
        Assert.assertEquals(2, testTile.getTigerZone(testTile.getTileSections().get(3)));


        Tile allLake = TileFactory.makeTile("LLLL-");
        Assert.assertEquals(1, allLake.getTigerZone(allLake.getTileSections().get(0)));


        Tile TileTLLL = TileFactory.makeTile("TLLL-");
        //left jungle
        Assert.assertEquals(1, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(0)));
        //right jungle
        Assert.assertEquals(3, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(1)));
        //trail
        Assert.assertEquals(2, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(2)));
        //lake
        Assert.assertEquals(4, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(3)));

        TileTLLL.rotateCounterClockwise(1);
        //ORDER = left jungle       right jungle        trail       lake
        Assert.assertEquals(7, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(0)));
        Assert.assertEquals(1, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(1)));
        Assert.assertEquals(4, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(2)));
        Assert.assertEquals(2, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(3)));

        TileTLLL.rotateCounterClockwise(1);
        //ORDER = left jungle       right jungle        trail       lake
        Assert.assertEquals(9, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(0)));
        Assert.assertEquals(7, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(1)));
        Assert.assertEquals(8, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(2)));
        Assert.assertEquals(1, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(3)));

        TileTLLL.rotateCounterClockwise(1);
        //ORDER = left jungle       right jungle        trail       lake
        Assert.assertEquals(3, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(0)));
        Assert.assertEquals(9, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(1)));
        Assert.assertEquals(6, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(2)));
        Assert.assertEquals(1, TileTLLL.getTigerZone(TileTLLL.getTileSections().get(3)));


        Tile tileTTTT = TileFactory.makeTile("TTTT-");
        //top left jungle
        Assert.assertEquals(1, tileTTTT.getTigerZone(tileTTTT.getTileSections().get(0)));
        //top right jungle
        Assert.assertEquals(3, tileTTTT.getTigerZone(tileTTTT.getTileSections().get(1)));
        //bottom right jungle
        Assert.assertEquals(9, tileTTTT.getTigerZone(tileTTTT.getTileSections().get(2)));
        //bottom left jungle
        Assert.assertEquals(7, tileTTTT.getTigerZone(tileTTTT.getTileSections().get(3)));
        //top trail
        Assert.assertEquals(2, tileTTTT.getTigerZone(tileTTTT.getTileSections().get(4)));
        //right trail
        Assert.assertEquals(6, tileTTTT.getTigerZone(tileTTTT.getTileSections().get(5)));
        //bottom trail
        Assert.assertEquals(8, tileTTTT.getTigerZone(tileTTTT.getTileSections().get(6)));
        //left trail
        Assert.assertEquals(4, tileTTTT.getTigerZone(tileTTTT.getTileSections().get(7)));


        Tile tileJLJL = TileFactory.makeTile("JLJL-");
        //top jungle
        Assert.assertEquals(1, tileJLJL.getTigerZone(tileJLJL.getTileSections().get(0)));
        //bottom jungle
        Assert.assertEquals(7, tileJLJL.getTigerZone(tileJLJL.getTileSections().get(1)));
        //lake
        Assert.assertEquals(4, tileJLJL.getTigerZone(tileJLJL.getTileSections().get(2)));

        tileJLJL.rotateCounterClockwise(1);
        //ORDER = top jungle        bottom jungle       lake
        Assert.assertEquals(1, tileJLJL.getTigerZone(tileJLJL.getTileSections().get(0)));
        Assert.assertEquals(3, tileJLJL.getTigerZone(tileJLJL.getTileSections().get(1)));
        Assert.assertEquals(2, tileJLJL.getTigerZone(tileJLJL.getTileSections().get(2)));

        tileJLJL.rotateCounterClockwise(2);
        //ORDER = top jungle        bottom jungle       lake
        Assert.assertEquals(3, tileJLJL.getTigerZone(tileJLJL.getTileSections().get(0)));
        Assert.assertEquals(1, tileJLJL.getTigerZone(tileJLJL.getTileSections().get(1)));
        Assert.assertEquals(2, tileJLJL.getTigerZone(tileJLJL.getTileSections().get(2)));


        Tile tileLLJJ = TileFactory.makeTile("LLJJ-");
        //lake
        Assert.assertEquals(2, tileLLJJ.getTigerZone(tileLLJJ.getTileSections().get(0)));
        //jungle
        Assert.assertEquals(1, tileLLJJ.getTigerZone(tileLLJJ.getTileSections().get(1)));

        tileLLJJ.rotateCounterClockwise(1);
        //ORDER = lake      jungle
        Assert.assertEquals(1, tileLLJJ.getTigerZone(tileLLJJ.getTileSections().get(0)));
        Assert.assertEquals(3, tileLLJJ.getTigerZone(tileLLJJ.getTileSections().get(1)));

        tileLLJJ.rotateCounterClockwise(1);
        //ORDER = lake      jungle
        Assert.assertEquals(4, tileLLJJ.getTigerZone(tileLLJJ.getTileSections().get(0)));
        Assert.assertEquals(1, tileLLJJ.getTigerZone(tileLLJJ.getTileSections().get(1)));

        tileLLJJ.rotateCounterClockwise(1);
        //ORDER = lake      jungle
        Assert.assertEquals(6, tileLLJJ.getTigerZone(tileLLJJ.getTileSections().get(0)));
        Assert.assertEquals(1, tileLLJJ.getTigerZone(tileLLJJ.getTileSections().get(1)));

        Tile tileJJJJ = TileFactory.makeTile("JJJJ-");
        Assert.assertEquals(1, tileJJJJ.getTigerZone(tileJJJJ.getTileSections().get(0)));


        Tile tileTLLTB = TileFactory.makeTile("TLLTB");
        //lake
        Assert.assertEquals(6, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(0)));
        //trail
        Assert.assertEquals(2, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(1)));
        //small jungle
        Assert.assertEquals(1, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(2)));
        //big jungle
        Assert.assertEquals(3, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(3)));

        tileTLLTB.rotateCounterClockwise(1);
        //ORDER = lake      trail       small jungle        big jungle
        Assert.assertEquals(2, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(0)));
        Assert.assertEquals(4, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(1)));
        Assert.assertEquals(7, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(2)));
        Assert.assertEquals(1, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(3)));

        tileTLLTB.rotateCounterClockwise(1);
        //ORDER = lake      trail       small jungle        big jungle
        Assert.assertEquals(1, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(0)));
        Assert.assertEquals(5, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(1)));
        Assert.assertEquals(9, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(2)));
        Assert.assertEquals(3, tileTLLTB.getTigerZone(tileTLLTB.getTileSections().get(3)));
    }
}