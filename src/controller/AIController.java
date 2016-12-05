package controller;

import entities.board.Tile;
import entities.board.TileFactory;
import entities.overlay.TileSection;
import game.GameInteractor;
import game.LocationAndOrientation;
import game.messaging.info.PlayerInfo;
import game.messaging.info.RegionInfo;
import game.messaging.request.TilePlacementRequest;
import game.messaging.response.TilePlacementResponse;
import game.messaging.response.ValidMovesResponse;
import wrappers.BeginTurnWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AIController implements AIInterface {
    private GameInteractor gameInteractor;
    private String playerName;
    private Map<String, PlayerInfo> playerInfos;


    public AIController(GameInteractor gameInteractor, String playerName) {
        this.gameInteractor = gameInteractor;
        this.playerName = playerName;
        this.playerInfos = new HashMap<>();
    }

    private Move calculateMoveWithCrocodile(String tileToPlaceName, LocationAndOrientation locationAndOrientation) {
        int tileScore = 0;

        // Create the tile to be placed, rotate it, and add the crocodile
        Tile tileToPlace = TileFactory.makeTile(tileToPlaceName);
        tileToPlace.placeCrocodile();
        tileToPlace.rotateCounterClockwise(locationAndOrientation.getOrientation());
        TilePlacementRequest request = new TilePlacementRequest(playerName, tileToPlace,
                                                                locationAndOrientation.getLocation());

        TilePlacementResponse response = gameInteractor.tryTilePlacement(request);
        for (Map.Entry<RegionInfo, Integer> effectedRegionEntry : response.regionsEffected.entrySet()) {
            int scoreDifference = effectedRegionEntry.getValue();
            if (effectedRegionEntry.getKey().dominantPlayerNames.contains(playerName) &&
                effectedRegionEntry.getKey().dominantPlayerNames.size() == 1) {
                // The score difference hurts us because we are hurting ourselves
                tileScore += scoreDifference;
            }
            else if (!effectedRegionEntry.getKey().dominantPlayerNames.isEmpty()) {
                // The score difference helps use because we are hurting other players
                tileScore -= scoreDifference;
            }
        }
        return new Move(tileToPlaceName, locationAndOrientation, tileScore, false, true, -1);
    }

    private List<Move> calculateMovesWithTigers(String tileToPlaceName, LocationAndOrientation locationAndOrientation) {
        int baseTileScore = 0;
        List<Move> tigerPlacementMoves = new ArrayList<>();
        Tile tileToPlace = TileFactory.makeTile(tileToPlaceName);
        tileToPlace.rotateCounterClockwise(locationAndOrientation.getOrientation());
        TilePlacementRequest request = new TilePlacementRequest(playerName, tileToPlace,
                                                                 locationAndOrientation.getLocation());
        TilePlacementResponse response = gameInteractor.tryTilePlacement(request);

        // Calculate the base tile score by adding and subtracting ongoing region scores
        for (Map.Entry<RegionInfo, Integer> effectedRegionEntry : response.regionsEffected.entrySet()) {
            // The score difference without playing the tiger
            int scoreDifference = effectedRegionEntry.getValue();
            if (effectedRegionEntry.getKey().dominantPlayerNames.contains(playerName)) {
                // one of our regions is being added to, add the score difference
                baseTileScore += scoreDifference;
                if (effectedRegionEntry.getKey().isFinished) {
                    baseTileScore += effectedRegionEntry.getKey().scoreIfRegionCompletedNow;
                }
            }
            else if (!effectedRegionEntry.getKey().dominantPlayerNames.isEmpty()) {
                // Someone else has that region, subtract the score difference from ours
                baseTileScore -= scoreDifference;
                if (effectedRegionEntry.getKey().isFinished) {
                    baseTileScore -= effectedRegionEntry.getKey().scoreIfRegionCompletedNow;
                }
            } else {
                // We could place a tiger here, create a new tiger move
                int zoneToPlaceTigerIn = effectedRegionEntry.getKey().possibleTigerPlacementZone;

                if (zoneToPlaceTigerIn == 10) {
                    System.err.println("Region info with no dominant players could not find zone to place tiger in");
                }
                else {
                    if (effectedRegionEntry.getKey().isFinished) {
                        // If the region is finished and can be claimed, add the entire value of the region
                        baseTileScore += effectedRegionEntry.getKey().scoreIfRegionCompletedNow;
                    }
                    else {
                        // Add this weight since we would be placing a tiger in the region as well
                        baseTileScore += scoreDifference;
                    }
                    // Create the move and add it to the moves list
                    Move move = new Move(tileToPlaceName, locationAndOrientation, baseTileScore, true, false,
                                         zoneToPlaceTigerIn);
                    tigerPlacementMoves.add(move);
                }
            }
        }

        for (Move move : tigerPlacementMoves) {
            int scoreAfterAddingBaseScoreOfTile = move.getScore() + baseTileScore;
            move.setScore(scoreAfterAddingBaseScoreOfTile);
        }

        if (response.tigerDenPlacementOption != null) {
            // We could place a tiger in the den, check that out
            int currentScore = 9 - response.tigerDenPlacementOption.numberOfTilesLeftToCompleteDen();
            int scoreAfterAddingBaseTileScore = currentScore + baseTileScore;
            Move move = new Move(tileToPlaceName, locationAndOrientation, scoreAfterAddingBaseTileScore,
                                 true, false, 5);
            tigerPlacementMoves.add(move);
        }

        return tigerPlacementMoves;
    }

    private Move calculateMoveWithoutFollowers(String tileToPlaceName, LocationAndOrientation locationAndOrientation) {
        int tileScore = 0;
        Tile tileToPlace = TileFactory.makeTile(tileToPlaceName);
        tileToPlace.rotateCounterClockwise(locationAndOrientation.getOrientation());
        TilePlacementRequest request = new TilePlacementRequest(playerName, tileToPlace,
                                                                locationAndOrientation.getLocation());
        TilePlacementResponse response = gameInteractor.tryTilePlacement(request);

        // Calculate the base tile score by adding and subtracting ongoing region scores
        for (Map.Entry<RegionInfo, Integer> effectedRegionEntry : response.regionsEffected.entrySet()) {
            // The score difference without playing the tiger
            int scoreDifference = effectedRegionEntry.getValue();
            if (effectedRegionEntry.getKey().dominantPlayerNames.contains(playerName)) {
                // one of our regions is being added to, add the score difference
                tileScore += scoreDifference;
            }
            else if (!effectedRegionEntry.getKey().dominantPlayerNames.isEmpty()) {
                // Someone else has that region, subtract the score difference from ours
                tileScore -= scoreDifference;
            }
        }

        return new Move(tileToPlaceName, locationAndOrientation, tileScore, false, false, -1);
    }


    // MARK: Implementation of AIInterface


    /**
     * Picks out the best move based on the possible locations and orientations
     *
     * @param beginTurn
     * Used to obtain the tile of the
     * @return
     * The Move to be used for the tile placement including Tiger and crocodile Placement
     */
    public Move decideMove(BeginTurnWrapper beginTurn) {
        Tile tileToPlace = TileFactory.makeTile(beginTurn.getTile());
        List<PlayerInfo> updatedPlayerInfos = gameInteractor.getPlayerInfos();

        for (PlayerInfo playerInfo : updatedPlayerInfos) {
            playerInfos.put(playerInfo.playerName, playerInfo);
        }

        ValidMovesResponse validMoves = gameInteractor.getValidMoves(tileToPlace);
        List<LocationAndOrientation> possibleLocationsAndOrientations = validMoves.locationsAndOrientations;
        List<Move> possibleMoves = new ArrayList<>();
        for (LocationAndOrientation locationAndOrientation : possibleLocationsAndOrientations) {
            possibleMoves.add(calculateMoveWithoutFollowers(beginTurn.getTile(), locationAndOrientation));
            if (tileToPlace.canPlaceCrocodile() && playerInfos.get(playerName).remainingCrocodiles > 0) {
                possibleMoves.add(calculateMoveWithCrocodile(beginTurn.getTile(), locationAndOrientation));
            }
            if (playerInfos.get(playerName).remainingTigers > 0) {
                possibleMoves.addAll(calculateMovesWithTigers(beginTurn.getTile(), locationAndOrientation));
            }
        }

        if (possibleMoves.isEmpty()) {
            return null;
        }
        else {
            Move maxMove = possibleMoves.get(0);
            int max = possibleMoves.get(0).getScore();
            for (int i = 1; i < possibleMoves.size(); ++i) {
                int score = possibleMoves.get(i).getScore();
                if (score > max) {
                    maxMove = possibleMoves.get(i);
                    max = score;
                }
            }
            return maxMove;
        }
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }
}
