package game;

import exceptions.ParseFailureException;
import server.networkState.AuthenticationState;
import server.networkState.NetworkContext;
import server.networkState.NetworkState;

import java.io.*;
import java.net.*;

public class Main {

    public static void main(String[] args) throws Exception {

        args = new String[]{"192.168.1.37", "4444", "TIGERZONE", "TEAMC", "IAMC"};


        if (args.length != 5) {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        // dave sample: java AwesomeClient 129.68.1.88 3333 PersiaRocks! Red Obiwan77

        int portNumber = Integer.parseInt(args[1]);

        String hostName = args[0];
        String tournamentPassword = args[2];
        String username = args[3];
        String password = args[4];

        try (
                Socket socket = new Socket(hostName, portNumber); // create the socket within client to communicate with the server
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        ) {

            NetworkContext context = new NetworkContext(in, out);
            context.setTournamentPassword(tournamentPassword);
            context.setUsername(username);
            context.setPassword(password);
            context.setState(new AuthenticationState(context));;

            // server connection successful

            String input;  // string received from server
            String output = "";    // string client gives

            while ((input = in.readLine()) != null) {
                // Process input using current state and formulate response
                NetworkState currentState = context.getState();

                System.out.println("SERVER: " + input);

                try {
                    output = currentState.processInput(input);
                } catch (ParseFailureException e) {
                    System.err.println("Failed to parse: " + input);
                    break;
                }

                if (output != null) {
                    System.out.println("CLIENT: " + output); // print client response
                    out.println(output); // send message to server
                }

                if (context.shouldReturn()) break;
            }

        } catch (UnknownHostException e) {

            System.err.println("Host " + hostName + " unknown");
            System.exit(1);

        } catch (IOException e) {
            // if I/O connection fails for some reason
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
            // print appropriate error message, exit program
        }
    }
}
