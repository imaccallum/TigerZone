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
    public void parseIsSparta() throws Exception {

    }

    @Test
    public void parseIsHello() throws Exception {

    }

    @Test
    public void parseWelcomePID() throws Exception {

    }

    @Test
    public void parseNewChallenge() throws Exception {
        String cid0 = "9282em8ms32";
        int rounds0 = 5;
        String input = "NEW CHALLENGE " + cid0 + " YOU WILL PLAY " + rounds0 + " MATCHES";

        Pair<String, Integer> pair = parser.parseNewChallenge(input);
        String cid1 = pair.getKey();
        int rounds1 = pair.getValue().intValue();

        System.out.println("CID " + cid1);
        System.out.println("Rounds " + rounds1);
        assertEquals(cid0, cid1);
        assertEquals(rounds0, rounds1);
    }

    @Test
    public void parseOpponentPID() throws Exception {
        String id = "12384843";
        String pid = parser.parseOpponentPID("YOUR OPPONENT IS PLAYER " + id);
        assertEquals(id, pid);
    }

    @Test
    public void parseRemainingTiles() throws Exception {
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