package server;

public class ServerMatchMessageHandler {
    private String gameId;
    private String serverInput;
    private String serverOutput;

    public ServerMatchMessageHandler() {}

    public void setGameId(String gameId) {
        this.gameId = gameId;
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
        String output = serverOutput;
        serverOutput = null;
        return output;
    }

    public synchronized void setServerOutput(String output) {
        serverOutput = output;
        notifyAll();
    }

    public String getGameId() {
        return gameId;
    }
}
