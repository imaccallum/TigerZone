package entities.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Tile {

    //			Edge[0]
    //		Corner[3]	Corner[0]
    //	Edge[3]		Center		Edge[1]
    //		Corner[2]	Corner[1]
    //			Edge[2]
    private final int COUNT = 4;
    private Node[] edges;
    private Node[] corners;
    private Node center;
    private Tile[] tiles; // Adjacent tiles
    private int orientation; // 0 = 0, 1 = 90, 2 = 180, 3 = 270 degrees
    private boolean multiplier;

    public Tile() {
        edges = new Node[COUNT];
        corners = new Node[COUNT];
        tiles = new Tile[COUNT];
    }

    public Tile getTile(int index) {
        return null;
    }

    public Node getCorner(int index) {
        return null;
    }

    public  Node getEdge(int index) {
        return null;
    }

    public void setOrientation(int o) {
        orientation = o;
    }

    public void rotate(int r) {
        orientation = (orientation + r) % COUNT;
    }

    public static void rotate(int[] arr, int order) {
        if (arr == null || order < 0) {
            throw new IllegalArgumentException("Illegal argument!");
        }

        for (int i = 0; i < order; i++) {
            for (int j = arr.length - 1; j > 0; j--) {
                int temp = arr[j];
                arr[j] = arr[j - 1];
                arr[j - 1] = temp;
            }
        }
    }


    public void attachLeft(Tile t) {
        // link corresponding nodes
    }

    public void attachRight(Tile t) {
        // link corresponding nodes
    }

    public void attachBelow(Tile t) {
        // link corresponding nodes
    }
    public void attachAbove(Tile t) {
        // link corresponding nodes
    }

    private void setTile(Tile t, int i) {
        if (i < 0 || i >= COUNT) throw new RuntimeException("Illegal index");

        tiles[i] = t;

    }

    private int inverse(int i) {
        return (i + 2) % COUNT;
    }

}
