package entities.overlay;

import entities.board.Node;
import entities.board.Terrain;
import entities.board.Tiger;
import entities.board.Tile;

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

    public TileSection(Terrain terrain) {
        this.terrain = terrain;
        nodes = new ArrayList<>();
    }

    public void addNodes(Node... nodesToAdd) {
        for (Node node: nodesToAdd) {
            node.setTileSection(this);
        }
        nodes.addAll(Arrays.asList(nodesToAdd));
    }

    public String toString(){
        return "" + this.hashCode();
    }

    public boolean hasOpenConnection() {
        for (Node node : nodes) {
            if (!node.isConnected()) {
                return true;
            }
        }
        return false;
    }



    // MARK: Getters and setters
    public Terrain getTerrain() {
        return terrain;
    }

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

    public List<Node> getNodes() {
        return nodes;
    }

    public Tiger getTiger() {
        return tiger;
    }

    public void setTiger(Tiger tiger) {
        this.tiger = tiger;
    }
}
