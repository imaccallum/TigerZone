package entities.board;

import entities.overlay.Region;
import entities.overlay.TileSection;

public class Node {
    private Node connectedNode;
    private Region region;
    private Tiger tiger;
    private TileSection tileSection;

    public Node(TileSection tileSection, Terrain terrain) {
        this.connectedNode = null;
        this.tiger = null;
        this.tileSection = tileSection;
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
}
