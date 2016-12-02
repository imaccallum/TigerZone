package controller;

import entities.board.Tile;
import entities.board.TileFactory;
import entities.overlay.TileSection;
import exceptions.BadPlacementException;
import game.GameInteractor;
import game.LocationAndOrientation;
import game.messaging.info.PlayerInfo;
import game.messaging.response.ValidMovesResponse;
import game.scoring.Scorer;
import server.ProtocolMessageBuilder;
import server.ServerMatchMessageHandler;
import wrappers.BeginTurnWrapper;

import java.util.*;

public class AIController implements AIInterface {
    private List<Move> moves;
    private GameInteractor gameInteractor;
    private Move bestMove;
    private String playerName;
    private Map<String, PlayerInfo> playersInfo;
    private ProtocolMessageBuilder messageBuilder;


    public AIController(GameInteractor gameInteractor, String playerName) {
        this.gameInteractor = gameInteractor;
        this.playerName = playerName;
        moves = new ArrayList<>();
        this.playersInfo = new HashMap<>();
        messageBuilder = new ProtocolMessageBuilder();
    }

    /**
     * Gets the move with the maximum score
     *
     * @return
     * Returns the bet Move
     */
    private Move calculateBestMove() {
        int max = moves.get(0).getScore();
        Move best = moves.get(0);
        for (Move move: moves) {
            if (move.getScore() > max) {
                max = move.getScore();
                best = move;
            }
        }
        return best;
    }

    /**
     * Meant to add the score of a tile at a certain location & orientation
     * to list that keeps the optimal move
     *
     * @param locationAndOrientation
     * The orientation of at which the tile is being scored
     * @param tile
     * The tile being scored
     */
    public void addOptimalScoreForTile(LocationAndOrientation locationAndOrientation, Tile tile) {
        Move moveWithCroc = null;
        Move moveWithoutCroc = null;
        int score;
        boolean crocIsViable = false;

        if (gameInteractor.getPlayers().get(playerName).hasRemainingCrocodiles() && tile.canPlaceCrocodile()) {
            tile.placeCrocodile();
            moveWithCroc = calculateScoreForTile(tile);
            crocIsViable = true;
        }

        moveWithoutCroc = calculateScoreForTile(tile);

        if (crocIsViable){
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
        }

        moveWithoutCroc.setLocationAndOrientation(locationAndOrientation);
        moves.add(moveWithoutCroc);
    }

    /**
     * Calculates the score for a certain tile once it has been inserted
     * including possible ownership and Tiger placement
     *
     * @param tile
     * The tile that is being scored
     * @return
     * The preliminary Move that contains info about Tiger placement and possible score
     */
    public Move calculateScoreForTile(Tile tile){
        int tileScore = 0;
        TileSection sectionWhereTileNeedsToBePlaced  = null;
        int scoreWhereTigerWasPlaced = 0;

        // Assumes you have inserted the tile and will delete it later on if the move is not optimal
        for (TileSection tileSection: tile.getTileSections()) {
            Scorer scorer= tileSection.getRegion().getScorer();
            int regionScore = scorer.score();

            if (tileSection.getRegion().getDominantPlayerNames().contains(playerName)) {
                // If you are the owner of the region you are trying to expand
                if (tileSection.getRegion().getDominantPlayerNames().size() == 1) {
                    tileScore += regionScore;
                }
            }
            else {
                // If you can claim the region as your own.
                if (tileSection.getRegion().getDominantPlayerNames().isEmpty() && gameInteractor.getPlayers().get(playerName).hasRemainingTigers()) {
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
        boolean needsTiger = false;

        return new Move(tile, null, tileScore, needsTiger, false, scoreWhereTigerWasPlaced ,sectionWhereTileNeedsToBePlaced);
    }

    public void clearMoves() {
        moves.clear();
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
        ValidMovesResponse validMoves = gameInteractor.getValidMoves(tileToPlace);
        List<LocationAndOrientation> possibleLocations = validMoves.locationsAndOrientations;
        if (possibleLocations.isEmpty()) {
            return new Move(tileToPlace, null, 0, false, false, 0, null);
        }
        else {
            return new Move(tileToPlace, possibleLocations.get(0), 0, false, false, 0, null);
        }
        /*

        if (possibleLocations.isEmpty()) {
            // Stack a tiger or remove a tiger?

            // Create no moves server commands
            // else, bestMove contains all info needed to build server commands
            return null;
        }
        else {
            // Decide tile placement from lastGameInfoMessages
            //int rand = new Random().nextInt(possibleLocations.size());
            //TilePlacementRequest request;
            for (LocationAndOrientation locationAndOrientation: possibleLocations){
                System.out.println("Tile " + tileToPlace.getType() + " location (" + locationAndOrientation.getLocation().getX() + " , " + locationAndOrientation.getLocation().getY() + ")  orientation: " + locationAndOrientation.getOrientation());
                tileToPlace.rotateCounterClockwise(locationAndOrientation.getOrientation());
               // request =  new TilePlacementRequest(playerName, tileToPlace, locationAndOrientation.getLocation());
                try {
                    gameInteractor.place(tileToPlace, locationAndOrientation.getLocation());

                    addOptimalScoreForTile(locationAndOrientation, tileToPlace);
                    // Reset rotation
                    gameInteractor.removeLastPlacedTile();

                } catch (BadPlacementException e) {
                    e.printStackTrace();
                }
                // TilePlacementResponse placementResponse = gameInteractor.handleTilePlacementRequest(request);
                tileToPlace.rotateCounterClockwise(4 - locationAndOrientation.getOrientation());
            }

            bestMove = calculateBestMove();
//            tileToPlace.rotateCounterClockwise(bestMove.getLocationAndOrientation().getOrientation());

            moves.clear();
            return bestMove;


//            TilePlacementRequest request = new TilePlacementRequest(playerName, tileToPlace,
//                    bestMove.getLocationAndOrientation().getLocation());
//            TilePlacementResponse response = gameInteractor.handleTilePlacementRequest(request);
//
//            if (!response.wasValid) {
//                System.err.println("Tile placed in invalid condition.");
//            }
//
//
            // Wrap the move, create the server protocol string and output to the server
//            PlacementMoveWrapper placementMove = new PlacementMoveWrapper(tileToPlace.getType(),
//                    bestMove.getLocationAndOrientation().getLocation(), bestMove.getLocationAndOrientation().getOrientation());
//            String serverOutput = messageBuilder.messageForMove(placementMove, serverMessageHandler.getGameId());
//            serverMessageHandler.addServerOutput(serverOutput);
        }
        */
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }
}
