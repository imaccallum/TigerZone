package entities.overlay;


import entities.board.Board;
import entities.board.Tiger;
import exceptions.TigerAlreadyPlacedException;

import java.awt.Point;
import java.util.Map;

public class TigerDen {
    private Tiger tiger;
    private Point centerTileLocation;
    private Map<Point, Boolean> requiredTileLocations;

    public TigerDen(Point centerTileLocation, Board board) {
        this.centerTileLocation = centerTileLocation;
        Point right = new Point(centerTileLocation.x, centerTileLocation.y + 1);
        requiredTileLocations.put(right, board.getTile(right) != null);
        Point left = new Point(centerTileLocation.x, centerTileLocation.y - 1);
        requiredTileLocations.put(left, board.getTile(left) != null);
        Point above = new Point(centerTileLocation.x + 1, centerTileLocation.y);
        requiredTileLocations.put(above, board.getTile(above) != null);
        Point below = new Point(centerTileLocation.x - 1, centerTileLocation.y);
        requiredTileLocations.put(below, board.getTile(below) != null);
        Point aboveRight = new Point(centerTileLocation.x + 1, centerTileLocation.y + 1);
        requiredTileLocations.put(aboveRight, board.getTile(aboveRight) != null);
        Point aboveLeft = new Point(centerTileLocation.x + 1, centerTileLocation.y - 1);
        requiredTileLocations.put(aboveLeft, board.getTile(aboveLeft) != null);
        Point belowRight = new Point(centerTileLocation.x - 1, centerTileLocation.y + 1);
        requiredTileLocations.put(belowRight, board.getTile(belowRight) != null);
        Point belowLeft = new Point(centerTileLocation.x - 1, centerTileLocation.y - 1);
        requiredTileLocations.put(belowLeft, board.getTile(belowLeft) != null);
    }

    public void placeTiger(Tiger tiger) throws TigerAlreadyPlacedException {
        if (this.tiger != null) {
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

    public boolean isComplete() {
        boolean complete = true;
        // Ensure that all tiles have been placed in the required tiles
        for (Boolean tilePlaced : requiredTileLocations.values()) {
            complete = complete && tilePlaced;
        }
        return complete;
    }
}
