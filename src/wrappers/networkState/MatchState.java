package wrappers.networkState;

import exceptions.ParseFailureException;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;

public class MatchState extends NetworkState {

    public MatchState(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws ParseFailureException {
        ProtocolMessageParser parser = new ProtocolMessageParser();
        ProtocolMessageBuilder builder = new ProtocolMessageBuilder();

        return null;
    }

    public NetworkState returnState() {
        return new RoundState(context);
    }

}
