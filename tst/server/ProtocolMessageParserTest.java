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


    // MARK: - Authentication protocol tests
    @Test
    public void parseIsSparta() throws Exception {
        boolean t = parser.parseIsSparta("THIS IS SPARTA!");
        assertTrue(t);
    }

    @Test
    public void parseIsHello() throws Exception {
        boolean t = parser.parseIsSparta("HELLO!");
        assertTrue(t);
    }

    @Test
    public void parseWelcomePID() throws Exception {
        String id = "12384843";
        String input = "WELCOME " + id + " PLEASE WAIT FOR THE NEXT CHALLENGE";
        String pid = parser.parseWelcomePID(input);
        assertEquals(id, pid);
    }



    // MARK: - Challenge protocol tests
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
    public void parseIsEndOfChallenges() throws Exception {
        String input = "END OF CHALLENGES";
        boolean t = parser.parseIsEndOfChallenges(input);
        assertTrue(t);
    }

    @Test
    public void parseIsWaitForNextChallenge() throws Exception {
        String input = "PLEASE WAIT FOR THE NEXT CHALLENGE TO BEGIN";
        boolean t = parser.parseIsWaitForNextChallenge(input);
        assertTrue(t);
    }



    // MARK - Round protocol tests
    @Test
    public void parseBeginRound() throws Exception {
        String rid0 = "s4d54rvf56";
        int rounds0 = 5;
        String input = "BEGIN ROUND " + rid0 + " OF " + rounds0;

        Pair<String, Integer> pair = parser.parseBeginRound(input);
        String rid1 = pair.getKey();
        int rounds1 = pair.getValue().intValue();

        assertEquals(rid0, rid1);
        assertEquals(rounds0, rounds1);
    }

    @Test
    public void parseEndRound() throws Exception {
        String rid0 = "j38duin2h82";
        int rounds0 = 5;
        String input = "END OF ROUND " + rid0 + " OF " + rounds0;

        Pair<String, Integer> pair = parser.parseEndRound(input);
        String rid1 = pair.getKey();
        int rounds1 = pair.getValue().intValue();

        assertEquals(rid0, rid1);
        assertEquals(rounds0, rounds1);
    }

    @Test
    public void parseEndRoundAndWait() throws Exception {
        String rid0 = "930euwjdmi";
        int rounds0 = 5;
        String input = "END OF ROUND " + rid0 + " OF " + rounds0 + " PLEASE WAIT FOR THE NEXT MATCH";

        Pair<String, Integer> pair = parser.parseEndRoundAndWait(input);
        String rid1 = pair.getKey();
        int rounds1 = pair.getValue().intValue();

        assertEquals(rid0, rid1);
        assertEquals(rounds0, rounds1);
    }




    // MARK: - Match protocol tests
    @Test
    public void parseOpponentPID() throws Exception {
        String id = "12384843";
        String pid = parser.parseOpponentPID("YOUR OPPONENT IS PLAYER " + id);
        assertEquals(id, pid);
    }

    @Test
    public void parseStartingTile() throws Exception {
        parser.parseStartingTile();
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


    @Test
    public void parseRemainingTiles1() throws Exception {

    }

    @Test
    public void parseMatchBeginsPlanTime() throws Exception {

    }

    @Test
    public void parseGameOver() throws Exception {

    }

    @Test
    public void parseBeginTurn() throws Exception {

    }

    @Test
    public void parseConfirmMove() throws Exception {

    }

    @Test
    public void parseMove() throws Exception {

    }

    @Test
    public void parseTigerZone() throws Exception {

    }





}