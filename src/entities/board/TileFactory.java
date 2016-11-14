package entities.board;

import entities.overlay.TileSection;

public class TileFactory {

    public Tile makeTile(char c) {
        switch (c) {
            case 'a': return makeTileA();
            case 'b': return makeTileB();
            case 'c': return makeTileC();
            case 'd': return makeTileD();
            case 'e': return makeTileE();
            case 'f': return makeTileF();
            case 'g': return makeTileG();
            case 'h': return makeTileH();
            case 'i': return makeTileI();
            case 'j': return makeTileJ();
            case 'k': return makeTileK();
            case 'l': return makeTileL();
            case 'm': return makeTileM();
            case 'n': return makeTileN();
            case 'o': return makeTileO();
            case 'p': return makeTileP();
            case 'q': return makeTileQ();
            case 'r': return makeTileR();
            case 's': return makeTileS();
            case 't': return makeTileT();
            case 'u': return makeTileU();
            case 'v': return makeTileV();
            case 'w': return makeTileW();
            case 'x': return makeTileX();
            default: throw new RuntimeException("Invalid tile identifier");
        }
    }

    private Tile makeTileA() {
        // Initialize the Tile and Sections with respective terrain types
        Tile tile = new Tile();
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection den = new TileSection(Terrain.DEN);
        TileSection trail = new TileSection(Terrain.TRAIL);

        // Create Nodes counterclockwise
        Node e_zero = new Node();
        Node e_one = new Node();
        Node c_two = new Node();
        Node e_two = new Node();
        Node c_three = new Node();
        Node e_three = new Node();
        Node center = new Node();

        // Add nodes to the respective Sections
        jungle.addNodes(e_zero, e_one, c_two, c_three, e_three);
        den.addNodes(center);
        trail.addNodes(e_two);

        // Set the sections up in the tile
        tile.setEdge(e_zero, 0);
        tile.setEdge(e_one, 1);
        tile.setEdge(e_two, 2);
        tile.setEdge(e_three, 3);

        tile.setCorner(c_two, 2);
        tile.setCorner(c_three, 3);

        tile.setCenter(center);

        // Add the sections to the tile
        tile.addTileSections(jungle, den, trail);

        return tile;
    }

    private Tile makeTileB() {
        Tile tile = new Tile();
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection den = new TileSection(Terrain.DEN);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();
        Node center = new Node();

        jungle.addNodes(e_zero, e_one, e_two, e_three);
        den.addNodes(center);

        tile.setEdge(e_zero, 0);
        tile.setEdge(e_one, 1);
        tile.setEdge(e_two, 2);
        tile.setEdge(e_three, 3);
        tile.setCenter(center);

        tile.addTileSections(jungle, den);

        return tile;
    }

    private Tile makeTileC() {
        Tile tile = new Tile();
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        lake.addNodes(e_zero, e_one, e_two, e_three);

        tile.setEdge(e_zero, 0);
        tile.setEdge(e_one, 1);
        tile.setEdge(e_two, 2);
        tile.setEdge(e_three, 3);

        tile.addTileSections(lake);

        return tile;
    }

    private Tile makeTileD() {
        Tile tile = new Tile();
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection jungle2 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle.addNodes(c_zero, e_three, c_three);
        trail.addNodes(e_zero, e_two);
        jungle2.addNodes(c_one, c_two);
        lake.addNodes(e_one);

        tile.setEdge(e_zero, 0);
        tile.setEdge(e_one, 1);
        tile.setEdge(e_two, 2);
        tile.setEdge(e_three, 3);

        tile.setCorner(c_zero, 0);
        tile.setCorner(c_one, 1);
        tile.setCorner(c_two, 2);
        tile.setCorner(c_three, 3);

        tile.addTileSections(jungle, trail, jungle2, lake);

        return tile;
    }

    private Tile makeTileE() {
        Tile tile = new Tile();
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();

        jungle.addNodes(c_zero, e_three, e_two, e_one, c_one);
        lake.addNodes(e_zero);

        tile.setEdge(e_zero, 0);
        tile.setEdge(e_one, 1);
        tile.setEdge(e_two, 2);
        tile.setEdge(e_three, 3);

        tile.setCorner(c_zero, 0);
        tile.setCorner(c_one, 1);

        tile.addTileSections(jungle, lake);

        return tile;
    }

    private Tile makeTileF() {
        Tile tile = new Tile();
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection jungle2 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        jungle.addNodes(e_zero);
        jungle2.addNodes(e_two);
        lake.addNodes(e_one, e_three);

        tile.setEdge(e_zero, 0);
        tile.setEdge(e_one, 1);
        tile.setEdge(e_two, 2);
        tile.setEdge(e_three, 3);

        tile.addTileSections(jungle, jungle2, lake);

        return tile;
    }

    private Tile makeTileG() {
        Tile tile = new Tile();
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection jungle2 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        jungle.addNodes(e_three);
        jungle2.addNodes(e_one);
        lake.addNodes(e_zero, e_two);

        tile.setEdge(e_zero, 0);
        tile.setEdge(e_one, 1);
        tile.setEdge(e_two, 2);
        tile.setEdge(e_three, 3);

        tile.addTileSections(jungle, jungle2, lake);

        return tile;
    }

    private Tile makeTileH() {
        Tile tile = new Tile();
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection lake2 = new TileSection(Terrain.LAKE);


        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        jungle.addNodes(e_zero, e_two);
        lake.addNodes(e_one);
        lake2.addNodes(e_three);

        tile.setEdge(e_zero, 0);
        tile.setEdge(e_one, 1);
        tile.setEdge(e_two, 2);
        tile.setEdge(e_three, 3);

        tile.addTileSections(jungle, lake, lake2);

        return tile;
    }

    private Tile makeTileI() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileJ() {
        Tile t = new Tile();
        return t;

    }

    private Tile makeTileK() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileL() {
        Tile tile = new Tile();
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection trail1 = new TileSection(Terrain.TRAIL);
        TileSection trail2 = new TileSection(Terrain.TRAIL);
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection jungle2 = new TileSection(Terrain.JUNGLE);
        TileSection jungle3 = new TileSection(Terrain.JUNGLE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        lake.addNodes(e_one);
        jungle.addNodes(c_zero);
        jungle.addNodes(c_one);
        jungle.addNodes(c_two);
        jungle.addNodes(c_three);
        trail.addNodes(e_zero);
        trail.addNodes(e_three);
        trail.addNodes(e_two);

        tile.setEdge(e_zero, 0);
        tile.setEdge(e_one, 1);
        tile.setEdge(e_two, 2);
        tile.setEdge(e_three, 3);

        tile.setCorner(e_zero, 0);
        tile.setCorner(e_one, 1);
        tile.setCorner(e_two, 2);
        tile.setCorner(e_three, 3);

        tile.addTileSections(lake, trail, trail1, trail2, jungle, jungle1, jungle2, jungle3);

        return tile;
    }

    private Tile makeTileM() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileN() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileO() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileP() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileQ() {
        Tile t = new Tile();
        return t;
    }
    private Tile makeTileR() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileS() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileT() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileU() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileV() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileW() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileX() {
        Tile t = new Tile();
        return t;
    }
}
