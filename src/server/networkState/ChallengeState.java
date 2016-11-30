package server.networkState;

import exceptions.ParseFailureException;
import javafx.util.Pair;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;

/**
 * Created by ianmaccallum on 11/28/16.
 */
public class ChallengeState extends NetworkState {

    public ChallengeState(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws ParseFailureException {

        ProtocolMessageParser parser = new ProtocolMessageParser();
        ProtocolMessageBuilder builder = new ProtocolMessageBuilder();

        // Received challenge, begin round
        try {
            Pair<String, Integer> pair = parser.parseNewChallenge(input);
            String cid = pair.getKey();
            int rounds = pair.getValue().intValue();
            context.setCid(cid);
            context.setRoundCount(rounds);
            context.setState(new RoundState(context));
            return null;
        } catch (ParseFailureException e) {

            // No challenge received
            if (parser.parseIsEndOfChallenges(input)) {
                // End of challenges, return to authentication protocol
                NetworkState oldState = returnState();
                context.setState(oldState);
                return null;
            } else if (parser.parseIsWaitForNextChallenge(input)) {
                // Do nothing, continue waiting
                return null;
            } else {

                NetworkState oldState = returnState();
                context.setState(oldState);
                return oldState.processInput(input);

            }
        }

    }

    public NetworkState returnState() {
        return new AuthenticationState(context);
    }
}
