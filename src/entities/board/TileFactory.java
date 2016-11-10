package entities.board;

import entities.board.Tile;

public class TileFactory {

    public Tile makeTile(char c) {
        switch (c) {
            case 'a': return makeTileA();
            case 'b': break;
            case 'c': break;
            case 'd': break;
            case 'e': break;
            case 'f': break;
            case 'g': break;
            case 'h': break;
            case 'i': break;
            case 'j': break;
            case 'k': break;
            case 'l': break;
            case 'm': break;
            case 'n': break;
            case 'o': break;
            case 'p': break;
            case 'q': break;
            case 'r': break;
            case 's': break;
            case 't': break;
            case 'u': break;
            case 'v': break;
            case 'w': break;
            case 'x': break;
            case 'y': break;
            case 'z': break;
            default: throw new RuntimeException("Invalid tile identifier");
        }
    }

    private Tile makeTileA() {
        Tile t = new Tile();
        return t;
    }
}