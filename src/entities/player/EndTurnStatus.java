package entities.player;

import entities.board.Tiger;

import java.awt.*;
import java.util.List;

public class EndTurnStatus {
    private List<Tiger> placedTigers;
    private List<PlayerStatus> playerStatuses;
    private List<Point> openTilePlacements;
}
