package server.networkState;

import controller.AIController;
import controller.AIInterface;
import entities.board.Tile;
import entities.board.TileFactory;
import exceptions.ParseFailureException;
import game.GameInteractor;
import game.Player;
import server.ProtocolMessageParser;
import wrappers.BeginTurnWrapper;
import wrappers.ConfirmedMoveWrapper;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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


    public NetworkContext(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    public void startMatch() {
        System.out.println("STATE: GAME");

        Tile firstTile = TileFactory.makeTile(startingTile);
        Tile secondTile = TileFactory.makeTile(startingTile);

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

        boolean gameOneOver = false;
        boolean gameTwoOver = false;

        try {

            String serverInput;
            String serverOutput = "";

            while (true) {

                if (gameOneOver && gameTwoOver) return;

                serverInput = in.readLine();
                System.out.println("SERVER: " + serverInput);

                // Check if game is still running
                try {
                    String gameId = parser.parseGameOver(serverInput);

                    if (gameId.equals(gameOne.getGameId())) {
                        gameOneOver = true;
                    } else if (gameId.equals(gameTwo.getGameId())) {
                        gameTwoOver = true;
                    } else {
                        System.err.println("Received bad game id: " + gameId);
                        return;
                    }

                    continue;
                }
                catch (ParseFailureException parseException) {}


                // Check if begin turn
                try {

                    BeginTurnWrapper beginTurn = parser.parseBeginTurn(serverInput);
                    String gameId = beginTurn.getGid();
                    System.out.println("BEGINNING TURN " + beginTurn.getGid() + " tile: " + beginTurn.getTile());


                    if (gameOne.getGameId() == null) {
                        gameOne.setGameId(gameId);
                    } else if (gameTwo.getGameId() == null) {
                        gameTwo.setGameId(gameId);
                    }

                    if (gameId.equals(gameOne.getGameId())) {
                        serverOutput = gameOne.decideTurn(serverInput);
                    } else if (gameId.equals(gameTwo.getGameId())) {
                        serverOutput = gameTwo.decideTurn(serverInput);
                    } else {
                        System.err.println("Invalid game Id received " + gameId);
                        return;
                    }

                    System.out.println("CLIENT: " + serverOutput);
                    out.println(serverOutput);
                    continue;

                } catch (ParseFailureException e) {}


                // Check if confirm move
                try {
                    ConfirmedMoveWrapper confirmedMove = parser.parseConfirmMove(serverInput);
                    String gameId = confirmedMove.getGid();
                    System.out.println("PARSED CONFIRM MOVE " + gameId);

                    if (gameId.equals(gameOne.getGameId())) {
                        gameOne.confirmMove(confirmedMove);
                        continue;
                    } else if (gameOne.getGameId() == null) {
                        gameOne.setGameId(gameId);
                    } else if (gameTwo.getGameId() == null) {
                        gameTwo.setGameId(gameId);
                    }

                    if (gameId.equals(gameOne.getGameId())) {
                        gameOne.confirmMove(confirmedMove);
                    } else if (gameId.equals(gameTwo.getGameId())) {
                        gameTwo.confirmMove(confirmedMove);
                    } else {
                        System.err.println("Received confirmed move with bad GID: " + confirmedMove.getGid());
                        return;
                    }
                } catch (ParseFailureException e) {

                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getLocalizedMessage());
        }
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
