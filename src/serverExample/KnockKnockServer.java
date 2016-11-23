package serverExample;

import java.net.*;
import java.io.*;

public class KnockKnockServer {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }
        // this'll run if you just try to run the main() from IntelliJ
        // need to run using a command prompt

        int portNumber = Integer.parseInt(args[0]);
        // get the int from running "java KnockKnockServer <int>" in the command prompt

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                // create a new ServerSocket to listen on a specific port (number from being saved earlier)
                // ServerSocket is a system-independent implementation of the server side of a client/server socket
                Socket clientSocket = serverSocket.accept();
                // attempts a connection from a client
                // .accept() waits until a client starts up and requests a connection
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                // clientSocket is connected to the same local port
                // its remote address and remote port are set to that of the client
        ) {
            //once server successfully established a connection with a client
            String inputLine, outputLine;

            // Initiate conversation with client
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            outputLine = kkp.processInput(null);
            //returns message of "Knock Knock!" since input is null (so state = WAITING in kkp)
            out.println(outputLine);
            // get the socket's input and output stream and open readers and writers on them
            // initiates communication with the client by writing to the socket
            // basically sends first message ("Knock Knock!") to client

            while ((inputLine = in.readLine()) != null) {
                // .readLine() waits for client to type something into the socket
                // reading from socket (while input from client isn't null)
                outputLine = kkp.processInput(inputLine);
                // process input using KnockKnockProtocol class (returns the message to be sent to client)
                out.println(outputLine);    // sends the message to the client
                if (outputLine.equals("Bye."))  // will break if client puts "n" when asked if they want another joke
                    break;
            }
            // communicates with the client by reading from and writing to the socket
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
            //if the part in try's parameters fails, give an error message
        }
    }
}