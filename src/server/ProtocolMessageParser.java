package server;
import exceptions.ParseFailureException;
import game.LocationAndOrientation;
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

    public String parseWelcomePID(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("WELCOME .+ PLEASE WAIT FOR THE NEXT CHALLENGE");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String pid = m.group(0);
            return pid;
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }



    // MARK: - Challenge protocol parser
    public Pair<String, Integer> parseNewChallenge(String input) throws ParseFailureException {

        Pattern p = Pattern.compile("NEW CHALLENGE (.+) YOU WILL PLAY (\\d+) MATCH.+");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String cid = m.group(1);
            Integer rounds = Integer.parseInt(m.group(2));

            return new Pair(cid, rounds);
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

    public boolean parseIsEndOfChallenges(String input) {
        return input.equals("END OF CHALLENGES");
    }

    public boolean parseIsWaitForNextChallenge(String input) {
        return input.equals("PLEASE WAIT FOR THE NEXT CHALLENGE TO BEGIN");
    }



    // MARK: - Round protocol parser
    public Pair<String, Integer> parseBeginRound(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("BEGIN ROUND (.+) OF (\\d+)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String rid = m.group(1);
            Integer rounds = Integer.parseInt(m.group(2));

            return new Pair(rid, rounds);
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

    public Pair<String, Integer> parseEndRound(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("END OF ROUND (.+) OF (\\d+)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String rid = m.group(1);
            Integer rounds = Integer.parseInt(m.group(2));

            return new Pair(rid, rounds);
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

    public Pair<String, Integer> parseEndRoundAndWait(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("END OF ROUND (.+) OF (\\d+) PLEASE WAIT FOR THE NEXT MATCH");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String rid = m.group(1);
            Integer rounds = Integer.parseInt(m.group(2));

            return new Pair(rid, rounds);
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }



    // MARK: - Match protocol parser
    public String parseOpponentPID(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("YOUR OPPONENT IS PLAYER (.+)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String pid = m.group(1);
            return pid;
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

    public Pair<String, LocationAndOrientation> parseStartingTile(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("STARTING TILE IS (.+) AT (\\d+) (\\d+) (\\d+)");
        Matcher m = p.matcher(input);

        if (m.find()) {

            String tile = m.group(1);
            int x = Integer.parseInt(m.group(2));
            int y = Integer.parseInt(m.group(3));
            int orientation = Integer.parseInt(m.group(4));

            Point point = new Point(x, y);
            LocationAndOrientation locationAndOrientation = new LocationAndOrientation(point, orientation);

            return new Pair(tile, locationAndOrientation);
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

    public Pair<Integer, String[]> parseRemainingTiles(String input) throws ParseFailureException {

        Pattern p = Pattern.compile("THE REMAINING (\\d+) TILES ARE (\\[.+\\])");
        Matcher m = p.matcher(input);

        if (m.find()) {
            Integer count = Integer.parseInt(m.group(1));
            String arr = m.group(2);
            String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
            return new Pair(count, items);
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

    public Integer parseMatchBeginsPlanTime(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("MATCH BEGINS IN (\\d+) SECONDS");
        Matcher m = p.matcher(input);

        if (m.find()) {
            Integer time = Integer.parseInt(m.group(1));
            return time;
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

//    public String parseGameOver(String input) {
//
//    }
}
