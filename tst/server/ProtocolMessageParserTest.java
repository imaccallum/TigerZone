package server;

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

}