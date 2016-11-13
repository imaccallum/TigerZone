package entities.board;

import entities.board.Node.Node;
import entities.board.Node.Terrain;
import entities.overlay.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Class TileSection
// Represents a section on the tile which has a given terrain, and node numbers which it connects to for that tile.
// Also keeps track of the tile it is on and the region it is a part of in the game's overlay.
// Has the ability to have a tiger placed on it.
public class TileSection {
    private List<Node> nodes;
    private Terrain terrain;
    private Tile tile;
    private Region region;
    private Tiger tiger;
    private int pointMultiplier;


    public TileSection(Terrain terrain) {
        this.terrain = terrain;
        nodes = new ArrayList<>();
        this.pointMultiplier = 1;
    }

    public void addNodeNumbers(Node... nodesToAdd) {
        nodes.addAll(Arrays.asList(nodesToAdd));
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public boolean contains(Node node) {
        return nodes.contains(node);
    }

    public boolean hasTiger() {
        return tiger != null;
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

    public Tiger getTiger() {
        return tiger;
    }

    public void setTiger(Tiger tiger) {
        this.tiger = tiger;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
