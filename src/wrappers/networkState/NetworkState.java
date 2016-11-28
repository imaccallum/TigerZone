package wrappers.networkState;

import exceptions.ParseFailureException;

public abstract class NetworkState {
    NetworkContext context;

    NetworkState(NetworkContext context) {
        this.context = context;
    }

    public abstract String processInput(String input) throws ParseFailureException;
    public abstract NetworkState returnState();
}