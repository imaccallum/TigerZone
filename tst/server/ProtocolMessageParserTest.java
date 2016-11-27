package server;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ianmaccallum on 11/26/16.
 */
public class ProtocolMessageParserTest {

    ProtocolMessageParser parser;

    @Before
    public void setup(){
        parser = new ProtocolMessageParser();
    }

    @Test
    public void testParseWelcomePID() throws Exception {

    }

    @Test
    public void testParseOpponentPID() throws Exception {
        String id = "12384843";
        String pid = parser.parseOpponentPID("YOUR OPPONENT IS PLAYER " + id);
        assertEquals(id, pid);
    }

    @Test
    public void testParseRemainingTiles() throws Exception {
        String items = "[JJJJ-, JJJJX, JJJJX, JJJJX, JJJJX, JJTJX, JJTJX, TTTT-, TJTJ-,TJTJ-,TJTJ-,TJTJ-,TJTJ-,TJTJ-,TJTJ-,TJTJ-]";
        String input = "THE REMAINING 9 TILES ARE " + items;
        Pair<Integer, String[]> pair = parser.parseRemainingTiles(input);

        Integer count = pair.getKey();
        String[] arr = pair.getValue();

        System.out.println("Count " + count.intValue());
        for (String s : arr) {
            System.out.println(s);
        }

    }

}