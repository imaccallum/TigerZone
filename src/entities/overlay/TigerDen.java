package entities.overlay;


import entities.board.Board;
import entities.board.Tiger;
import exceptions.TigerAlreadyPlacedException;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class TigerDen {
    private Tiger tiger;
    private Point centerTileLocation;
    private Map<Point, Boolean> requiredTileLocations;
    private Board board;

    /**
     * Construct a tiger den with a given center point and the board it is on
     */
    public TigerDen() {
        requiredTileLocations = new HashMap<>();
        updateRequiredLocations();
    }

    /**
     * Places a tiger on the board
     *
     * @param tiger,
     * The tiger to be placed in the den
     *
     * @throws TigerAlreadyPlacedException if there is already a tiger present in the den
     */
    public void placeTiger(Tiger tiger) throws TigerAlreadyPlacedException {
        if (this.tiger != null) {
            throw new TigerAlreadyPlacedException("Attempted to place tiger in den that already has tiger");
        }
        else {
            this.tiger = tiger;
        }
    }

    /**
     * See whether or not the tiger den is complete, checks if the surrounding tiles are present
     *
     * @return
     * The boolean representing this state
     */
    public boolean isComplete() {
        boolean complete = true;
        // Ensure that all tiles have been placed in the required tiles
        updateRequiredLocations();
        for (Boolean tilePlaced : requiredTileLocations.values()) {
            complete = complete && tilePlaced;
        }
        return complete;
    }

    // MARK: Getters and Setters

    // Get the center tile location of the den
    public Point getCenterTileLocation() {
        return centerTileLocation;
    }

    // Get the required tile locations to complete the den
    public Map<Point, Boolean> getRequiredTileLocations() {
        return requiredTileLocations;
    }

    // Set the center tile location
    public void setCenterTileLocation(Point centerTileLocation) {
        this.centerTileLocation = centerTileLocation;
    }

    // Set the board
    public void setBoard(Board board) {
        this.board = board;
    }

    // MARK: Private functions

    //
    // Update the required locations by checking the board again to see if the tiles have been placed
    //
    private void updateRequiredLocations() {
        if (centerTileLocation != null) {
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
    }
}
