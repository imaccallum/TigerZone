package controller;

import entities.board.Tiger;
import entities.board.Tile;
import entities.overlay.TileSection;
import entities.player.Player;
import entities.player.PlayerNotifier;
import game.GameInteractor;
import game.LocationAndOrientation;
import game.messaging.GameStatusMessage;
import game.messaging.info.PlayerInfo;
import game.messaging.request.TilePlacementRequest;
import game.messaging.response.TilePlacementResponse;
import game.scoring.Scorer;

import java.util.*;

public class AIController implements PlayerNotifier {
    private Map<LocationAndOrientation, Integer> moves;
    private Stack<GameStatusMessage> lastGameStatusMessages;
    private GameInteractor gameInteractor;
    private String playerName;
    private Map<String, PlayerInfo> playersInfo;

    public AIController(GameInteractor gameInteractor, String playerName) {
        this.gameInteractor = gameInteractor;
        moves = new HashMap<>();
        lastGameStatusMessages = new Stack<>();
        this.playerName = playerName;
        this.playersInfo = new HashMap<>();
    }

    public LocationAndOrientation getBestMove() {
        int maxScore = Collections.max(moves.values());
        for (Map.Entry<LocationAndOrientation, Integer> entry : moves.entrySet()) {
            if (entry.getValue() == maxScore) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Use function through the Board's findValidTilePlacements()
    public void addOptimalScoreForTile(LocationAndOrientation locationAndOrientation, Tile tile, Player player) {
        int score,scoreWithoutCrocodile, scoreWithCrocodile;
        scoreWithoutCrocodile = scoreWithCrocodile = 0;

        if (player.hasRemainingCrocodiles() && tile.canPlaceCrocodile()){
            tile.placeCrocodile();
            scoreWithCrocodile += calculateScoreForTile(tile, player);
        } else {
            scoreWithoutCrocodile += calculateScoreForTile(tile, player);
        }

        score = Math.max(scoreWithoutCrocodile, scoreWithCrocodile);

        if (score == scoreWithCrocodile) {
            // TODO: 11/26/2016 make sure the player knows that the move involves placing a crocodile
        }

        moves.put(locationAndOrientation, score);
    }

    public void clearMoves() {
        moves.clear();
    }

    public int calculateScoreForTile(Tile tile, Player player) {
        int tileScore = 0;

        // Assumes you have inserted the tile and will delete it later on if the move is not optimal
        for (TileSection tileSection : tile.getTileSections()) {
            Scorer scorer = tileSection.getRegion().getScorer();
            int regionScore = scorer.score();

            if (tileSection.getRegion().getDominantPlayerNames().contains(player.getName())) {
                // If you are the owner of the region you are trying to expand
                if (tileSection.getRegion().getDominantPlayerNames().size() == 1) {
                    tileScore += regionScore;
                }
                // If the ownership is shared
                else {
                    tileScore += (regionScore / 2);
                }
            }
            else {
                // If you can claim the region as your own.
                if (tileSection.getRegion().getDominantPlayerNames().isEmpty() && player.getRemainingTigers() > 0) {
                    // You should place the Tiger in this region
                    tileScore += regionScore;
                } else {

                    tileScore -= regionScore;
                }
            }
        }
        return tileScore;
    }

    // MARK: Implementation of PlayerNotifier

    public void notifyGameStatus(GameStatusMessage gameStatusMessage) {
        this.lastGameStatusMessages.push(gameStatusMessage);
        for (PlayerInfo info : gameStatusMessage.playersInfo) {
            // Update all of the players info in the map
            playersInfo.put(info.playerName, info);
        }
    }

    public void startTurn(Tile tileToPlace, List<LocationAndOrientation> possibleLocations,
                          List<Tiger> tigersPlacedOnBoard) {

        if (possibleLocations.isEmpty()) {
            // Stack a tiger or remove a tiger?
        }
        else {
            // Decide tile placement from lastGameInfoMessages
            int rand = new Random().nextInt(possibleLocations.size());
            LocationAndOrientation locOrient = possibleLocations.get(rand);
            tileToPlace.rotateCounterClockwise(locOrient.getOrientation());
            TilePlacementRequest request = new TilePlacementRequest(playerName, tileToPlace, locOrient.getLocation());
            TilePlacementResponse response = gameInteractor.handleTilePlacementRequest(request);

            if (!response.wasValid) {
                // forfeit
            }
        }



        /*



        */

        PlayerInfo aiPlayerInfo = playersInfo.get(playerName);
        if (aiPlayerInfo.remainingTigers > 0 || aiPlayerInfo.remainingCrocodiles > 0) {
            // Can place a follower

            // DECIDE FOLLOWER PLACEMENT HERE

            // Find a tiger to stack
            Set<Tiger> placedTigers = aiPlayerInfo.placedTigers;

            /*
            FollowerPlacementResponse followerPlacementResponse = gameInteractor.handleFollowerPlacementRequest(...);

            if (!followerPlacementResponse.wasValid) {
                // forfeit
            }
            */
        }
    }
}
