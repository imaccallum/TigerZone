package controller;

import entities.board.Tiger;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.overlay.TileSection;
import entities.player.PlayerNotifier;
import exceptions.ParseFailureException;
import exceptions.BadPlacementException;
import game.GameInteractor;
import game.LocationAndOrientation;
import game.messaging.info.PlayerInfo;
import game.messaging.request.TilePlacementRequest;
import game.messaging.response.TilePlacementResponse;
import game.messaging.response.ValidMovesResponse;
import game.scoring.Scorer;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;
import server.ServerMatchMessageHandler;
import wrappers.BeginTurnWrapper;
import wrappers.PlacementMoveWrapper;

import java.util.*;

public class AIController implements PlayerNotifier {
    private List<Move> moves;
    private GameInteractor gameInteractor;
    private String playerName;
    private Map<String, PlayerInfo> playersInfo;
    private ServerMatchMessageHandler serverMessageHandler;
    private ProtocolMessageParser messageParser;
    private ProtocolMessageBuilder messageBuilder;

    public AIController(GameInteractor gameInteractor, String playerName,
                        ServerMatchMessageHandler serverMessageHandler) {
        this.gameInteractor = gameInteractor;
        this.playerName = playerName;
        this.serverMessageHandler = serverMessageHandler;
        moves = new ArrayList<>();
        this.playersInfo = new HashMap<>();
        messageParser = new ProtocolMessageParser();
        messageBuilder = new ProtocolMessageBuilder();
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
    public void addOptimalScoreForTile(LocationAndOrientation locationAndOrientation, Tile tile) {
        Move moveWithCroc = null;
        Move moveWithoutCroc = null;
        int score;
        boolean crocIsViable = false;

        if (playersInfo.get(playerName).remainingCrocodiles > 0 && tile.canPlaceCrocodile()){
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
                if (tileSection.getRegion().getDominantPlayerNames().isEmpty() && playersInfo.get(playerName).remainingTigers > 0){
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

    public void startTurn() {
        String serverMessage;
        BeginTurnWrapper beginTurn;
        try {
            serverMessage = serverMessageHandler.getServerInput();
            beginTurn = messageParser.parseBeginTurn(serverMessage);
        }
        catch (InterruptedException exception) {
            System.err.println("Interrupted: " + exception.getMessage());
            return;
        } catch (ParseFailureException exception) {
            System.err.println("Parse failure: " + exception.getMessage());
            return;
        }

        Tile tileToPlace = TileFactory.makeTile(beginTurn.getTile());
        ValidMovesResponse validMoves = gameInteractor.getValidMoves(tileToPlace);
        List<LocationAndOrientation> possibleLocations = validMoves.locationsAndOrientations;

        if (possibleLocations.isEmpty()) {
            // Stack a tiger or remove a tiger?
        }
        else {
            // Decide tile placement from lastGameInfoMessages
            //int rand = new Random().nextInt(possibleLocations.size());
            //TilePlacementRequest request;
            for (LocationAndOrientation locationAndOrientation: possibleLocations){
                tileToPlace.rotateCounterClockwise(locationAndOrientation.getOrientation());
               // request =  new TilePlacementRequest(playerName, tileToPlace, locationAndOrientation.getLocation());
                try {
                    gameInteractor.place(tileToPlace, locationAndOrientation.getLocation());
                } catch (BadPlacementException e) {
                    e.printStackTrace();
                }
                // TilePlacementResponse placementResponse = gameInteractor.handleTilePlacementRequest(request);
                addOptimalScoreForTile(locationAndOrientation, tileToPlace);
                gameInteractor.removeLasPlacedTile();
            }

            LocationAndOrientation bestMove = getBestMove();
            tileToPlace.rotateCounterClockwise(bestMove.getOrientation());
            moves.clear();
            TilePlacementRequest request = new TilePlacementRequest(playerName, tileToPlace, bestMove.getLocation());
            TilePlacementResponse response = gameInteractor.handleTilePlacementRequest(request);

            if (!response.wasValid) {
                System.err.println("Tile placed in invalid condition.");
            }

            // Wrap the move, create the server protocol string and output to the server
            PlacementMoveWrapper placementMove = new PlacementMoveWrapper(tileToPlace.getType(), bestMove.getLocation(),
                                                                          bestMove.getOrientation());
            String serverOutput = messageBuilder.messageForMove(placementMove, serverMessageHandler.getGameId());
            serverMessageHandler.setServerOutput(serverOutput);
        }

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
