package entities.board.Node;

import entities.board.Tiger;
import entities.overlay.Region;

import java.util.ArrayList;

public abstract class Node {
    // List of all possible connections a Node might might have
    private ArrayList<Node> connectedNodes = new ArrayList<>();

    // Store attributes in a shared object so you don't have to iterate through chain to update each nodes values
    public Region region;
    private Tiger tiger;

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
}
