package wrappers.networkState;

import exceptions.ParseFailureException;
import game.LocationAndOrientation;
import javafx.util.Pair;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;

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


//        // Server tells us remaining tiles
//        try {
//
//        } catch(ParseFailureException e) {}
//
//
//        // Server tells us when match begins
//        try {
//
//        } catch(ParseFailureException e) {}

        return null;
    }

    public NetworkState returnState() {
        return new RoundState(context);
    }

}
