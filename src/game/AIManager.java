package game;

import entities.board.Board;
import entities.board.Tile;
import entities.overlay.TileSection;
import entities.player.Player;
import game.scoring.Scorer;

import java.util.*;

public class AIManager {
    private Board board;
    private Map<LocationAndOrientation, Integer> moves;

    public AIManager(Board board) {
        this.board = board;
        moves = new HashMap<>();
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
}
