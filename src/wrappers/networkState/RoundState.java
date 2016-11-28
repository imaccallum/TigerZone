package wrappers.networkState;

import exceptions.ParseFailureException;
import javafx.util.Pair;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;

/**
 * Created by ianmaccallum on 11/28/16.
 */
public class RoundState extends NetworkState {

    public RoundState(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws ParseFailureException {

        ProtocolMessageParser parser = new ProtocolMessageParser();
        ProtocolMessageBuilder builder = new ProtocolMessageBuilder();

        // Begin round message
        try {
            Pair<String, Integer> pair = parser.parseBeginRound(input);
            String rid = pair.getKey();
            int rounds = pair.getValue().intValue();
            context.setRid(rid);
            context.setRoundCount(rounds);
            context.setState(new MatchState(context));
            return null;

        } catch (ParseFailureException e) {}

        try {
            
        } catch (ParseFailureException e) {}

        return null;
    }

    public NetworkState returnState() {
        return new ChallengeState(context);
    }
}
