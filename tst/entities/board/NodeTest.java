package entities.board;

import entities.overlay.TileSection;
import entities.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {
    Node testNode1;
    Node testNode2;
    Node testNode3;
    TileSection testTileSection1;
    TileSection testTileSection2;
    Tiger diegoTiger;

    @Before
    public void before() {
        testNode1 = new Node();
        testNode2 = new Node();
        testNode3 = new Node();
        testTileSection1 = new TileSection(Terrain.LAKE);
        testTileSection2 = new TileSection(Terrain.JUNGLE);
        diegoTiger = new Tiger("Diego");
    }

    @Test // Should not throw
    public void testIsTigerDisplayNodeReturnsFalseWhenNotAndTrueWhenIs() throws Exception {
        // has no tiger
        testTileSection1.addNodes(testNode1, testNode2, testNode3);
        for (Node node : testTileSection1.getNodes()) {
            Assert.assertFalse(node.isTigerDisplayNode());
        }

        int numberOfTigerDisplayNodes = 0;
        testTileSection1.placeTiger(diegoTiger);
        for (Node node : testTileSection1.getNodes()) {
            if (node.equals(testTileSection1.getTigerDisplayNode())) {
                Assert.assertTrue(node.isTigerDisplayNode());
                ++numberOfTigerDisplayNodes;
            }
            else {
                Assert.assertFalse(node.isTigerDisplayNode());
            }
        }
        Assert.assertEquals(1, numberOfTigerDisplayNodes);
    }
}
