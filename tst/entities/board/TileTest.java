package entities.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TileTest {
    private Tile leftAdjacentTile;
    private Tile rightAdjacentTile;
    private Tile topAdjacentTile;
    private Tile bottomAdjacentTile;
    private Tile testTile;

    @Before
    public void setup() {
        testTile = new Tile();
        leftAdjacentTile = new Tile();
        rightAdjacentTile = new Tile();
        bottomAdjacentTile = new Tile();
        topAdjacentTile = new Tile();
    }

    @Test
    public void testGetAndSetLeftAdjacentTile() {
        testTile.setLeftAdjacentTile(leftAdjacentTile);
        Assert.assertTrue(testTile.getLeftAdjacentTile().equals(leftAdjacentTile));
    }

    @Test
    public void testGetAndSetRightAdjacentTile() {
        testTile.setRightAdjacentTile(rightAdjacentTile);
        Assert.assertTrue(testTile.getRightAdjacentTile().equals(rightAdjacentTile));
    }

    @Test
    public void testGetAndSetTopAdjacentTile() {
        testTile.setTopAdjacentTile(topAdjacentTile);
        Assert.assertTrue(testTile.getTopAdjacentTile().equals(topAdjacentTile));
    }

    @Test
    public void testGetAndSetBottomAdjacentTile() {
        testTile.setBottomAdjacentTile(bottomAdjacentTile);
        Assert.assertTrue(testTile.getBottomAdjacentTile().equals(bottomAdjacentTile));
    }

    @Test
    public void testCorrectSectionForNodeNumber() {
        Tile tile = new Tile();
        section.addNodeNumbers(1,2,3);
        tile.addSections(section);
        Assert.assertTrue(tile.sectionForNodeNumber(1).equals(section));
        Assert.assertTrue(tile.sectionForNodeNumber(2).equals(section));
        Assert.assertTrue(tile.sectionForNodeNumber(3).equals(section));
        Assert.assertTrue(tile.sectionForNodeNumber(5) == null);
    }
}
