package server;
import javafx.util.Pair;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolMessageParser {

    // MARK: - Authentication protocol parser
    public boolean parseIsSparta(String input) {
        return input.equals("THIS IS SPARTA!");
    }

    public boolean parseIsHello(String input) {
        return input.equals("HELLO!");
    }

    public String parseWelcomePID(String input) {
        Pattern p = Pattern.compile("WELCOME .+ PLEASE WAIT FOR THE NEXT CHALLENGE");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String pid = m.group(0);
            return pid;
        } else {
            return null;
        }
    }



    // MARK: - Challenge protocol parser
    public Pair<String, Integer> parseNewChallenge(String input) {

        Pattern p = Pattern.compile("NEW CHALLENGE (.+) YOU WILL PLAY (\\d+) MATCH.+");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String cid = m.group(1);
            Integer rounds = Integer.parseInt(m.group(2));

            return new Pair(cid, rounds);
        } else {
            return null;
        }
    }

    public boolean parseIsEndOfChallenges(String input) {
        return input.equals("END OF CHALLENGES");
    }

    public boolean parseIsWaitForNextChallenge(String input) {
        return input.equals("PLEASE WAIT FOR THE NEXT CHALLENGE TO BEGIN");
    }



    // MARK: - Round protocol parser
    public Pair<String, Integer> parseBeginRound(String input) {
        Pattern p = Pattern.compile("BEGIN ROUND (.+) OF (\\d+)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String rid = m.group(1);
            Integer rounds = Integer.parseInt(m.group(2));

            return new Pair(rid, rounds);
        } else {
            return null;
        }
    }




    // MARK: - Match protocol parser



    public String parseOpponentPID(String input) {
        Pattern p = Pattern.compile("YOUR OPPONENT IS PLAYER (.+)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String pid = m.group(1);
            return pid;
        } else {
            return null;
        }
    }

    public Pair<Integer, String[]> parseRemainingTiles(String input) {

        Pattern p = Pattern.compile("THE REMAINING (\\d+) TILES ARE (\\[.+\\])");
        Matcher m = p.matcher(input);

        if (m.find()) {
            Integer count = Integer.parseInt(m.group(1));
            String arr = m.group(2);
            String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
            return new Pair(count, items);
        } else {
            return null;
        }
    }
}
