package game;

import entities.board.Board;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.overlay.Region;
import entities.overlay.RegionLinker;
import entities.overlay.TileSection;
import entities.player.EndTurnStatus;
import entities.player.Player;
import entities.player.PlayerNotifier;

import java.io.IOException;
import java.util.*;

public class GameManager {
    private List<Player> players;
    private int playerTurn;

    PlayerNotifier notifier = new PlayerNotifier() {
        @Override
        public void notifyTigerPlacementOptions(List<TileSection> tileSections) {
            //flesh these out later
        }

        @Override
        public void notifyEndTurnStatus(EndTurnStatus status) {
            //flesh these out later
        }
    };

    RegionLinker regionLinker = new RegionLinker();

    private Board board;

    public GameManager(Stack<Tile> stack, Player... players) {
        for(Player player : players) {
            this.players.add(player);
        }
        board = new Board(stack);
    }

    public void completeRegion(Region region){
        List<Player> playersToGetScore = region.getDominantPlayers();
        int score = region.getScorer().score(region);
        for(Player p : playersToGetScore){
            p.addToScore(score);
        }
    }

    public Stack<Tile> buildCompleteDeck(){

        Character[] deckchars = {'a','b','b','b','b','c','c','d','e','e','e','e'};
        List<Character> charList = Arrays.asList(deckchars);

        TileFactory f = new TileFactory();
        Stack<Tile> deck = new Stack<>();

        for (Character c: charList) {
            Tile t = f.makeTile(c);
            deck.push(t);
        }
        Collections.shuffle(deck);

        return deck;
    }

    public static void main(String[] args) throws IOException {

    }
    //as needed when creating a new Region, add it to the list of Regions stored in RegionLinker
}