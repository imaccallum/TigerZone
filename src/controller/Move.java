package controller;

import game.LocationAndOrientation;

public class Move {
    private String tileType;
    private LocationAndOrientation locationAndOrientation;
    private int score;
    private boolean needsTiger;
    private boolean needsCrocodile;
    private int tigerZone;

    public Move(String tileType, LocationAndOrientation locationAndOrientation, int score,
                boolean needsTiger, boolean needsCrocodile, int tigerZone) {
        this.locationAndOrientation = locationAndOrientation;
        this.tileType = tileType;
        this.score = score;
        this.needsTiger = needsTiger;
        this.needsCrocodile = needsCrocodile;
        this.tigerZone = tigerZone;
    }

    public LocationAndOrientation getLocationAndOrientation() {
        return locationAndOrientation;
    }

    public int getScore() {
        return score;
    }

    public boolean needsTiger() {
        return needsTiger;
    }

    public boolean needsCrocodile() {
        return needsCrocodile;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setLocationAndOrientation(LocationAndOrientation locationAndOrientation){
        this.locationAndOrientation = locationAndOrientation;
    }

    public void setNeedsTiger(boolean needsTiger){
        this.needsTiger = needsTiger;
    }

    public void setNeedsCrocodile(boolean needsCrocodile){
        this.needsCrocodile = needsCrocodile;
    }
    
    public String getTileType() { return tileType; }

    public int getTigerZone() {
        return tigerZone;
    }

    public void setTigerZone(int tigerZone) {
        this.tigerZone = tigerZone;
    }
}
