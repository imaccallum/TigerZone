package entities.board.Node;

import entities.board.Tiger;
import entities.overlay.Region;
import entities.overlay.TileSection;

import java.util.ArrayList;

public class Node {
    private Node connectedNode;
    private Region region;
    private Tiger tiger;
    private Terrain terrain;
    private TileSection tileSection;

    public Node(TileSection tileSection, Terrain terrain) {
        this.connectedNode = null;
        this.tiger = null;
        this.tileSection = tileSection;
        this.terrain = terrain;
        this.region = null;
    }
    
    public Node getConnectedNode() {
        return connectedNode;
    }

    public void setConnectedNode(Node node) {
        connectedNode = node;
    }

    public void placeTiger(Tiger t) {
        tiger = t;
        region.addTiger(t);
    }

    public void removeTiger() {
        tiger = null;
    }

    public boolean isConnected() {
        return connectedNode != null;
    }

    // Getters and Setters

    public Tiger getTiger() {
        return tiger;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
}
