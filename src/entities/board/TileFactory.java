package entities.board;

import entities.board.Tile;

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
        Tile t = new Tile();
        //create all the nodes for the tile, and add the nodes onto the tile in the appropriate spots in the arrays of Tile
        //since the node arrays are private, probably need to add some sort of set() function in Tile to update those arrays
        //make sure to account for connections (use node.setConnection(Node node)), regions (use node.setRegion(Region region); this is just creating the tiles, no need to worry about linking them yet)
        //in all of these, make sure to pass a reference to t when making the nodes for the tile
        //we'll add a reference to Tile in node so that we can use that in Region to check if it's complete
        return t;
    }

    private Tile makeTileB() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileC() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileD() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileE() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileF() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileG() {
        Tile t = new Tile();
        return t;
    }

    private Tile makeTileH() {
        Tile t = new Tile();
        return t;
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
        Tile t = new Tile();
        return t;
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
