package entities.board;

import entities.overlay.TileSection;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class TileTest {
    private Tile testTile;

    @Before
    public void setup() {
        TileFactory f = new TileFactory();
        testTile = f.makeTile('r');
    }

    @Test
    public void testShouldContainCorrectArrayOfNodesAfterRotation(){
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

    @Test
    public void testShouldContainCorrectAmountofSections() {
        int jungles=0, lakes=0, trails=0, den=0;
        for(TileSection t : testTile.getTileSections()){
            switch(t.getTerrain()){
                case DEN: den++;
                    break;
                case TRAIL: trails++;
                    break;
                case JUNGLE: jungles++;
                    break;
                case LAKE: lakes++;
                    break;
            }
        }
        Assert.assertEquals(2, jungles);
        Assert.assertEquals(1, trails);
        Assert.assertEquals(1, lakes);
        Assert.assertEquals(0, den);
        Assert.assertEquals(testTile.hasDeer(), false);
        Assert.assertEquals(testTile.hasBoar(), false);
        Assert.assertEquals(testTile.hasBuffalo(), true);
    }
}