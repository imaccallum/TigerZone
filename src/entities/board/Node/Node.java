package entities.board.Node;

import entities.board.Tiger;
import entities.overlay.Region;
import java.util.ArrayList;

public class Node {
    // List of all possible connections a Node might might have
    private Node connectedNode = null;

    // Store attributes in a shared object so you don't have to iterate through chain to update each nodes values
    private Region region;
    private Tiger tiger;
    private Terrain terrain;

    public Node(TileSection tilesection, Terrain terrain, Node connectedNode){
        tiger = null;
        this.tilesection = tilesection;
        this.terrain = terrain;
        this.connectedNode = connectedNode;
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
