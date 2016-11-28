package game;

import server.ServerMatchMessageHandler;

import java.io.PrintWriter;
import java.util.concurrent.locks.Lock;

public class MessageOutputRunner implements Runnable {
    public static String terminationMessage = "TERMINATE MESSAGE OUTPUT TO SERVER";

    private Lock mutex;
    private PrintWriter serverOutput;
    private ServerMatchMessageHandler messageHandler;

    public MessageOutputRunner(Lock mutex, PrintWriter serverOutput, ServerMatchMessageHandler messageHandler) {
        this.mutex = mutex;
        this.serverOutput = serverOutput;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        while (true) {
            String serverOutputString = "";
            try {
                serverOutputString = messageHandler.getServerOutput();
            }
            catch (InterruptedException exception) {
                System.err.println("MessageOutputRunner was interrupted");
            }

            if (serverOutputString.equals(terminationMessage)) {
                break;
            }
            else {
                mutex.lock();
                System.out.println(serverOutputString);
                serverOutput.println("CLIENT: " + serverOutputString);
                mutex.unlock();
            }
        }
    }


}
