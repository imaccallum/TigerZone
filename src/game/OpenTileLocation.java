package game;

import java.awt.*;

public class OpenTileLocation {
    private final Point location;
    private final int orientation;

    public OpenTileLocation(Point location, int orientation) {
        this.location = location;
        this.orientation = orientation;
    }

    public Point getLocation() {
        return location;
    }

    public int getOrientation() {
        return orientation;
    }
}
