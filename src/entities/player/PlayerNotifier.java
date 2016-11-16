package entities.player;

import entities.overlay.TileSection;

import java.util.List;

public interface PlayerNotifier {
    void notifyTigerPlacementOptions(List<TileSection> tileSections);
    void notifyEndTurnStatus(EndTurnStatus status);
}
