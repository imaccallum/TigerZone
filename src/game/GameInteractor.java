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
import wrappers.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameInteractor {
    private String playerTurn;
    private Map<String, Player> players;
    private List<Player> playerList;
    private Board board;
    private ProtocolMessageParser messageParser;
    private ProtocolMessageBuilder messageBuilder;
    private AIInterface aiNotifier;

    public String getGameId() {
        return gameId;
    }

    private String gameId;

    public GameInteractor(Tile firstTile, int stackSize) {
        board = new Board(stackSize, firstTile);
        players = new HashMap<>();
        playerList = new ArrayList<>();
        playerTurn = "";
        messageParser = new ProtocolMessageParser();
        messageBuilder = new ProtocolMessageBuilder();
    }

    /**
     * Sets all of the parameters for a Move to be decided in terms of possible follower placement
     *
     * @param serverInput
     * The String for the input from the server
     * @return
     * A String that contains the message of the Move that was just constructed
     * @throws ParseFailureException
     * May return an exception if the parsing of the turn goes wrong
     */
    public String decideTurn(String serverInput) throws ParseFailureException {
        BeginTurnWrapper beginTurn = messageParser.parseBeginTurn(serverInput);
        playerTurn = aiNotifier.getPlayerName();
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
            return messageBuilder.messageForMove(placementMove, gameId);
        }
        else {
            NonplacementMoveWrapper nonplacementMove = new NonplacementMoveWrapper(beginTurn.getTile(),
                    beginTurn.getMoveNumber());
            return messageBuilder.messageForNonplacementMove(nonplacementMove, gameId);
        }
    }

    /**
     * Confirms the move that was just made making sure that there are no illegal placements
     *
     * @param confirmedMove
     * The move to be confirmed
     */
    public void confirmMove(ConfirmedMoveWrapper confirmedMove) {
        if (!confirmedMove.hasForfeited() && confirmedMove.isPlacementMove()) {
            System.out.println("PLACEMENT MOVE");
            PlacementMoveWrapper placementMove = confirmedMove.getPlacementMove();
            Point location = board.getNativeLocation(placementMove.getLocation());
            int orientation = placementMove.getOrientation();
            Tile tileToPlace = TileFactory.makeTile(placementMove.getTile());
            tileToPlace.rotateCounterClockwise(orientation);

            System.out.println("Location " + location.getX() + " " + location.getY());

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
                        return;
                    }
                }
                else {
                    Tiger tiger = new Tiger(playerTurn, false);
                    try {
                        tileSection.placeTiger(tiger);
                        placeTiger(tiger, playerTurn);
                    } catch (TigerAlreadyPlacedException e) {
                        System.err.println("Error placing tiger: " + e.getMessage());
                        return;
                    }
                }

            }
            TilePlacementRequest request = new TilePlacementRequest(playerTurn, tileToPlace, location);
            handleTilePlacementRequest(request);
        }
        else if (confirmedMove.hasForfeited()) {
            return;
        }
        else {
            System.out.println("NONPLACEMENT MOVE");
            NonplacementMoveWrapper nonplacementMove = confirmedMove.getNonplacementMove();
            if (nonplacementMove.getType() == UnplaceableType.RETRIEVED_TIGER) {
                removeTigerFromTileAt(nonplacementMove.getTigerLocation(), playerTurn);
            }
            else if (nonplacementMove.getType() == UnplaceableType.ADDED_TIGER) {
                stackTigerAt(nonplacementMove.getTigerLocation(), playerTurn);
            }
        }
    }

    /**
     * Adds a player to the list of of players and the Map
     * @param player
     * The player to be added
     */
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

    /**
     * Removes a Tiger from the given location
     *
     * @param location
     * The location from which a Tiger is to be removed
     * @param playerName
     * The Player that receives the tiger back;
     */
    public void removeTigerFromTileAt(Point location, String playerName) {
        board.removeTigerFromTileAt(location);
        players.get(playerName).incrementRemainingTigers();
    }

    /**
     * Adds a Tiger at a location on which there is already a Tiger
     *
     * @param location
     * The location on which the Tiger is to be stacked
     * @param playerName
     * The name of the player who will place the Tiger
     */
    public void stackTigerAt(Point location, String playerName) {
        try {
            board.stackTigerAt(location);
            players.get(playerName).decrementRemainingTigers();
        }
        catch (StackingTigerException exception) {
            System.err.println("Stacking tiger error: " + exception.getMessage());
        }
    }

    /**
     * Places a Tiger at a given location
     *
     * @param tiger
     * The Tiger to be placed
     * @param playerName
     * The location on which the Tiger is to be placed
     */
    public void placeTiger(Tiger tiger, String playerName) {
        players.get(playerName).addPlacedTiger(tiger);
        players.get(playerName).decrementRemainingTigers();
    }

    public void log() throws IOException {
        board.log();
    }

    public void setAiNotifier(AIInterface aiNotifier) {
        this.aiNotifier = aiNotifier;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}