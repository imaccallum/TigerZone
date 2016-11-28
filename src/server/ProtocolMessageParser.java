package server;
import entities.board.Placement;
import exceptions.ParseFailureException;
import game.LocationAndOrientation;
import javafx.util.Pair;
import wrappers.*;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolMessageParser {

    // MARK: - Other parsers
    public String parseGID(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("GAME (.+) ");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String gid = m.group(1);
            return gid;
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

    public boolean parseGoodbye(String input) {
        return input.equals("THANK YOU FOR PLAYING! GOODBYE");
    }



    // MARK: - Authentication protocol parser
    public boolean parseIsSparta(String input) {
        return input.equals("THIS IS SPARTA!");
    }

    public boolean parseIsHello(String input) {
        return input.equals("HELLO!");
    }

    public String parseWelcomePID(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("WELCOME (.+) PLEASE WAIT FOR THE NEXT CHALLENGE");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String pid = m.group(1);
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
            String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").split(" ");
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

    public GameOverWrapper parseGameOver(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("GAME (.+) OVER PLAYER (.+) (\\d+) PLAYER (.+) (\\d+)");
        Matcher m = p.matcher(input);

        if (m.find()) {

            String gid = m.group(1);
            String pid0 = m.group(2);
            String pid1 = m.group(4);

            int score0 = Integer.parseInt(m.group(3));
            int score1 = Integer.parseInt(m.group(5));

            return new GameOverWrapper(gid, pid0, score0, pid1, score1);
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }



    // MARK: - Move protocol parser
    public BeginTurnWrapper parseBeginTurn(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("MAKE YOUR MOVE IN GAME (.+) WITHIN (\\d+) SECONDS?: MOVE (\\d+) PLACE (.+)");
        Matcher m = p.matcher(input);

        if (m.find()) {

            String gid = m.group(1);
            int time = Integer.parseInt(m.group(2));
            int moveNumber = Integer.parseInt(m.group(3));
            String tile = m.group(4);

            return new BeginTurnWrapper(gid, time, moveNumber, tile);
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

    public ConfirmedMoveWrapper parseConfirmMove(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("GAME (.+) MOVE (\\d+) PLAYER (\\S+) (.+)");
        Matcher m = p.matcher(input);

        if (m.find()) {

            String gid = m.group(1);
            int moveNumber = Integer.parseInt(m.group(2));
            String pid = m.group(3);
            String suffix = m.group(4);

            ConfirmedMoveWrapper wrapper = new ConfirmedMoveWrapper(gid, moveNumber, pid);

            // Check forfeit
            try {
                String forfeitMessage = parseForfeit(suffix);
                wrapper.setForfeitMessage(forfeitMessage);
                wrapper.setHasForfeited(true);
                return wrapper;

            } catch (ParseFailureException e) {}

            // Check placement move
            try {
                PlacementMoveWrapper move = parsePlacementMove(suffix);
                wrapper.setPlacementMove(move);
                wrapper.setIsPlacementMove(true);
                return wrapper;
            } catch (ParseFailureException e) {}

            // Check nonplacement move
            try {
                NonplacementMoveWrapper move = parseNonplacementMove(suffix);
                wrapper.setNonplacementMove(move);
                wrapper.setIsPlacementMove(false);
                return wrapper;
            } catch (ParseFailureException e) {}
        }

        throw new ParseFailureException("Failed to parse: " + input);
    }

    public String parseForfeit(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("FORFEITED: (.+)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String error = m.group(1);
            return error;
        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }



    public PlacementMoveWrapper parsePlacementMove(String input) throws ParseFailureException {
        Pattern p0 = Pattern.compile("PLACED (.+) AT (\\d+) (\\d+) (\\d+) (.+)");
        Matcher m0 = p0.matcher(input);

        if (m0.find()) {

            String tile = m0.group(1);
            int x = Integer.parseInt(m0.group(2));
            int y = Integer.parseInt(m0.group(3));
            int orientation = Integer.parseInt(m0.group(4));
            String placement = m0.group(5);

            Point point = new Point(x, y);
            PlacementMoveWrapper wrapper = new PlacementMoveWrapper(tile, point, orientation);

            if (placement.startsWith("TIGER ")) {
                wrapper.setPlacedObject(Placement.TIGER);

                int zone = parseTigerZone(placement);
                wrapper.setZone(zone);

            } else if (placement.equals("CROCODILE")) {
                wrapper.setPlacedObject(Placement.CROCODILE);
            } else {
                wrapper.setPlacedObject(Placement.NONE);
            }

            return wrapper;
        }

            throw new ParseFailureException("Failed to parse: " + input);
    }

    public NonplacementMoveWrapper parseNonplacementMove(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("TILE (.+) UNPLACEABLE (.+)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String tile = m.group(1);
            String suffix = m.group(2);

            NonplacementMoveWrapper wrapper = new NonplacementMoveWrapper(tile);

            if (suffix.equals("PASSED")) {
                wrapper.setType(UnplaceableType.PASSED);
            } else if (suffix.startsWith("RETRIEVED TIGER AT")) {
                wrapper.setType(UnplaceableType.RETRIEVED_TIGER);

                Pattern p0 = Pattern.compile("RETRIEVED TIGER AT (\\d+) (\\d+)");
                Matcher m0 = p0.matcher(suffix);

                if (m0.find()) {
                    int x = Integer.parseInt(m0.group(1));
                    int y = Integer.parseInt(m0.group(2));
                    Point location = new Point(x, y);
                    wrapper.setTigerLocation(location);
                } else {
                    throw new ParseFailureException("Failed to parse: " + input);
                }

            } else if (suffix.startsWith("ADDED ANOTHER TIGER")) {
                wrapper.setType(UnplaceableType.ADDED_TIGER);

                Pattern p1 = Pattern.compile("ADDED ANOTHER TIGER TO (\\d+) (\\d+)");
                Matcher m1 = p1.matcher(suffix);

                if (m1.find()) {
                    int x = Integer.parseInt(m1.group(1));
                    int y = Integer.parseInt(m1.group(2));
                    Point location = new Point(x, y);
                    wrapper.setTigerLocation(location);
                } else {
                    throw new ParseFailureException("Failed to parse: " + input);
                }

            }
            return wrapper;

        } else {
            throw new ParseFailureException("Failed to parse: " + input);
        }
    }

    public int parseTigerZone(String input) throws ParseFailureException {
        Pattern p = Pattern.compile("TIGER (\\d+)");
        Matcher m = p.matcher(input);

        if (m.find()) {
            int zone = Integer.parseInt(m.group(1));
            return zone;
        }

        throw new ParseFailureException("Failed to parse: " + input);
    }
}
