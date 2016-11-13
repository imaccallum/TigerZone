package entities.board.Node;

import entities.board.Tiger;
import entities.overlay.Region;
import java.util.ArrayList;

public abstract class Node {
    // List of all possible connections a Node might might have
    private ArrayList<Node> connectedNodes = new ArrayList<>();

    // Store attributes in a shared object so you don't have to iterate through chain to update each nodes values
    private Region region;
    private Tiger tiger;
    private Terrain terrain;

    public Node getConnectedNode(int x) {
        if (x >= connectedNodes.size()) {
            throw new IndexOutOfBoundsException("The index is larger than the amount of connected Nodes for this Node");
        }
        return connectedNodes.get(x);
    }

    public void setConnection(Node node){
        connectedNodes.add(node);
    }

    public void placeTiger(Tiger t) {
        tiger = t;
        region.addTiger(t);
    }

    public void removeTiger() {
        tiger = null;
    }

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
