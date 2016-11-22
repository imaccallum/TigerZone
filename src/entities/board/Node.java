package entities.board;

import entities.overlay.TileSection;

public class Node {

    private Node connectedNode;
    private TileSection tileSection;

    public Node() {
        this.connectedNode = null;
    }

    /**
     * Get whether the tiger is being represented as the tiger display node for the tile section
     *
     * @return
     * Returns the boolean representing this state.
     */
    public boolean isTigerDisplayNode() {
        if (tileSection.hasTiger()) {
            return tileSection.getTigerDisplayNode().equals(this);
        } else {
            return false;
        }
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
