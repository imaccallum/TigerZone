package controller;

import entities.board.Tile;
import entities.overlay.TileSection;
import game.LocationAndOrientation;

public class Move {

    private Tile tile;
    private String tileType;

    private LocationAndOrientation locationAndOrientation;
    private int score;
    private boolean needsTiger;
    private boolean needsCrocodile;

    private TileSection tileSection;
    private int zone;

    private int scoreTigerGives;

    public Move(Tile tile, LocationAndOrientation locationAndOrientation, int score, boolean needsTiger, boolean needsCrocodile, int scoreTigerGives , TileSection tileSection){
        this.locationAndOrientation = locationAndOrientation;
        this.tile = tile;
        tileType = tile.getType();
        this.score = score;
        this.needsTiger = needsTiger;
        this.needsCrocodile = needsCrocodile;
        this.scoreTigerGives = scoreTigerGives;
        this.tileSection = tileSection;
        if(tileSection != null) {
            zone = tile.getTigerZone(tileSection);
        }
    }

    public LocationAndOrientation getLocationAndOrientation() {
        return locationAndOrientation;
    }

    public int getScore() {
        return score;
    }

    public int getScoreTigerGives() {
        return scoreTigerGives;
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

    public TileSection getTileSection() {
        return tileSection;
    }

    public void setLocationAndOrientation(LocationAndOrientation locationAndOrientation){
        this.locationAndOrientation = locationAndOrientation;
    }

    public void setNeedsTiger(boolean needsTiger){
        this.needsTiger = needsTiger;
    }

    public void setScoreTigerGives(int scoreTigerGives){
        this.scoreTigerGives = scoreTigerGives;
    }

    public void setNeedsCrocodile(boolean needsCrocodile){
        this.needsCrocodile = needsCrocodile;
    }

    public void setTileSection(TileSection tileSection) {
        this.tileSection = tileSection;
    }

    public Tile getTile() { return tile; }
    public String getTileType() { return tileType; }
}
