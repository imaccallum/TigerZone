package server.networkState;

import exceptions.ParseFailureException;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;

public class AuthenticationState extends NetworkState {

    public AuthenticationState(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws ParseFailureException {
        System.out.println("STATE: AUTHENTICATION -- " + input);
        ProtocolMessageParser parser = new ProtocolMessageParser();
        ProtocolMessageBuilder builder = new ProtocolMessageBuilder();

        String pid;

        if (parser.parseIsSparta(input)) {
            // Join tournament
            return builder.joinBuilder(context.getTournamentPassword());
        } else if (parser.parseIsHello(input)) {
            // Identify yourself
            return builder.identityBuilder(context.getUsername(), context.getPassword());
        } else if ((pid = parser.parseWelcomePID(input)) != null) {
            // Save PID to context, wait for challenge
            context.setPid(pid);
            context.setState(new ChallengeState(context));
            return null;
        } else if (parser.parseGoodbye(input)) {
            context.setShouldReturn(true);
            return null;
        } else {
            throw new ParseFailureException(input);
        }
    }

    public NetworkState returnState() {
        return null;
    }
}
