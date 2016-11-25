package entities.player;

import entities.board.Tile;
import game.LocationAndOrientation;
import game.messaging.GameStatusMessage;

import java.util.List;

public interface PlayerNotifier {
    void notifyGameStatus(GameStatusMessage message);
    void startTurn(Tile tileToPlace, List<LocationAndOrientation> possiblePlacements);
}
