package controller;

import entities.board.Tiger;
import entities.board.Tile;
import entities.player.PlayerNotifier;
import game.LocationAndOrientation;
import game.messaging.GameStatusMessage;

import java.util.List;
import java.util.Set;

public class ServerController implements PlayerNotifier {
    @Override
    public void notifyGameStatus(GameStatusMessage message) {

    }

    @Override
    public void startTurn(Tile tileToPlace, List<LocationAndOrientation> possiblePlacements,
                          Set<Tiger> tigersPlacedOnBoard) {
        // Send to server to inform of turn
    }
}
