package wrappers;

import java.awt.*;

public class PlacementMoveWrapper {

    private String tile;
    private Point location;
    private int orientation;
    private Placement placedObject;
    private int zone;
    private int moveNumber;

    public PlacementMoveWrapper(String tile, Point location, int orientation, int moveNumber) {
        this.tile = tile;
        this.location = location;
        this.orientation = orientation;
        this.moveNumber = moveNumber;
    }

    public void setPlacedObject(Placement placedObject) {
        this.placedObject = placedObject;
    }

    public void setZone(int zone) {
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

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }
}
