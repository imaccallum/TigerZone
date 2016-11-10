package entities.board;

import entities.overlay.Region;

import java.util.ArrayList;

public abstract class Node {

    private ArrayList<Node> connectedNodes = new ArrayList<Node>(); // List of all possible connections a Node might might have
    Region attributes;	// Store attributes in a shared object so you don't have to iterate through chain to update each nodes values
    private Tiger tiger;

    public Node getConnectedNode(int x){
        if (x >= connectedNodes.size()) throw new IndexOutOfBoundsException("The index is larger than the amount of Connected Nodes for this Node");
        return connectedNodes.get(x);
    }

    public void setConnection(Node node){
        connectedNodes.add(node);
    }

    public void placeTiger(Tiger t) {
        tiger = t;
    }

    public void removeTiger() {
        tiger = null;
    }
}
