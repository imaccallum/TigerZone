package server;

import java.util.LinkedList;
import java.util.Queue;

public class ServerMatchMessageHandler {
    private String gameId;
    private Queue<String> serverInput;
    private Queue<String> serverOutput;

    public ServerMatchMessageHandler() {
        serverInput = new LinkedList<>();
        serverOutput = new LinkedList<>();
    }

    public synchronized String getServerInput() throws InterruptedException {
        while (serverInput.isEmpty()) {
            wait();
        }
        return serverInput.remove();
    }

    public synchronized void addServerInput(String input) {
        serverInput.add(input);
        notifyAll();
    }

    public synchronized String getServerOutput() throws InterruptedException {
        while (serverOutput.isEmpty()) {
            wait();
        }
        return serverOutput.remove();
    }

    public synchronized void addServerOutput(String output) {
        serverOutput.add(output);
        notifyAll();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
