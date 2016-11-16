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

        Tile leftTile = new Tile();
        leftTile.setEdge(new Node(), 0);
        leftTile.setEdge(new Node(), 2);
        leftTile.setEdge(new Node(), 1);
        Tile leftTopTile = new Tile();
        leftTopTile.setEdge(new Node(), 2);
        leftTile.setTopTile(leftTopTile);
        Tile leftBottomTile = new Tile();
        leftBottomTile.setEdge(new Node(), 0);
        leftTile.setBottomTile(leftBottomTile);

        Tile rightTile = new Tile();
        rightTile.setEdge(new Node(), 0);
        rightTile.setEdge(new Node(), 2);
        rightTile.setEdge(new Node(), 3);
        Tile rightTopTile = new Tile();
        rightTopTile.setEdge(new Node(), 2);
        rightTile.setTopTile(rightTopTile);
        Tile rightBottomTile = new Tile();
        rightBottomTile.setEdge(new Node(), 0);
        rightTile.setBottomTile(rightBottomTile);

        Tile topTile = new Tile();
        topTile.setEdge(new Node(), 2);
        Tile bottomTile = new Tile();
        bottomTile.setEdge(new Node(), 0);

        testTileB.setLeftTile(leftTile);
        testTileB.setRightTile(rightTile);
        testTileB.setTopTile(topTile);
        testTileB.setBottomTile(bottomTile);

        Assert.assertFalse(testTileB.getTileSections().get(1).hasOpenConnection());

        testTileR.setLeftTile(leftTile);
        testTileR.setRightTile(rightTile);
        testTileR.setTopTile(topTile);
        testTileR.setBottomTile(bottomTile);

        Assert.assertFalse(testTileR.getTileSections().get(0).hasOpenConnection());

        testTileD.setLeftTile(testTileD2);
        testTileD.setRightTile(testTileD2);
        testTileD.setTopTile(testTileD2);
        testTileD.setBottomTile(testTileD2);

        Assert.assertFalse(testTileD.getTileSections().get(0).hasOpenConnection());
    }
}
