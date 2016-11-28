package wrappers;

import java.awt.*;

public class NonplacementMoveWrapper {
    private String tile;
    private UnplaceableType type;
    private Point tigerLocation;
    private int moveNumber;

    public NonplacementMoveWrapper(String tile, int moveNumber) {
        this.tile = tile;
        this.moveNumber = moveNumber;
    }

    public void setType(UnplaceableType type) {
        this.type = type;
    }

    public void setTigerLocation(Point tigerLocation) {
        this.tigerLocation = tigerLocation;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
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

    public int getMoveNumber() {
        return moveNumber;
    }
}
