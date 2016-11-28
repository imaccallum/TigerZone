package wrappers;

import java.awt.*;

public class NonplacementMoveWrapper {
    private String tile;
    private UnplaceableType type;
    private Point tigerLocation;

    public NonplacementMoveWrapper(String tile, UnplaceableType type, Point tigerLocation) {
        this.tile = tile;
        this.type = type;
        this.tigerLocation = tigerLocation;
    }

    public String getTile() {
        return tile;
    }

    public UnplaceableType getType() {
        return type;
    }

    public Point getTigerLocation() {
        return tigerLocation;
    }
}
