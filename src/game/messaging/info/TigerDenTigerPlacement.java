package game.messaging.info;

import java.awt.*;
import java.util.Map;

public class TigerDenTigerPlacement {
    public final Point centerPoint;
    public final Map<Point, Boolean> surroundingPointStatuses;

    public TigerDenTigerPlacement(Point centerPoint, Map<Point, Boolean> surroundingPointStatuses) {
        this.centerPoint = centerPoint;
        this.surroundingPointStatuses = surroundingPointStatuses;
    }

    /**
     * Get the number of surrounding tiles that must be placed to complete the den
     *
     * @return
     * An integer count of the number of tiles left
     */
    public int numberOfTilesLeftToCompleteDen() {
        int numberOfTilesRemaining = 0;
        for (Map.Entry<Point, Boolean> entry : surroundingPointStatuses.entrySet()) {
            if (!entry.getValue()) {
                ++numberOfTilesRemaining;
            }
        }
        return numberOfTilesRemaining;
    }
}
