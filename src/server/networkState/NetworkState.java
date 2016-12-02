package server.networkState;

import exceptions.ParseFailureException;

/*

Network state is used as an alternative to brute forcing all server input over a switch statement
and reducing the total number of checks needed by tracking the current state that the system is in
and processing the input according to that state. The context keeps track of the current state and
each state also tracks the previous state so if the input is not found there, it will revert to the
previous state and attempt to parse the input there. It can not go to a deeper state without
validating a parsable input. If no state is able to parse the input, a parse exception is thrown.

 */

public abstract class NetworkState {
    NetworkContext context;

    NetworkState(NetworkContext context) {
        this.context = context;
    }

    public abstract String processInput(String input) throws ParseFailureException;
    public abstract NetworkState returnState();
}