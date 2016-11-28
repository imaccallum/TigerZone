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
    private List<Move> moves;
    private Stack<GameStatusMessage> lastGameStatusMessages;
    private GameInteractor gameInteractor;
    private String playerName;
    private Map<String, PlayerInfo> playersInfo;

    public AIController(GameInteractor gameInteractor, String playerName) {
        this.gameInteractor = gameInteractor;
        moves = new ArrayList<>();
        lastGameStatusMessages = new Stack<>();
        this.playerName = playerName;
        this.playersInfo = new HashMap<>();
    }

    public LocationAndOrientation getBestMove() {
        int max = moves.get(0).getScore();
        LocationAndOrientation loc = moves.get(0).getLocationAndOrientation();
        for (Move move: moves) {
            if (move.getScore() > max) {
                max = move.getScore();
                loc = move.getLocationAndOrientation();
            }
        }
        return loc;
    }

    // Use function through the Board's findValidTilePlacements()
    public void addOptimalScoreForTile(LocationAndOrientation locationAndOrientation, Tile tile, Player player) {
        boolean needsCroc = false;
        Move moveWithCroc = null;
        Move moveWithoutCroc = null;

        int score;

        if (player.hasRemainingCrocodiles() && tile.canPlaceCrocodile()){
            tile.placeCrocodile();
            moveWithCroc = calculateScoreForTile(tile, player);
        } else {
            moveWithoutCroc = calculateScoreForTile(tile, player);
        }

        // Since you can only place one follower at the time make sure placing the crocodile is worth
        //over placing a tiger.
        if (moveWithCroc.getScore() - moveWithCroc.getScoreTigerGives() <= moveWithoutCroc.getScore()){
            moveWithCroc.setScore(0);
        }

        score = Math.max(moveWithCroc.getScore(), moveWithoutCroc.getScore());

        if (score == moveWithCroc.getScore() && moveWithoutCroc.getScore() != moveWithCroc.getScore()){
            moveWithCroc.setLocationAndOrientation(locationAndOrientation);
            moveWithCroc.setNeedsCrocodile(true);
            moveWithCroc.setNeedsTiger(false);
            moveWithCroc.setScore(moveWithCroc.getScore() - moveWithCroc.getScoreTigerGives());
            moveWithCroc.setTileSection(null);
            moves.add(moveWithCroc);
            return;
        }

        moveWithoutCroc.setLocationAndOrientation(locationAndOrientation);
        moves.add(moveWithoutCroc);
    }

    public Move calculateScoreForTile(Tile tile, Player player){
        int tileScore = 0;
        TileSection sectionWhereTileNeedsToBePlaced  = null;
        int scoreWhereTigerWasPlaced = 0;

        // Assumes you have inserted the tile and will delete it later on if the move is not optimal
        for (TileSection tileSection: tile.getTileSections()) {
            Scorer scorer= tileSection.getRegion().getScorer();
            int regionScore = scorer.score();

            if (tileSection.getRegion().getDominantPlayerNames().contains(player.getName())) {
                // If you are the owner of the region you are trying to expand
                if (tileSection.getRegion().getDominantPlayerNames().size() == 1) {
                    tileScore += regionScore;
                }
            }
            else {
                // If you can claim the region as your own.
                if (tileSection.getRegion().getDominantPlayerNames().isEmpty() && player.getRemainingTigers() > 0){
                    if (regionScore > scoreWhereTigerWasPlaced) {
                        scoreWhereTigerWasPlaced = regionScore;
                        sectionWhereTileNeedsToBePlaced = tileSection;
                    }
                } else {
                    tileScore -= regionScore;
                }
            }
        }
        tileScore += scoreWhereTigerWasPlaced;
        boolean needsTiger = scoreWhereTigerWasPlaced > 0;
        return new Move(null, tileScore, needsTiger, false, scoreWhereTigerWasPlaced ,sectionWhereTileNeedsToBePlaced);
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

    @Override
    public void startTurn(Tile tileToPlace, List<LocationAndOrientation> possiblePlacements, Set<Tiger> tigersPlacedOnBoard) {

    }

    public void startTurn(Tile tileToPlace, List<LocationAndOrientation> possibleLocations,
                          Set<Tiger> tigersPlacedOnBoard) {

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
