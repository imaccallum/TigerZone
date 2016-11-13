package entities.board;

import entities.overlay.Region;
import entities.overlay.TileSection;

public class Node {
    private Node connectedNode;
    private Region region;
    private TileSection tileSection;

    public Node(TileSection tileSection, Terrain terrain) {
        this.connectedNode = null;
        this.tileSection = tileSection;
        this.region = null;
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

    // Getters and Setters
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public TileSection getTileSection() {
        return tileSection;
    }
}
