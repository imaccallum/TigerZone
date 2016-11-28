package entities.board;

import entities.overlay.TileSection;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private List<Node> connectedNodes;
    private TileSection tileSection;

    public Node() {
        this.connectedNodes = new ArrayList<>();
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

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    public void addConnectedNode(Node node) {
        connectedNodes.add(node);
    }

    public boolean isConnected() {
        return !connectedNodes.isEmpty();
    }
}
