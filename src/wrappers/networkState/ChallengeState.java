package wrappers.networkState;

import exceptions.ParseFailureException;

/**
 * Created by ianmaccallum on 11/28/16.
 */
public class ChallengeState extends NetworkState {

    public ChallengeState(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws ParseFailureException {
        return null;
    }

    public NetworkState returnState() {
        return new AuthenticationState(context);
    }
}
