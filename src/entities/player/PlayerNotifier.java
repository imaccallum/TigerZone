package entities.player;

import entities.board.Tiger;
import entities.board.Tile;
import game.LocationAndOrientation;
import game.messaging.GameStatusMessage;

import java.util.List;
import java.util.Set;

public interface PlayerNotifier {
    void notifyGameStatus(GameStatusMessage message);
    void startTurn(Tile tileToPlace, List<LocationAndOrientation> possiblePlacements, Set<Tiger> tigersPlacedOnBoard);
}
