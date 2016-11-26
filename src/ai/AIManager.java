package ai;

import entities.board.Tiger;
import entities.board.Tile;
import entities.overlay.TileSection;
import entities.player.Player;
import entities.player.PlayerNotifier;
import game.GameInteractor;
import game.LocationAndOrientation;
import game.messaging.GameStatusMessage;
import game.messaging.info.PlayerInfo;
import game.scoring.Scorer;

import java.util.*;

public class AIManager implements PlayerNotifier {
    private Map<LocationAndOrientation, Integer> moves;
    private Stack<GameStatusMessage> lastGameStatusMessages;
    private GameInteractor gameInteractor;
    private String playerName;
    private Map<String, PlayerInfo> playersInfo;

    public AIManager(GameInteractor gameInteractor, String playerName) {
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
    public void parseScoreForPossibleMove(LocationAndOrientation locationAndOrientation, Tile tile, Player player) {
        int score, tileScore, preyScore;
        score = tileScore = preyScore = 0;

        // Assumes you have inserted the tile and will delete it later on if the move is not optimal
        for (TileSection tileSection: tile.getTileSections()) {
            Scorer scorer= tileSection.getRegion().getScorer();
            int regionScore = scorer.score();

            if (tileSection.getRegion().getDominantPlayerNames().contains(player.getName())) {
                if (tileSection.getRegion().getDominantPlayerNames().size() == 1) {
                    tileScore += regionScore;
                }
                else {
                    tileScore += (regionScore / 2);
                }
            }
            else {
                tileScore -= regionScore;
            }
        }

        score += tileScore + preyScore;
        moves.put(locationAndOrientation, score);
    }

    public void clearMoves() {
        moves.clear();
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
                          List<Tiger> tigersThatCanBeStacked) {

        if (possibleLocations.isEmpty()) {
            // Stack a tiger?
        }

        // Decide tile placement from lastGameInfoMessages

        /*
        TilePlacementResponse tilePlacementResponse = gameInteractor.handleTilePlacementRequest(...);

        if (!tilePlacementResponse.wasValid) {
            // forfeit
        }
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
