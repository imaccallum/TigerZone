package game;

import java.awt.*;

public class LocationAndOrientation {
    private final Point location;
    private final int orientation;

    public LocationAndOrientation(Point location, int orientation) {
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
