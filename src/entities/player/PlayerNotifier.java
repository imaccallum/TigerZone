package entities.player;

import game.messaging.GameStatusMessage;

public interface PlayerNotifier {
    void notifyGameStatus(GameStatusMessage message);
    void notifyTurn();
}
