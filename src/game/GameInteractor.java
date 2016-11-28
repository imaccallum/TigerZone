package game;

import entities.board.Board;
import entities.board.Tiger;
import entities.board.Tile;
import entities.overlay.Region;
import entities.overlay.TigerDen;
import entities.overlay.TileSection;
import entities.player.Player;
import exceptions.BadPlacementException;
import exceptions.StackingTigerException;
import exceptions.TigerAlreadyPlacedException;
import game.messaging.GameStatusMessage;
import game.messaging.info.PlayerInfo;
import game.messaging.info.RegionInfo;
import game.messaging.info.RegionTigerPlacement;
import game.messaging.info.TigerDenTigerPlacement;
import game.messaging.request.FollowerPlacementRequest;
import game.messaging.request.TilePlacementRequest;
import game.messaging.response.FollowerPlacementResponse;
import game.messaging.response.TilePlacementResponse;
import server.ServerMatchMessageHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GameInteractor implements Runnable {
    private String playerTurn;
    private Stack<Tile> tileStack;
    private Map<String, Player> players;
    private List<Player> playerList;
    private Board board;

    public GameInteractor(Stack<Tile> stack) {
        board = new Board(stack.size(), stack.pop());
        tileStack = stack;
        players = new HashMap<>();
        playerList = new ArrayList<>();
        playerTurn = "";
    }

    public void addPlayer(Player player) {
        players.put(player.getName(), player);
        playerList.add(player);
    }

    public void init() {
        GameStatusMessage gameStatusMessage = createGameStatusMessage();
        for (Player notifyingPlayer : playerList) {
            notifyingPlayer.getPlayerNotifier().notifyGameStatus(gameStatusMessage);
        }
    }

    /**
     * Runs the game
     */
    public void run() {
    }

    /**
     * Plays the game, ending when the tile stack is empty.
     */
    public void playGame() {
        int playerTurnNumber = 0;
        while (!tileStack.isEmpty()) {
            Player player = playerList.get(playerTurnNumber);
            playerTurn = player.getName();
            playerTurnNumber = (playerTurnNumber + 1) % playerList.size();
            Tile tileToPlace = tileStack.pop();
            List<LocationAndOrientation> validPlacements = board.findValidTilePlacements(tileToPlace);

            if (validPlacements.isEmpty()) {
                // As consolation, allow player to stack tigers
                Set<Tiger> tigersPlacedOnBoard = players.get(playerTurn).getPlacedTigers();
                player.getPlayerNotifier().startTurn(tileToPlace, validPlacements, tigersPlacedOnBoard);
            }
            else {
                // No tigers can be stacked, must place the tile
                player.getPlayerNotifier().startTurn(tileToPlace, validPlacements, new HashSet<>());
            }

            // Synthesize the gameStatusMessage and sent it to all players
            GameStatusMessage gameStatusMessage = createGameStatusMessage();
            for (Player notifyingPlayer : playerList) {
                notifyingPlayer.getPlayerNotifier().notifyGameStatus(gameStatusMessage);
            }

        }

        // Score the regions at the end
        board.regionsAsList().stream().filter(Region::containsTigers).forEach(region -> {
            int score = region.getScorer().score();
            List<String> dominantPlayerNames = region.getDominantPlayerNames();
            for (String name : dominantPlayerNames) {
                players.get(name).addToScore(score);
            }
        });
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

    private GameStatusMessage createGameStatusMessage() {
        List<Region> regions = board.regionsAsList();
        List<RegionInfo> openRegionsInfo = regions.stream()
                .filter(region -> !region.isFinished())
                .map(Region::getRegionInfo)
                .collect(Collectors.toList());
        List<PlayerInfo> playersInfo = playerList.stream()
                .map(Player::getPlayerInfo)
                .collect(Collectors.toList());
        return new GameStatusMessage(openRegionsInfo, playersInfo);
    }

    public void log() throws IOException {
        board.log();
    }
}