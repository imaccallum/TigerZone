package wrappers;

import java.awt.*;

/**
 * Created by ianmaccallum on 11/27/16.
 */
public class MoveWrapper {

    private String tile;
    private Point location;
    private int orientation;

    public MoveWrapper(String tile, Point location, int orientation) {
        this.tile = tile;
        this.location = location;
        this.orientation = orientation;
    }

    public String getTile() {
        return tile;
    }

    public Point getLocation() {
        return location;
    }

    public int getOrientation() {
        return orientation;
    }
}
