package entities.overlay;


import entities.board.Tiger;
import exceptions.TigerAlreadyPlacedException;

import java.awt.Point;
import java.util.Map;

public class TigerDen {
    private Tiger tiger;
    private Point centerTileLocation;
    private Map<Point, Boolean> requiredTileLocations;

    public void placeTiger(Tiger tiger) throws TigerAlreadyPlacedException {
        if (tiger != null) {
            throw new TigerAlreadyPlacedException("Attempted to place tiger in den that already has tiger");
        }
        else {
            this.tiger = tiger;
        }
    }

    public Point getCenterTileLocation() {
        return centerTileLocation;
    }

    public Map<Point, Boolean> getRequiredTigerLocations() {
        return requiredTileLocations;
    }
}
