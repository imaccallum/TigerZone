package entities.board.Node;

import entities.overlay.Region;

public class Jungle extends Node {
    public Jungle() {
        this.region = new Region();
        region.addNode(this);
    }
}
