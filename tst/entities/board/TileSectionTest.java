package entities.board;

import entities.overlay.TileSection;
import game.BadPlacementException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TileSectionTest {
    private Tile testTile;
    private List<TileSection> tileSections;
    private Tile testTile2;

    @Before
    public void setup() {
        TileFactory f = new TileFactory();
        testTile = f.makeTile('r');
        tileSections = testTile.getTileSections();
        testTile2 = f.makeTile('b');
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
        Assert.assertTrue(testTile2.getTileSections().get(1).hasOpenConnection());

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

        testTile2.setLeftTile(leftTile);
        testTile2.setRightTile(rightTile);
        testTile2.setTopTile(topTile);
        testTile2.setBottomTile(bottomTile);

        Assert.assertFalse(testTile2.getTileSections().get(1).hasOpenConnection());

        testTile.setLeftTile(leftTile);
        testTile.setRightTile(rightTile);
        testTile.setTopTile(topTile);
        testTile.setBottomTile(bottomTile);

        Assert.assertFalse(testTile.getTileSections().get(0).hasOpenConnection());
    }
}
