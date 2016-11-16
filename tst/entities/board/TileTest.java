package entities.board;

import entities.overlay.TileSection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;

public class TileTest {
    private Tile testTile;

    @Before
    public void setup() {
        TileFactory f = new TileFactory();
        testTile = f.makeTile('r');
    }

    @Test
    public void testShouldContainCorrectAmountOfNodes(){
        int numberOfNodes = 0;
        for (TileSection tileSection: testTile.getTileSections()) {
            numberOfNodes+= tileSection.getNodes().size();
        }
        Assert.assertEquals(numberOfNodes,8);
    }

    @Test
    public void testShouldContainCorrectAmountOfSections() {
        int jungles = 0, lakes = 0, trail = 0;
        for (TileSection t: testTile.getTileSections()) {
            switch (t.getTerrain()){
                case JUNGLE: jungles++;
                    break;
                case LAKE: lakes++;
                    break;
                case TRAIL: trail++;
                    break;
            }
        }

        Assert.assertEquals(jungles, 2);
        Assert.assertEquals(lakes, 1);
        Assert.assertEquals(trail, 1);
        Assert.assertEquals(testTile.hasBoar(), false);
        Assert.assertEquals(testTile.hasDeer(), false);
        Assert.assertEquals(testTile.hasBuffalo(), true);
    }

    @Test
    public void testShouldContainCorrectArrayOfNodesAfterRotation() {
        testTile.rotateClockwise(1);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.LAKE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.TRAIL);

        testTile.rotateClockwise(1);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.LAKE);

        testTile.rotateClockwise(1);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.LAKE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.JUNGLE);

        testTile.rotateClockwise(1);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.TOP).getTileSection().getTerrain(), Terrain.JUNGLE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.RIGHT).getTileSection().getTerrain(), Terrain.LAKE);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.BOTTOM).getTileSection().getTerrain(), Terrain.TRAIL);
        Assert.assertEquals(testTile.getEdge(EdgeLocation.LEFT).getTileSection().getTerrain(), Terrain.TRAIL);
    }
}