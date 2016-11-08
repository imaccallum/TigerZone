package entities.board;

import entities.overlay.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Class TileSection
// Represents a section on the tile which has a given terrain, and node numbers which it connects to for that tile.
// Also keeps track of the tile it is on and the region it is a part of in the game's overlay.
// Has the ability to have a follower placed on it.
public class TileSection {
    private List<Integer> nodeNumbers;
    private TerrainType terrain;
    private Tile tile;
    private Region region;

    private Follower follower;

    private int pointMultiplier;

    public TileSection(TerrainType terrain) {
        this.terrain = terrain;
        nodeNumbers = new ArrayList<>();
        this.pointMultiplier = 1;
    }

    public void addNodeNumbers(Integer... nodeNumbersToAdd) {
        nodeNumbers.addAll(Arrays.asList(nodeNumbersToAdd));
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public boolean contains(int nodeNumber) {
        return nodeNumbers.contains(nodeNumber);
    }

    public boolean hasFollower() {
        return follower != null;
    }

    // MARK: Getters and setters

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getPointMultiplier() {
        return pointMultiplier;
    }

    public void setPointMultiplier(int pointMultiplier) {
        this.pointMultiplier = pointMultiplier;
    }

    public Follower getFollower() {
        return follower;
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }

    public List<Integer> getNodeNumbers() {
        return nodeNumbers;
    }
}
