package entities.overlay;

import entities.board.Node.Node;
import entities.board.Node.Terrain;
import entities.board.Tiger;
import entities.board.Tile;
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

    public boolean isComplete() {
        if (terrain == Terrain.DEN) {
            Tile[] adjacentTiles = tile.getAdjacentTiles();
            if (adjacentTiles[0] == null || adjacentTiles[1] == null ||
                adjacentTiles[2] == null || adjacentTiles[3] == null) {
                return false;
            }

            Tile leftTile = adjacentTiles[0];
            Tile rightTile = adjacentTiles[2];
            if (leftTile.getAdjacentTiles()[1] == null || leftTile.getAdjacentTiles()[3] == null ||
                rightTile.getAdjacentTiles()[1] == null || rightTile.getAdjacentTiles()[3] == null) {
                return false;
            }
        } else {
            for (Node node : nodes) {
                if (!node.isConnected()) {
                    return false;
                }
            }
            return true;
        }
        return false;
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
