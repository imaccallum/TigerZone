package controller;

import entities.board.Tile;
import entities.board.TileFactory;
import exceptions.ParseFailureException;
import game.GameInteractor;
import game.messaging.request.TilePlacementRequest;
import game.messaging.response.TilePlacementResponse;
import server.ProtocolMessageParser;
import server.ServerMatchMessageHandler;
import wrappers.ConfirmedMoveWrapper;
import wrappers.NonplacementMoveWrapper;
import wrappers.PlacementMoveWrapper;

import java.awt.*;

public class ServerController implements PlayerNotifier {
    private ServerMatchMessageHandler matchMessageHandler;
    private ProtocolMessageParser messageParser;
    private GameInteractor gameInteractor;
    private String playerName;

    public ServerController(GameInteractor gameInteractor, String playerName,
                            ServerMatchMessageHandler matchMessageHandler) {
        this.gameInteractor = gameInteractor;
        this.playerName = playerName;
        this.matchMessageHandler = matchMessageHandler;
        messageParser = new ProtocolMessageParser();
    }

    @Override
    public boolean startTurn() {
        String turnConfirmed = "";
        try {
            turnConfirmed = matchMessageHandler.getServerInput();
        } catch (InterruptedException exception) {
            System.err.println(exception.getMessage());
        }

        ConfirmedMoveWrapper confirmedMove = null;
        try {
            confirmedMove = messageParser.parseConfirmMove(turnConfirmed);
        }
        catch (ParseFailureException exception) {
            System.out.println("Failed to parse placement turn");
            return false;
        }

        if (confirmedMove.hasForfeited()) {
            return false;
        }
        else if (confirmedMove.isPlacementMove()) {
            PlacementMoveWrapper placementMove = confirmedMove.getPlacementMove();
            Point location = placementMove.getLocation();
            int orientation = placementMove.getOrientation();
            Tile tileToPlace = TileFactory.makeTile(placementMove.getTile());
            tileToPlace.rotateCounterClockwise(orientation);
            TilePlacementRequest request = new TilePlacementRequest(playerName, tileToPlace, location);
            TilePlacementResponse response = gameInteractor.handleTilePlacementRequest(request);
            return true;
        }
        else {
            NonplacementMoveWrapper nonplacementMove = confirmedMove.getNonplacementMove();
            return true;
        }
    }
}
