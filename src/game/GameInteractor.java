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
import game.messaging.info.PlayerInfo;
import game.messaging.info.RegionInfo;
import game.messaging.info.TigerDenTigerPlacement;
import game.messaging.request.TilePlacementRequest;
import game.messaging.response.TilePlacementResponse;
import game.messaging.response.ValidMovesResponse;
import javafx.util.Pair;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;
import wrappers.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GameInteractor {
    private String playerTurn;
    private Map<String, Player> players;
    private Board board;
    private ProtocolMessageParser messageParser;
    private ProtocolMessageBuilder messageBuilder;
    private AIInterface aiNotifier;
    private String gameId;

    /**
     * Construct a game interactor with a first tile and a stack size
     *
     * @param firstTile,
     * The first tile to be placed in the game
     *
     * @param stackSize,
     * The number of tiles total in the stack of tiles to be placed
     */
    public GameInteractor(Tile firstTile, int stackSize) {
        board = new Board(stackSize, firstTile);
        players = new HashMap<>();
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
            String tile = bestMove.getTileType();
            PlacementMoveWrapper placementMove = new PlacementMoveWrapper(tile, location, orientation,
                    beginTurn.getMoveNumber());
            if (bestMove.needsTiger()) {
                placementMove.setPlacedObject(Placement.TIGER);
                placementMove.setZone(bestMove.getTigerZone());
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
        playerTurn = confirmedMove.getPid();
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
                players.get(playerTurn).decrementRemainingCrocodiles();
            }
            else if (placementMove.getPlacedObject() == Placement.TIGER) {
                TileSection tileSection = tileToPlace.tileSectionForZone(placementMove.getZone());
                if (tileSection == null) {
                    Tiger tiger = new Tiger(playerTurn, false);
                    try {
                        tileToPlace.getDen().placeTiger(tiger);
                        players.get(playerTurn).addPlacedTiger(tiger, location);
                        players.get(playerTurn).decrementRemainingTigers();
                    } catch (TigerAlreadyPlacedException e) {
                        System.err.println("Error placing tiger" + e.getMessage());
                        return;
                    }
                }
                else {
                    Tiger tiger = new Tiger(playerTurn, false);
                    try {
                        tileSection.placeTiger(tiger);
                        players.get(playerTurn).addPlacedTiger(tiger, location);
                        players.get(playerTurn).decrementRemainingTigers();
                    } catch (TigerAlreadyPlacedException e) {
                        System.err.println("Error placing tiger: " + e.getMessage());
                        return;
                    }
                }
            }
            try {
                board.place(tileToPlace, location);
            }
            catch (BadPlacementException e) {
                System.err.println("Received bad placement exception while confirming a move: " + e.getMessage());
            }

            for (TileSection tileSection : tileToPlace.getTileSections()) {
                Region region = tileSection.getRegion();
                List<Tiger> regionTigers = region.getAllTigers();
                if (region.isFinished() && !regionTigers.isEmpty()) {
                    Set<Player> awardedPlayers = new HashSet<>();
                    // Return tigers in that region
                    for (Tiger tiger : regionTigers) {
                        String owningPlayerName = tiger.getOwningPlayerName();
                        Player player = players.get(owningPlayerName);
                        int score = region.getScorer().score();
                        if (region.getDominantPlayerNames().contains(owningPlayerName) &&
                            !awardedPlayers.contains(player)) {
                            awardedPlayers.add(player);
                            player.addToScore(score);
                        }

                        player.incrementRemainingTigers();
                        if (tiger.isStacked()) {
                            // Increment remaining tigers an additional time
                            player.incrementRemainingTigers();
                        }

                        Point locationToRemoveTigerFrom = player.getPlacedTigers().get(tiger);
                        // Totally remove the tiger from the board
                        board.removeTigerFromTileAt(locationToRemoveTigerFrom, true);
                        player.getPlacedTigers().remove(tiger);
                    }
                }
            }
            System.out.println(board.toString());
        }
        else if (confirmedMove.hasForfeited()) {
            return;
        }
        else {
            System.out.println("NONPLACEMENT MOVE");
            NonplacementMoveWrapper nonplacementMove = confirmedMove.getNonplacementMove();
            Point location = nonplacementMove.getTigerLocation();
            if (nonplacementMove.getType() == UnplaceableType.RETRIEVED_TIGER) {
                Player player = players.get(playerTurn);
                // Remove a tiger from the board, allow unstacking
                Pair<Tiger, Tiger> tigers = board.removeTigerFromTileAt(location, false);
                if (tigers.getKey() != null) {
                    // There is a removed tiger
                    player.getPlacedTigers().remove(tigers.getKey());
                }
                else {
                    System.err.println("Failed to remove a tiger from the board");
                }
                if (tigers.getValue() != null) {
                    // There is a placed tiger
                    player.getPlacedTigers().put(tigers.getValue(), location);
                }
                player.incrementRemainingTigers();

            }
            else if (nonplacementMove.getType() == UnplaceableType.ADDED_TIGER) {
                try {
                    Player player = players.get(playerTurn);
                    Pair<Tiger, Tiger> tigers = board.stackTigerAt(location);
                    player.getPlacedTigers().remove(tigers.getKey());
                    player.getPlacedTigers().put(tigers.getValue(), location);
                    player.decrementRemainingTigers();
                }
                catch (StackingTigerException exception) {
                    System.err.println("Problem confirming tiger stacking: " + exception);
                }
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
            List<Point> tigersPlacedLocations = new ArrayList<>(players.get(playerTurn).getPlacedTigers().values());
            return new ValidMovesResponse(validPlacements, tigersPlacedLocations, true);
        }
        else {
            return new ValidMovesResponse(validPlacements, new ArrayList<>(), false);
        }
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
    /*
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
    */

    /**
     * Try a tile placement and get information about that tile placement in a response
     *
     * @param request,
     * The tile placement request to try
     *
     * @return
     * The reponse data associated with the tile placement tried
     */
    public TilePlacementResponse tryTilePlacement(TilePlacementRequest request) {
        if (!request.playerName.equals(playerTurn)) {
            // Not the players turn
            return new TilePlacementResponse(false, null, false, new HashMap<>());
        }
        else {
            try {
                board.place(request.tileToPlace, request.locationToPlaceAt);
            }
            catch (BadPlacementException exception) {
                System.err.println(exception.getMessage());
                return new TilePlacementResponse(false, null, false, new HashMap<>());
            }


            Map<Region, Set<Region>> allMergedRegions = new HashMap<>();
            Stack<RegionMerge> lastRegionMerges = board.getLastRegionMerges();

            while (!lastRegionMerges.isEmpty()) {
                RegionMerge regionMerge = lastRegionMerges.pop();
                boolean found = false;
                for (Set<Region> regionSet : allMergedRegions.values()) {
                    if (regionSet.contains(regionMerge.newRegion)) {
                        regionSet.remove(regionMerge.newRegion);
                        regionSet.add(regionMerge.firstOldRegion);
                        regionSet.add(regionMerge.secondOldRegion);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // Create a new entry in the map
                    Set<Region> oldRegions = new HashSet<>();
                    oldRegions.add(regionMerge.firstOldRegion);
                    oldRegions.add(regionMerge.secondOldRegion);
                    allMergedRegions.put(regionMerge.newRegion, oldRegions);
                }
            }

            Map<UUID, Integer> oldRegionScores = new HashMap<>();
            for (Map.Entry<Region, Set<Region>> newAndOldRegionsEntry : allMergedRegions.entrySet()) {
                Integer oldScore = 0;
                for (Region region : newAndOldRegionsEntry.getValue()) {
                    oldScore  += region.getScorer().scoreIfCompletedNow();
                }
                oldRegionScores.put(newAndOldRegionsEntry.getKey().getRegionId(), oldScore);
            }

            Map<UUID, Integer> regionTigerZonePlacements = new HashMap<>();
            Set<UUID> recordedRegions = new HashSet<>();
            Map<RegionInfo, Integer> regionsEffected = new HashMap<>();
            for (TileSection tileSection : request.tileToPlace.getTileSections()) {
                if (!recordedRegions.contains(tileSection.getRegion().getRegionId())) {
                    RegionInfo regionInfo = tileSection.getRegion().getRegionInfo();
                    Integer oldScore = oldRegionScores.get(regionInfo.regionId);
                    int newScore = regionInfo.scoreIfRegionCompletedNow;
                    Integer scoreDifferential = oldScore != null ? newScore - oldScore : newScore;
                    regionsEffected.put(regionInfo, scoreDifferential);
                    recordedRegions.add(regionInfo.regionId);
                }
                if (tileSection.getRegion().getDominantPlayerNames().isEmpty()) {
                    // Can place a tiger
                    UUID regionId = tileSection.getRegion().getRegionId();
                    Integer recordedZone = regionTigerZonePlacements.get(regionId);
                    int foundZone = request.tileToPlace.getTigerZone(tileSection);
                    if (recordedZone == null ) {
                        regionTigerZonePlacements.put(regionId, foundZone);
                    }
                    else if (foundZone < recordedZone) {
                        regionTigerZonePlacements.put(regionId, foundZone);
                    }
                }

            }

            for (RegionInfo regionEffected : regionsEffected.keySet()) {
                Integer found = regionTigerZonePlacements.get(regionEffected.regionId);
                if (found != null) {
                    regionEffected.possibleTigerPlacementZone = found;
                }
            }

            TigerDenTigerPlacement denPlacement = null;
            if (request.tileToPlace.getDen() != null) {
                TigerDen den = request.tileToPlace.getDen();
                denPlacement = new TigerDenTigerPlacement(den.getCenterTileLocation(), den.getRequiredTileLocations());
            }

            boolean canPlaceCrocodile = request.tileToPlace.canPlaceCrocodile();

            // undo the tile placement
            board.removeLastPlacedTile();

            return new TilePlacementResponse(true, denPlacement, canPlaceCrocodile, regionsEffected);
        }
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

    public String getGameId() {
        return gameId;
    }

    public List<PlayerInfo> getPlayerInfos() {
        return players.values().stream()
                .map(Player::getPlayerInfo)
                .collect(Collectors.toList());
    }
}