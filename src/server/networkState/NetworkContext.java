package server.networkState;

import controller.AIController;
import controller.AIInterface;
import entities.board.Tile;
import entities.board.TileFactory;
import exceptions.ParseFailureException;
import game.GameInteractor;
import game.MessageOutputRunner;
import game.Player;
import javafx.util.Pair;
import server.ProtocolMessageParser;
import server.ServerMatchMessageHandler;
import wrappers.BeginTurnWrapper;
import wrappers.ConfirmedMoveWrapper;
import wrappers.GameOverWrapper;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ianmaccallum on 11/28/16.
 */
public class NetworkContext {

    private NetworkState state;
    private String tournamentPassword;
    private String username;
    private String password;
    private String pid;
    private String opid;
    private String cid;
    private String rid;
    private int round;
    private int roundCount;
    private boolean shouldReturn = false;

    private String startingTile;
    private Point startingTileLocation;
    private int startingTileOrientation;

    private int remainingTileCount;
    private String[] remainingTiles;

    private BufferedReader in;
    private PrintWriter out;

    private Lock mutex = new ReentrantLock();


    public NetworkContext(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    public Pair<GameOverWrapper, GameOverWrapper> startMatch() {
        Tile firstTile = TileFactory.makeTile(startingTile);
        Tile secondTile = TileFactory.makeTile(startingTile);

        String gameId1 = null;
        String gameId2 = null;

        System.out.println(remainingTileCount);

        GameInteractor gameOne = new GameInteractor(firstTile, remainingTileCount + 1);
        GameInteractor gameTwo = new GameInteractor(secondTile, remainingTileCount + 1);

        gameOne.addPlayer(new Player(pid));
        gameOne.addPlayer(new Player(opid));

        gameTwo.addPlayer(new Player(pid));
        gameTwo.addPlayer(new Player(opid));

        AIInterface ai1 = new AIController(gameOne, pid);
        AIInterface ai2 = new AIController(gameTwo, pid);

        gameOne.setAiNotifier(ai1);
        gameTwo.setAiNotifier(ai2);

        ProtocolMessageParser parser = new ProtocolMessageParser();
        boolean gameOneDone = false;
        boolean gameTwoDone = false;

        while (true) {
            try {
                String serverInput = in.readLine();
                String serverOutput;

                try {
                    GameOverWrapper gameOver1 = parser.parseGameOver(serverInput);
                    GameOverWrapper gameOver2 = parser.parseGameOver(in.readLine());
                    return new Pair<>(gameOver1, gameOver2);
                }
                catch (ParseFailureException parseException) {
                    // continue
                }

                System.out.println("SERVER: " + serverInput);
                BeginTurnWrapper beginTurn = parser.parseBeginTurn(serverInput);
                String gameId = beginTurn.getGid();

                if (gameId1 == null) {
                    gameId1 = gameId;
                    gameOne.setGameId(gameId1);
                }

                if (gameId.equals(gameId1)) {
                    serverOutput = gameOne.decideTurn(serverInput);
                }
                else if (gameId.equals(gameId2)) {
                    serverOutput = gameOne.decideTurn(serverInput);
                }
                else {
                    System.err.println("Invalid game Id received " + gameId);
                    break;
                }
                out.println(serverOutput);

                for (int i = 0; i < 2; i++) {
                    serverInput = in.readLine();
                    System.out.println("SERVER: " + serverInput);
                    ConfirmedMoveWrapper confirmedMove = parser.parseConfirmMove(serverInput);
                    gameId = confirmedMove.getGid();
                    if (gameId1.equals(gameId)) {
                        gameOne.confirmMove(confirmedMove);
                    } else if (gameId2 == null) {
                        gameId2 = confirmedMove.getGid();
                        gameTwo.confirmMove(confirmedMove);
                    } else if (gameId.equals(gameId2)) {
                        gameTwo.confirmMove(confirmedMove);
                    } else {
                        System.err.println("Received confirmed move with bad GID: " + confirmedMove.getGid());
                    }

                }
            }
            catch (IOException exception) {
                System.err.println("Received IO exception");
            }
            catch (ParseFailureException exception) {
                System.err.println("Failed to parse the game id, exception: " + exception.getMessage());
                break;
            }
        }
        return new Pair<>(null, null);
    }

    public NetworkState getState() {
        return state;
    }

    public void setState(NetworkState state) {
        this.state = state;
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

    public String getOpid() {
        return opid;
    }

    public void setOpid(String opid) {
        this.opid = opid;
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

    public boolean shouldReturn() {
        return shouldReturn;
    }

    public void setShouldReturn(boolean shouldReturn) {
        this.shouldReturn = shouldReturn;
    }

    public String getStartingTile() {
        return startingTile;
    }

    public void setStartingTile(String startingTile) {
        this.startingTile = startingTile;
    }

    public Point getStartingTileLocation() {
        return startingTileLocation;
    }

    public void setStartingTileLocation(Point startingTileLocation) {
        this.startingTileLocation = startingTileLocation;
    }

    public int getStartingTileOrientation() {
        return startingTileOrientation;
    }

    public void setStartingTileOrientation(int startingTileOrientation) {
        this.startingTileOrientation = startingTileOrientation;
    }

    public int getRemainingTileCount() {
        return remainingTileCount;
    }

    public void setRemainingTileCount(int remainingTileCount) {
        this.remainingTileCount = remainingTileCount;
    }

    public String[] getRemainingTiles() {
        return remainingTiles;
    }

    public void setRemainingTiles(String[] remainingTiles) {
        this.remainingTiles = remainingTiles;
    }
}
