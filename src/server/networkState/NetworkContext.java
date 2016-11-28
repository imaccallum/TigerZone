package server.networkState;

import entities.board.Tile;
import entities.board.TileFactory;
import exceptions.ParseFailureException;
import game.GameInteractor;
import game.MessageOutputRunner;
import javafx.util.Pair;
import server.ProtocolMessageParser;
import server.ServerMatchMessageHandler;
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
        // TODO
        // Parse input for match start
        // Initialize GameManagers
        // Start two threads

        Tile firstTile = TileFactory.makeTile(startingTile);
        Tile secondTile = TileFactory.makeTile(startingTile);

        ServerMatchMessageHandler gameOneMessageHandler = new ServerMatchMessageHandler("A");
        ServerMatchMessageHandler gameTwoMessageHandler = new ServerMatchMessageHandler("B");

        GameInteractor gameInteractorOne = new GameInteractor(firstTile, remainingTileCount + 1);
        GameInteractor gameInteractorTwo = new GameInteractor(secondTile, remainingTileCount + 1);

        MessageOutputRunner gameOneMessageOutputRunner = new MessageOutputRunner(mutex, out, gameOneMessageHandler);
        MessageOutputRunner gameTwoMessageOutputRunner = new MessageOutputRunner(mutex, out, gameTwoMessageHandler);

        Thread matchGameOneThread = new Thread(gameInteractorOne);
        Thread matchGameTwoThread = new Thread(gameInteractorTwo);
        Thread gameOneMessageRunner = new Thread(gameOneMessageOutputRunner);
        Thread gameTwoMessageRunner = new Thread(gameTwoMessageOutputRunner);

        matchGameOneThread.run();
        matchGameTwoThread.run();
        gameOneMessageRunner.run();
        gameTwoMessageRunner.run();

        ProtocolMessageParser parser = new ProtocolMessageParser();
        GameOverWrapper firstGameOverWrapper = null;
        GameOverWrapper secondGameOverWrapper = null;

        while (firstGameOverWrapper == null || secondGameOverWrapper == null) {
            try {
                String serverInput = in.readLine();
                String gameId = parser.parseGID(serverInput);
                switch(gameId) {
                    case "A": {
                        try {
                            firstGameOverWrapper = parser.parseGameOver(serverInput);
                        }
                        catch (ParseFailureException exception) {
                            gameOneMessageHandler.setServerInput(serverInput);
                        }
                    }

                    case "B": {
                        try {
                            secondGameOverWrapper = parser.parseGameOver(serverInput);
                        }
                        catch (ParseFailureException exception) {
                            gameTwoMessageHandler.setServerInput(serverInput);
                        }
                    }

                    default: System.err.println("Invalid game Id received");
                }
            }
            catch (IOException exception) {
                System.err.println("Received IO exception");
            }
            catch (ParseFailureException exception) {
                System.err.println("Failed to parse the group id, exception: " + exception.getMessage());
            }
        }
        try {
            matchGameOneThread.join();
            matchGameTwoThread.join();
        } catch (InterruptedException exception) {
            System.err.println("Game interrupted");
        }
        gameOneMessageHandler.setServerOutput(MessageOutputRunner.terminationMessage);
        gameTwoMessageHandler.setServerOutput(MessageOutputRunner.terminationMessage);
        return new Pair<>(firstGameOverWrapper, secondGameOverWrapper);
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
