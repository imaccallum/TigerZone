package game;

import controller.AIController;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.player.Player;
import exceptions.BadServerInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;


import java.net.*;


public class Main {

    public static void main(String[] args) throws Exception {

        /*
        if (args.length != 2) {
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
            // server connection successful

            String input;  // string received from server
            String output;    // string client gives

            NetworkContext context = new NetworkContext();
            context.setTournamentPassword(tournamentPassword);
            context.setUsername(username);
            context.setPassword(password);
            context.setProtocol(new TournamentProtocol(context));


            while ((input = in.readLine()) != null) {
                System.out.println("SERVER: " + input);

                // Process input using current protocol and formulate response
                NetworkProtocol protocol = context.getProtocol();
                output = protocol.processInput(input);

                if (output != null) {
                    System.out.println("CLIENT: " + output); // print client response
                    out.println(output); // send message to server
                } else {
                    break;
                }
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

*/











            //region deckArray
        String[] myarray = {"JJJJ-",
                "JJJJX", "JJJJX", "JJJJX", "JJJJX",
                "JJTJX", "JJTJX",
                "TTTT-",
                "TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-",
                "TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-",
                "TJTT-","TJTT-","TJTT-","TJTT-",
                "LLLL-",
                "JLLL-","JLLL-","JLLL-","JLLL-",
                "LLJJ-","LLJJ-","LLJJ-","LLJJ-","LLJJ-",
                "JLJL-","JLJL-","JLJL-",
                "LJLJ-","LJLJ-","LJLJ-",
                "LJJJ-","LJJJ-","LJJJ-","LJJJ-","LJJJ-",
                "JLLJ-","JLLJ-",
                "TLJT-",
                "TLJTP","TLJTP",
                "JLTT-",
                "JLTTB","JLTTB",
                "TLTJ-","TLTJ-","TLTJ-",       // WEIRD # FORMAT 1 OR 3?
                "TLTJD","TLTJD",
                "TLLL-",
                "TLTT-",
                "TLTTP","TLTTP",
                "TLLT-","TLLT-","TLLT-",
                "TLLTB","TLLTB",
                "LJTJ-",
                "LJTJD","LJTJD",
                "TLLLC", "TLLLC"};
        //endregion

//        Character[] testDeck = {'b', 'a', 'a'};
        List<String> stringList = Arrays.asList(myarray);
        Collections.shuffle(stringList);

        TileFactory f = new TileFactory();
        Stack<Tile> deck = new Stack<>();

        for (String s: stringList) {
            Tile t = f.makeTile(s);
            deck.push(t);
        }

        GameInteractor game = new GameInteractor(deck);

        Player p0 = new Player("Player 0", new AIController(game, "Player 0"));
        Player p1 = new Player("Player 1", new AIController(game, "Player 1"));
        game.addPlayer(p0);
        game.addPlayer(p1);

        game.init();
        game.playGame();
        game.log();

//        while(!deck.empty())
//        {
//            Tile t = deck.pop();
//            Board board = gm.getBoard();
////            System.out.println(t);
//
//            List<LocationAndOrientation>  tileOptions = board.findValidTilePlacements(t);
//            if(tileOptions.size() > 0) {
//                int random = (int) (Math.random() * tileOptions.size());
//                LocationAndOrientation optimalPlacement = tileOptions.get(random);
//                System.out.println("Inserted " + t.getType() + " @ " + optimalPlacement.getLocation() + " with orientation " + optimalPlacement.getOrientation());
//                t.rotateCounterClockwise(optimalPlacement.getOrientation());
//                if(Math.random() > .9 && p1.hasRemainingTigers()){
//                    t.getTileSections().get(0).placeTiger(new Tiger(p1.getName(), false));
//                }
//                board.place(t, optimalPlacement.getLocation());
//            }
//            else {
//                System.out.println("No valid moves, discarding tile.");
//            }
//            if(deck.size() == 0) {
//                System.out.println(board.toString());
//                board.log();
//                System.out.println(board.getNumTiles());
//            }
//
//            //         System.out.println("------------------------");
//
//        }
    }

    public void startMatch(BufferedReader in, PrintWriter out) {
        // Parse input for match start
        // Initialize GameManagers
        // Start two threads

    }

    public void playGame(BufferedReader in, PrintWriter out, GameInteractor gameInteractor) {
        // Setup relationships, setup AI
        gameInteractor.playGame();
    }
}
