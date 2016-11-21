package entities.board;

import entities.overlay.TileSection;
import exceptions.TigerAlreadyPlacedException;

public class Node {

    private Node connectedNode;
    private TileSection tileSection;
    private Tiger tiger;

    public Node() {
        this.connectedNode = null;
    }

    // Getters and Setters

    public TileSection getTileSection() {
        return tileSection;
    }

    public void placeTiger(Tiger tiger) throws TigerAlreadyPlacedException {
        if (this.tiger != null) {
            throw new TigerAlreadyPlacedException("Attempted to place tiger on TileSection already containing tiger");
        }
        this.tiger = tiger;
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

    public Tiger getTiger(){
        return tiger;
    }

    public boolean hasTiger(){
        return getTiger() != null;
    }
}
