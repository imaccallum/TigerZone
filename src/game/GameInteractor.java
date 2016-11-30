package game;

import controller.AIInterface;
import controller.Move;
import entities.board.*;
import entities.overlay.Region;
import entities.overlay.TigerDen;
import entities.overlay.TileSection;
import exceptions.BadPlacementException;
import exceptions.ParseFailureException;
import exceptions.StackingTigerException;
import exceptions.TigerAlreadyPlacedException;
import game.messaging.info.RegionInfo;
import game.messaging.info.RegionTigerPlacement;
import game.messaging.info.TigerDenTigerPlacement;
import game.messaging.request.TilePlacementRequest;
import game.messaging.response.TilePlacementResponse;
import game.messaging.response.ValidMovesResponse;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;
import server.ServerMatchMessageHandler;
import wrappers.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameInteractor implements Runnable {
    private String playerTurn;
    private Map<String, Player> players;
    private List<Player> playerList;
    private Board board;
    private ServerMatchMessageHandler messageHandler;
    private ProtocolMessageParser messageParser;
    private ProtocolMessageBuilder messageBuilder;
    private GameOverWrapper gameOver;
    private AIInterface aiNotifier;
    private String gameId;

    public GameInteractor(Tile firstTile, int stackSize, ServerMatchMessageHandler messageHandler) {
        board = new Board(stackSize, firstTile);
        players = new HashMap<>();
        playerList = new ArrayList<>();
        playerTurn = "";
        this.messageHandler = messageHandler;
        messageParser = new ProtocolMessageParser();
        messageBuilder = new ProtocolMessageBuilder();
    }

    @Override
    public void run() {
        while (gameOver == null) {
            String message = "";
            try {
                message = messageHandler.getServerInput();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }

            boolean ourTurn = false;
            BeginTurnWrapper beginTurn = null;
            try {
                beginTurn = messageParser.parseBeginTurn(message);
                ourTurn = true;
            }
            catch (ParseFailureException exception) {}

            boolean confirmed = false;
            ConfirmedMoveWrapper confirmedMove = null;
            try {
                confirmedMove = messageParser.parseConfirmMove(message);
                confirmed = true;
            }
            catch (ParseFailureException exception) {}

            try {
                gameOver = messageParser.parseGameOver(message);
            }
            catch (ParseFailureException exception) {}

            if (ourTurn) {
                playerTurn = aiNotifier.getPlayerName();
                String serverMessage = "";
                Move bestMove = aiNotifier.decideMove(beginTurn);
                if (bestMove != null) {
                    Point location = board.getServerLocation(bestMove.getLocationAndOrientation().getLocation());
                    int orientation = bestMove.getLocationAndOrientation().getOrientation();
                    String tile = bestMove.getTile().getType();
                    PlacementMoveWrapper placementMove = new PlacementMoveWrapper(tile, location, orientation,
                                                                                  beginTurn.getMoveNumber());
                    if (bestMove.needsTiger()) {
                        placementMove.setPlacedObject(Placement.TIGER);
                        placementMove.setZone(bestMove.getTile().getTigerZone(bestMove.getTileSection()));
                    }
                    else if (bestMove.needsCrocodile()) {
                        placementMove.setPlacedObject(Placement.CROCODILE);
                    }
                    else {
                        placementMove.setPlacedObject(Placement.NONE);
                    }
                    serverMessage = messageBuilder.messageForMove(placementMove, gameId);
                }
                else {
                    NonplacementMoveWrapper nonplacementMove = new NonplacementMoveWrapper(beginTurn.getTile(),
                                                                                           beginTurn.getMoveNumber());
                    serverMessage = messageBuilder.messageForNonplacementMove(nonplacementMove, gameId);
                }
                messageHandler.setServerOutput(serverMessage);
            }
            else if (confirmed) {
                playerTurn = confirmedMove.getPid();
                confirmMove(confirmedMove);
            }
        }
    }

    public void addPlayer(Player player) {
        players.put(player.getName(), player);
        playerList.add(player);
    }

    public void place(Tile tile, Point location) throws BadPlacementException {
        board.place(tile, location);
    }

    public void removeLastPlacedTile() {
        board.removeLastPlacedTile();
    }

    /**
     * Get the valid moves for a tile on the board
     *
     * @param tile,
     * The tile that we want to place
     *
     * @return
     * A response with the valid moves one can do
     */
    public ValidMovesResponse getValidMoves(Tile tile) {
        List<LocationAndOrientation> validPlacements = board.findValidTilePlacements(tile);
        if (validPlacements.isEmpty()) {
            // As consolation, allow player to stack tigers
            Set<Tiger> tigersPlacedOnBoard = players.get(playerTurn).getPlacedTigers();
            return new ValidMovesResponse(validPlacements, tigersPlacedOnBoard, true);
        }
        else {
            return new ValidMovesResponse(validPlacements, new HashSet<>(), false);
        }
    }

    public Map<String, Player> getPlayers() {
        return players;
    }



    /**
     * Handle the placement of a follower as described in a follower placement request
     *
     * @param request,
     * The request describing the placement of the follower
     *
     * @return
     * The response to this request as a FollowerPlacementResponse
     */
    /*
    public FollowerPlacementResponse handleFollowerPlacementRequest(FollowerPlacementRequest request) {
        if (!request.playerName.equals(playerTurn)) {
            // Not the current players turn
            return new FollowerPlacementResponse(false, false, false);
        }
        else if (request.isRegionPlacement()) {
            boolean success = attemptTigerPlacementInRegion(request);
            if (success) {
                players.get(request.playerName).decrementRemainingTigers();
                return new FollowerPlacementResponse(true, false, true);
            }
            else {
                return new FollowerPlacementResponse(false, false, false);
            }
        }
        else if (request.isTigerDenPlacement()) {
            boolean success = attemptTigerPlacementInDen(request);
            if (success) {
                players.get(request.playerName).decrementRemainingTigers();
                return new FollowerPlacementResponse(true, false, true);
            }
            else {
                // Failed to place tiger, invalid move
                return new FollowerPlacementResponse(false, false, false);
            }
        }
        else if (request.isStackingTiger()) {
            boolean success = attemptStackingTiger(request);
            if (success) {
                // Valid move
                players.get(request.playerName).decrementRemainingTigers();
                return new FollowerPlacementResponse(true, false, true);
            }
            else {
                // Failed to place tiger, invalid move
                return new FollowerPlacementResponse(false, false, false);
            }
        }
        else if (request.isCrocodilePlacement()) {
            boolean success = attemptCrocodilePlacement(request);
            if (success) {
                players.get(request.playerName).decrementRemainingCrocodiles();
                return new FollowerPlacementResponse(false, true, true);
            }
            else {
                return new FollowerPlacementResponse(false, false, false);
            }
        }
        else {
            // Valid placement, placed nothing
            return new FollowerPlacementResponse(false, false, true);
        }
    }
    */

    /**
     * handle a request to place a tile on the board
     *
     * @param request,
     * The request to place the tile on the board
     *
     * @return
     * The response result of the tiger being placed on the board
     */
    public TilePlacementResponse handleTilePlacementRequest(TilePlacementRequest request) {
        if (!request.playerName.equals(playerTurn)) {
            // Not the players turn
            return new TilePlacementResponse(false, null, null, false);
        }
        else {
            try {
                board.place(request.tileToPlace, request.locationToPlaceAt);
            }
            catch (BadPlacementException exception) {
                System.err.println(exception.getMessage());
                return new TilePlacementResponse(false, null, null, false);
            }

            List<RegionTigerPlacement> regionTigerPlacements = new ArrayList<>();
            request.tileToPlace.getTileSections().forEach(tileSection -> {
                Region region = tileSection.getRegion();
                RegionInfo regionInfo = region.getRegionInfo();
                boolean regionJustFinished = region.isFinished();
                regionTigerPlacements.add(new RegionTigerPlacement(regionInfo, regionJustFinished));
            });

            TigerDenTigerPlacement denPlacement = null;
            if (request.tileToPlace.getDen() != null) {
                TigerDen den = request.tileToPlace.getDen();
                denPlacement = new TigerDenTigerPlacement(den.getCenterTileLocation(), den.getRequiredTileLocations());
            }

            boolean canPlaceCrocodile = request.tileToPlace.canPlaceCrocodile();

            return new TilePlacementResponse(true, regionTigerPlacements, denPlacement, canPlaceCrocodile);
        }
    }

    public void removeTigerFromTileAt(Point location, String playerName) {
        board.removeTigerFromTileAt(location);
        players.get(playerName).incrementRemainingTigers();
    }

    public void stackTigerAt(Point location, String playerName) {
        try {
            board.stackTigerAt(location);
            players.get(playerName).decrementRemainingTigers();
        }
        catch (StackingTigerException exception) {
            System.err.println("Stacking tiger error: " + exception.getMessage());
        }
    }

    public void placeTiger(Tiger tiger, String playerName) {
        players.get(playerName).addPlacedTiger(tiger);
        players.get(playerName).decrementRemainingTigers();
    }

    public void log() throws IOException {
        board.log();
    }

    public GameOverWrapper getGameOver() {
        return gameOver;
    }

    private boolean confirmMove(ConfirmedMoveWrapper confirmedMove) {
        if (confirmedMove.hasForfeited()) {
            return false;
        }
        else if (confirmedMove.isPlacementMove()) {
            PlacementMoveWrapper placementMove = confirmedMove.getPlacementMove();
            Point location = board.getNativeLocation(placementMove.getLocation());
            int orientation = placementMove.getOrientation();
            Tile tileToPlace = TileFactory.makeTile(placementMove.getTile());
            tileToPlace.rotateCounterClockwise(orientation);
            if (placementMove.getPlacedObject() == Placement.CROCODILE) {
                tileToPlace.placeCrocodile();
            }
            else if (placementMove.getPlacedObject() == Placement.TIGER) {
                TileSection tileSection = tileToPlace.tileSectionForZone(placementMove.getZone());
                if (tileSection == null) {
                    Tiger tiger = new Tiger(playerTurn, false);
                    try {
                        tileToPlace.getDen().placeTiger(tiger);
                        placeTiger(tiger, playerTurn);
                    } catch (TigerAlreadyPlacedException e) {
                        System.err.println("Error placing tiger" + e.getMessage());
                        return false;
                    }
                }
                else {
                    Tiger tiger = new Tiger(playerTurn, false);
                    try {
                        tileSection.placeTiger(tiger);
                        placeTiger(tiger, playerTurn);
                    } catch (TigerAlreadyPlacedException e) {
                        System.err.println("Error placing tiger: " + e.getMessage());
                        return false;
                    }
                }

            }
            TilePlacementRequest request = new TilePlacementRequest(playerTurn, tileToPlace, location);
            handleTilePlacementRequest(request);
            return true;
        }
        else {
            NonplacementMoveWrapper nonplacementMove = confirmedMove.getNonplacementMove();
            if (nonplacementMove.getType() == UnplaceableType.RETRIEVED_TIGER) {
                removeTigerFromTileAt(nonplacementMove.getTigerLocation(), playerTurn);
                return true;
            }
            else if (nonplacementMove.getType() == UnplaceableType.ADDED_TIGER) {
                stackTigerAt(nonplacementMove.getTigerLocation(), playerTurn);
                return true;
            }
            else {
                return true;
            }
        }
    }

    public void setAiNotifier(AIInterface aiNotifier) {
        this.aiNotifier = aiNotifier;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }



    /*

    private boolean attemptTigerPlacementInDen(FollowerPlacementRequest request) {
        TigerDenTigerPlacement placement = request.denTigerPlacement;
        if (!players.get(request.playerName).hasRemainingTigers()) {
            // No tigers for the player to place
            return false;
        }
        else if (placement.centerPoint != board.getLastPlacedTile().getLocation()) {
            // Point does not match last placed tile location, placed nothing, invalid move
            return false;
        }
        else if (board.getLastPlacedTile().getDen() == null) {
            // No den on last placed tile, placed nothing, invalid move
            return false;
        }
        else {
            try {
                Tiger tigerToPlace = new Tiger(request.playerName, false);
                board.getLastPlacedTile().getDen().placeTiger(tigerToPlace);
                board.addPlacedTiger(tigerToPlace);
                return true;
            }
            catch(TigerAlreadyPlacedException exception) {
                System.err.println(exception.getMessage());
                // Tiger already in den, placed nothing, invalid move
                return false;
            }
        }
    }

    private boolean attemptStackingTiger(FollowerPlacementRequest request) {
        if (!players.get(request.playerName).hasRemainingTigers()) {
            // No tigers left to place
            return false;
        }
        Tiger tigerToStack = request.tigerToStack;
        try {
            board.stackTiger(tigerToStack);
            players.get(request.playerName).decrementRemainingTigers();
            return true;
        }
        catch (StackingTigerException exception) {
            System.err.println(exception.getMessage());
            return false;
        }
    }

    private boolean attemptCrocodilePlacement(FollowerPlacementRequest request) {
        if (!players.get(request.playerName).hasRemainingCrocodiles()) {
            // No crocodiles to place
            return false;
        }
        else if (board.getLastPlacedTile().hasCrocodile()) {
            // Crocodile already on tile
            return false;
        }
        else if (board.getLastPlacedTile().canPlaceCrocodile()) {
            board.getLastPlacedTile().placeCrocodile();
            players.get(request.playerName).decrementRemainingCrocodiles();
            return true;
        } else {
            // Not sure what else could go wrong
            return false;
        }
    }

    private boolean attemptTigerPlacementInRegion(FollowerPlacementRequest request) {
        if (!players.get(request.playerName).hasRemainingTigers()) {
            return false;
        }
        Region region = board.regionForId(request.regionTigerPlacement.regionInfo.regionId);
        Tile lastPlacedTile = board.getLastPlacedTile();
        for (TileSection tileSection : lastPlacedTile.getTileSections()) {
            if (tileSection.getRegion().equals(region)) {
                try {
                    Tiger tigerToPlace = new Tiger(request.playerName, false);
                    tileSection.placeTiger(tigerToPlace);
                    board.addPlacedTiger(tigerToPlace);
                    return true;
                }
                catch (TigerAlreadyPlacedException exception) {
                    System.err.println(exception.getMessage());
                    return false;
                }
            }
        }
        // Region was not on the tile
        System.err.println("Attempted to place a tiger in a region not on the last placed tiger");
        return false;
    }

    */
}