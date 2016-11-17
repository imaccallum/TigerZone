package entities.board;

import entities.overlay.TileSection;

public class Node {

    private Node connectedNode;
    private TileSection tileSection;

    public Node() {
        this.connectedNode = null;
    }

    // Getters and Setters

    public TileSection getTileSection() {
        return tileSection;
    }

    public void setTileSection(TileSection tileSection) {
        this.tileSection = tileSection;
    }

    public Node getConnectedNode() {
        return connectedNode;
    }

    public void setConnectedNode(Node node) {
        connectedNode = node;
    }

    public boolean isConnected() {
        return connectedNode != null;
    }
}
