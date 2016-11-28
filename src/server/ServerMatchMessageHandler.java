package server;

import wrappers.NonplacementMoveWrapper;
import wrappers.PlacementMoveWrapper;

public class ServerMatchMessageHandler {
    private String gameId;
    private ProtocolMessageBuilder protocolMessageBuilder;
    private String serverInput;
    private String serverOutput;

    public ServerMatchMessageHandler(String gameId) {
        this.gameId = gameId;
        protocolMessageBuilder = new ProtocolMessageBuilder();
    }

    public void makeNonPlacementMove(NonplacementMoveWrapper move) {
        String serverOutput = protocolMessageBuilder.messageForNonplacementMove(move, gameId);
    }

    public void makePlacementMove(PlacementMoveWrapper move) {
        String serverOutput = protocolMessageBuilder.messageForMove(move, gameId);
    }

    public synchronized String getServerInput() throws InterruptedException {
        while (serverInput == null) {
            wait();
        }
        String input = serverInput;
        serverInput = null;
        return input;
    }

    public synchronized void setServerInput(String input) {
        serverInput = input;
        notifyAll();
    }

    public synchronized String getServerOutput() throws InterruptedException {
        while (serverOutput == null) {
            wait();
        }
        String input = serverInput;
        serverOutput = null;
        return input;
    }

    public synchronized void setServerOutput(String output) {
        serverOutput = output;
        notifyAll();
    }
}
