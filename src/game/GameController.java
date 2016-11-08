package game;

import entities.board.Tile;
import entities.board.TileCoordinate;
import entities.overlay.Region;
import entities.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameController {
    // *TODO Game game;
    private Tile centerTile;
    private Stack<Tile> remainingTileStack;
    private List<Region> regions;
    private List<TileCoordinate> openTileCoordinates;
    private List<Player> players;
    private int playerTurn;
    // *TODO PlayerNotifier notifier;
    // *TODO RegionLinker regionLinker;

    public GameController(){
        // *TODO game.Create(centerTile); <-- Should do placing of center tile
        // *TODO remainingTileStack = new Tile.randomStackOfTiles() <-- Static method to create random stack of tiles
        // *TODO players = game.getPlayers() <-- return players passed into the current game

        //Initialize open coordinates with all surrounding the center tile of (0,0)
        openTileCoordinates = new ArrayList<>();
        openTileCoordinates.add(new TileCoordinate(-1, 0));
        openTileCoordinates.add(new TileCoordinate(1, 0));
        openTileCoordinates.add(new TileCoordinate(0, 1));
        openTileCoordinates.add(new TileCoordinate(0, -1));

        regions = new ArrayList<>();
    }

    public void placeTile(Tile tile, TileCoordinate coordinate) { // *TODO throws BadTilePlacement
        while(!canBePlaced(tile)){ // *TODO <-- Refactor this into the game's general move flow. On each move, check if tile can be placed
            remainingTileStack.pop();
        }
        // *TODO Place tile
        remainingTileStack.pop();
    }

    public boolean canBePlaced(Tile tile){
        for(TileCoordinate tc : openTileCoordinates){
            if(tile.getCoordinate().equals(tc))
                return true;
        }
        return false;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public List<TileCoordinate> getOpenTileCoordinates() {
        return openTileCoordinates;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }
}
