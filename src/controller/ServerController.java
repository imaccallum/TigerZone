package controller;

import entities.board.Placement;
import entities.board.Tiger;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.overlay.TileSection;
import exceptions.ParseFailureException;
import exceptions.TigerAlreadyPlacedException;
import game.GameInteractor;
import game.messaging.request.TilePlacementRequest;
import server.ProtocolMessageParser;
import server.ServerMatchMessageHandler;
import wrappers.*;

import java.awt.*;

public class ServerController implements Runnable {
    private ServerMatchMessageHandler matchMessageHandler;
    private ProtocolMessageParser messageParser;
    private GameInteractor gameInteractor;
    private String playerName;
    private AIController aiController;

    public ServerController(GameInteractor gameInteractor, String playerName,
                            ServerMatchMessageHandler matchMessageHandler,
                            AIController aiController) {
        this.gameInteractor = gameInteractor;
        this.playerName = playerName;
        this.matchMessageHandler = matchMessageHandler;
        messageParser = new ProtocolMessageParser();
        this.aiController = aiController;
    }

    @Override
    public boolean run() {
        String message = "";
        try {
            message = matchMessageHandler.getServerInput();
        } catch (InterruptedException exception) {
            System.err.println(exception.getMessage());
        }

        boolean ourTurn = false;
        BeginTurnWrapper beginTurn = null;
        try {
            beginTurn = messageParser.parseBeginTurn(message);
            ourTurn = true;
        }
        catch (ParseFailureException exception) {
            System.out.println("Failed to parse begin turn");
            return false;
        }

        boolean confirmed = false;
        ConfirmedMoveWrapper confirmedMove = null;
        try {
            confirmedMove = messageParser.parseConfirmMove(message);
            confirmed = true;
        }
        catch (ParseFailureException exception) {
            System.out.println("Failed to parse placement turn");]
        }

        if (ourTurn) {
            return decideMove(beginTurn);
        }
        else if (confirmed) {
            return confirmMove(confirmedMove);
        } else {

        }
    }

    private boolean confirmMove(ConfirmedMoveWrapper confirmedMove) {
        if (confirmedMove.hasForfeited()) {
            return false;
        }
        else if (confirmedMove.isPlacementMove()) {
            PlacementMoveWrapper placementMove = confirmedMove.getPlacementMove();
            Point location = placementMove.getLocation();
            int orientation = placementMove.getOrientation();
            Tile tileToPlace = TileFactory.makeTile(placementMove.getTile());
            tileToPlace.rotateCounterClockwise(orientation);
            if (placementMove.getPlacedObject() == Placement.CROCODILE) {
                tileToPlace.placeCrocodile();
            }
            else if (placementMove.getPlacedObject() == Placement.TIGER) {
                TileSection tileSection = tileToPlace.tileSectionForZone(placementMove.getZone());
                if (tileSection == null) {
                    Tiger tiger = new Tiger(playerName, false);
                    try {
                        tileToPlace.getDen().placeTiger(tiger);
                        gameInteractor.placeTiger(tiger, playerName);
                    } catch (TigerAlreadyPlacedException e) {
                        System.err.println("Error placing tiger" + e.getMessage());
                        return false;
                    }
                }
                else {
                    Tiger tiger = new Tiger(playerName, false);
                    try {
                        tileSection.placeTiger(tiger);
                        gameInteractor.placeTiger(tiger, playerName);
                    } catch (TigerAlreadyPlacedException e) {
                        System.err.println("Error placing tiger: " + e.getMessage());
                        return false;
                    }
                }

            }
            TilePlacementRequest request = new TilePlacementRequest(playerName, tileToPlace, location);
            gameInteractor.handleTilePlacementRequest(request);
            return true;
        }
        else {
            NonplacementMoveWrapper nonplacementMove = confirmedMove.getNonplacementMove();
            if (nonplacementMove.getType() == UnplaceableType.RETRIEVED_TIGER) {
                gameInteractor.removeTigerFromTileAt(nonplacementMove.getTigerLocation(), playerName);
                return true;
            }
            else if (nonplacementMove.getType() == UnplaceableType.ADDED_TIGER) {
                gameInteractor.stackTigerAt(nonplacementMove.getTigerLocation(), playerName);
                return true;
            }
            else {
                return true;
            }
        }
    }

    private boolean decideMove(BeginTurnWrapper beginTurn) {
        aiController.decideMove(beginTurn);
        Move bestMove = aiController.getBestMove();
        bestMove.
    }
}
