package entities.overlay;

import entities.board.Node;
import entities.board.Terrain;
import entities.board.Tiger;
import entities.board.Tile;
import exceptions.TigerAlreadyPlacedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * class TileSection
 * Represents a tile section contains:
 * The nodes on the tile
 * The terrain that the tile section is
 * The tile that the TileSection is on
 * The tiger that the TileSection is
 */
public class TileSection {
    private List<Node> nodes;
    private Terrain terrain;
    private Tile tile;
    private Region region;
    private Tiger tiger;
    private Node tigerDisplayNode;

    /**
     * Constructs a tile section with a given terrain.
     *
     * @param terrain,
     * The terrain for the tile section to have.
     */
    public TileSection(Terrain terrain) {
        this.terrain = terrain;
        nodes = new ArrayList<>();
    }

    // HAS TEST - Bookkeeping
    /**
     * Add nodes to the tile section.
     *
     * @param nodesToAdd,
     * A comment separated list of the nodes to add.
     */
    public void addNodes(Node... nodesToAdd) {
        for (Node node : nodesToAdd) {
            node.setTileSection(this);
        }
        nodes.addAll(Arrays.asList(nodesToAdd));
    }

    // HAS TEST - bookkeeping
    /**
     * Create a to string representation of the tile section.
     *
     * @return
     * The string containing the hashCode
     */
    public String toString() {
        return "" + this.hashCode();
    }

    // HAS TEST - bookkeeping
    /**
     * See if any of the nodes contained in the tile section have an open connection, to see if the tile can be
     * connected to anything.
     *
     * @return
     * The boolean status as to whether there are any unconnected nodes.
     */
    public boolean hasOpenConnection() {
        if (nodes.isEmpty()) {
            System.err.println("Queried hasOpenConnections for tile section with no nodes!!");
        }
        for (Node node : nodes) {
            if (!node.isConnected()) {
                return true;
            }
        }
        return false;
    }

    // HAS TEST - bookkeeping
    /**
     * Place a given tiger on the tile section.
     *
     * @param tiger,
     * the tiger that is being placed on the tile section.
     *
     * @throws TigerAlreadyPlacedException
     */
    public void placeTiger(Tiger tiger) throws TigerAlreadyPlacedException {
        if (this.tiger != null) {
            throw new TigerAlreadyPlacedException("Attempted to place tiger on TileSection already containing tiger");
        }
        assert !nodes.isEmpty();
        this.tiger = tiger;
        this.tigerDisplayNode = nodes.get(0);
    }

    /**
     * Removes the tiger from the tile section
     */
    public void removeTiger() {
        tiger = null;
        this.tigerDisplayNode = null;
    }

    // HAS TEST - bookkeeping
    /**
     * Returns if the tile section has a tiger
     *
     * @return
     * The boolean value associated with whether the tiger exists.
     */
    public boolean hasTiger(){
        return getTiger() != null;
    }

    // MARK: Getters and setters

    // Get the terrain
    public Terrain getTerrain() {
        return terrain;
    }

    // Get the tile
    public Tile getTile() {
        return tile;
    }

    // Set the tile
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    // Get the region
    public Region getRegion() {
        return region;
    }

    // Set the region
    public void setRegion(Region region) {
        this.region = region;
    }

    // Get the tiger
    public Tiger getTiger(){
        return tiger;
    }

    // Get the display node for the Tiger if there is one
    public Node getTigerDisplayNode() {
        return tigerDisplayNode;
    }

    // Get the nodes on the tile section
    public List<Node> getNodes() {
        return nodes;
    }
}
