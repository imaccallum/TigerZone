package wrappers;

import entities.board.Placement;

import java.awt.*;

/**
 * Created by ianmaccallum on 11/27/16.
 */
public class MoveWrapper {

    private String tile;
    private Point location;
    private int orientation;
    private Placement placedObject;
    private int zone;

    public MoveWrapper(String tile, Point location, int orientation, Placement placedObject, int zone) {
        this.tile = tile;
        this.location = location;
        this.orientation = orientation;
        this.placedObject = placedObject;
        this.zone = zone;

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

    public Placement getPlacedObject() {
        return placedObject;
    }

    public int getZone() {
        return zone;
    }
}
