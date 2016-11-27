package game;

import ai.AIManager;
import com.sun.org.apache.bcel.internal.generic.NEW;
import entities.board.Board;
import entities.board.Tiger;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.player.Player;
import exceptions.BadServerInputException;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;


import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



abstract class NetworkProtocol {

    NetworkContext context;

    NetworkProtocol(NetworkContext context) {
        this.context = context;
    }

    public String processInput(String input) throws BadServerInputException {
        throw new BadServerInputException("Must Override");
    }
}

class TournamentProtocol extends NetworkProtocol {
    TournamentProtocol(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws BadServerInputException {

        if (input.startsWith("THIS IS SPARTA!")) {

            // Switch to AuthenticationProtocol
            AuthenticationProtocol protocol = new AuthenticationProtocol(context);
            context.setProtocol(protocol);
            return protocol.processInput(input);

        } else if (input.equals("THANK YOU FOR PLAYING! GOODBYE")) {
            return null;
        }
        return "";
    }

}

class AuthenticationProtocol extends NetworkProtocol {

    public AuthenticationProtocol(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws BadServerInputException {

        ProtocolMessageBuilder messageBuilder = new ProtocolMessageBuilder();
        ProtocolMessageParser messageParser = new ProtocolMessageParser();
        String pid;

        if (input.equals("THIS IS SPARTA!")) {
            return messageBuilder.joinBuilder(context.getTournamentPassword());
        } else if (input.equals("HELLO!")) {
            return messageBuilder.identityBuilder(context.getUsername(), context.getPassword());
        } else if ((pid = messageParser.parseWelcomePID(input)) != null) {
            context.setProtocol(new ChallengeProtocol(context, pid));
            return null;
        } else {
            throw new BadServerInputException(input);
        }
    }
}

class ChallengeProtocol extends NetworkProtocol {
    String pid;

    public ChallengeProtocol(NetworkContext context, String pid) {
        super(context);
        this.pid = pid;
    }

    public String processInput(String input) throws BadServerInputException {

        Pattern p = Pattern.compile("NEW CHALLENGE .+ YOU WILL PLAY .+ MATCH.+");
        Matcher m = p.matcher(input);

        if (m.find()) {
            String pid = m.group(1);
            context.setProtocol(new ChallengeProtocol(context, pid));
        }

        return "";
    }
}

class RoundProtocol extends NetworkProtocol {
    int currentRound;
    int totalRounds;

    public RoundProtocol(NetworkContext context) {
        super(context);
    }
}

class MatchProtocol extends NetworkProtocol {


    public MatchProtocol(NetworkContext context) {
        super(context);
    }
}

class MoveProtocol extends NetworkProtocol {
    public MoveProtocol(NetworkContext context) {
        super(context);
    }
}

class NetworkContext {
    NetworkProtocol protocol;
    private String tournamentPassword;
    private String username;
    private String password;
    private String pid;
    private String cid;
    private String rid;
    private int round;
    private int roundCount;

    public NetworkProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(NetworkProtocol protocol) {
        this.protocol = protocol;
    }

    public String getTournamentPassword() {
        return tournamentPassword;
    }

    public void setTournamentPassword(String tournamentPassword) {
        this.tournamentPassword = tournamentPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }
}

class Challenge {

}

class Match {
    String pid, opid;
    Map<String, Game> games;


}

class Game {

}


public class Main {

    public static void main(String[] args) throws Exception {

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

        Player p0 = new Player("Player 0", new AIManager(game, "Player 0"));
        Player p1 = new Player("Player 1", new AIManager(game, "Player 1"));
        game.addPlayer(p0);
        game.addPlayer(p1);

        game.init();
        game.playGame();
        game.getBoard().log();

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
}
