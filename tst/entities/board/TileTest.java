package entities.board;

import entities.overlay.TileSection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TileTest {
    private Tile testTile;

    @Before
    public void setup() {
        TileFactory factory = new TileFactory();
        testTile = factory.makeTile("JLTTB");
    }

    @Test
    public void testShouldContainCorrectEdgesAfterRotation() {
        testTile.rotateCounterClockwise(1);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.LAKE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.TRAIL);

        testTile.rotateCounterClockwise(1);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.LAKE);

        testTile.rotateCounterClockwise(1);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.LAKE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);

        testTile.rotateCounterClockwise(1);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.LAKE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.TRAIL);
    }

    @Test
    public void testShouldContainCorrectCornersAfterRotation() {
        testTile.rotateCounterClockwise(1);
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertNull(testTile.getCorner(CornerLocation.BOTTOM_RIGHT));
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);

        testTile.rotateCounterClockwise(1);
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertNull(testTile.getCorner(CornerLocation.BOTTOM_LEFT));

        testTile.rotateCounterClockwise(1);
        Assert.assertNull(testTile.getCorner(CornerLocation.TOP_LEFT));
        Assert.assertEquals(testTile.getCorner(CornerLocation.TOP_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getCorner(CornerLocation.BOTTOM_LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);

        testTile.rotateCounterClockwise(1);
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
}