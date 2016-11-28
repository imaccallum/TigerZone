package server.networkState;

import exceptions.ParseFailureException;
import game.LocationAndOrientation;
import javafx.util.Pair;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;
import wrappers.GameOverWrapper;

import java.awt.*;

public class MatchState extends NetworkState {

    public MatchState(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws ParseFailureException {
        ProtocolMessageParser parser = new ProtocolMessageParser();
        ProtocolMessageBuilder builder = new ProtocolMessageBuilder();

        // Server tells us our oponent
        try {
            String opid = parser.parseOpponentPID(input);
            context.setOpid(opid);
            return null;

        } catch(ParseFailureException e) {}


        // Server tells us starting tile
        try {
            Pair<String, LocationAndOrientation> pair = parser.parseStartingTile(input);
            String tile = pair.getKey();
            LocationAndOrientation locAndOrient = pair.getValue();
            Point location = locAndOrient.getLocation();
            int orientation = locAndOrient.getOrientation();

            context.setStartingTile(tile);
            context.setStartingTileLocation(location);
            context.setStartingTileOrientation(orientation);

            return null;

        } catch(ParseFailureException e) {}


        // Server tells us remaining tiles
        try {
            Pair<Integer, String[]> pair = parser.parseRemainingTiles(input);
            int count = pair.getKey().intValue();
            String[] tiles = pair.getValue();

            context.setRemainingTileCount(count);
            context.setRemainingTiles(tiles);

            return null;

        } catch(ParseFailureException e) {}


        // Server tells us when match begins
        try {
            int time = parser.parseMatchBeginsPlanTime(input);
            Pair<GameOverWrapper, GameOverWrapper> pair = context.startMatch();

            // Both games are done so the game is over
            NetworkState oldState = returnState();
            context.setState(oldState);
            return null;

        } catch(ParseFailureException e) {}

        throw new ParseFailureException(input);
    }

    public NetworkState returnState() {
        return new RoundState(context);
    }
}
