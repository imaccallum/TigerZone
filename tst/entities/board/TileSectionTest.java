package entities.board;

import entities.overlay.TileSection;
import exceptions.BadPlacementException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TileSectionTest {
    private Tile testTileR;  //tile R, to test other aspects
    private List<TileSection> tileSections;
    private Tile testTileB; //tile B (den and field all around, to test Den
    private Tile testTileD; //Tile D (four way crossroad), to test corner connections
    private Tile testTileD2;    //Tile D again

    @Before
    public void setup() {
        TileFactory f = new TileFactory();
        testTileR = f.makeTile('r');
        tileSections = testTileR.getTileSections();
        testTileB = f.makeTile('b');
        testTileD = f.makeTile('d');
        testTileD2 = f.makeTile('d');
    }

    @Test
    public void testTileSectionsOfTestTileContainCorrectAmountOfNodes(){
        int nodecount = 0;
        for(TileSection tileSection : tileSections){
            nodecount += tileSection.getNodes().size();
        }
        Assert.assertEquals(nodecount, 8);
    }

    /*@Test
    public void testSetTiger(){
        tileSection.setTiger(new Tiger(new Player("John")));
        Assert.assertNotNull(tileSection.getTiger());
    }*/

    @Test
    public void testHasOpenConnection() throws BadPlacementException {
        Assert.assertTrue(tileSections.get(0).hasOpenConnection());
        Assert.assertTrue(testTileB.getTileSections().get(1).hasOpenConnection());
        TileSection section = new TileSection(Terrain.JUNGLE);
        section.addNodes(new Node(), new Node(), new Node());
        section.getNodes().get(0).setConnectedNode(new Node());
        Assert.assertTrue(section.hasOpenConnection());
        section.getNodes().get(1).setConnectedNode(new Node());
        Assert.assertTrue(section.hasOpenConnection());
        section.getNodes().get(2).setConnectedNode(new Node());
        Assert.assertFalse(section.hasOpenConnection());
    }
}
