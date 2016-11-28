package controller;

import entities.player.PlayerNotifier;
import game.messaging.GameStatusMessage;

public class ServerController implements PlayerNotifier {
    @Override
    public void notifyGameStatus(GameStatusMessage message) {

    }

    @Override
    public void startTurn() {
        // Send to server to inform of turn
    }
}
