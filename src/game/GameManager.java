package game;

import entities.board.Board;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.overlay.Region;
import entities.player.Player;

import java.io.IOException;
import java.util.*;

public class GameManager {

    private List<Player> players = new ArrayList<>();
    private int playerTurn;

    // *TODO PlayerNotifier notifier;
    // *TODO RegionLinker regionLinker;

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

    public static void main(String[] args) throws IOException, BadPlacementException {
        Character[] myarray = {'a',
                'b', 'b', 'b', 'b',
                'c', 'c',
                'd',
                'e','e','e','e','e','e','e','e',
                'f','f','f','f','f','f','f','f','f',
                'g','g','g','g',
                'h',
                'i','i','i','i',
                'j','j','j','j','j',
                'k','k','k',
                'l','l','l',
                'm','m','m','m','m',
                'n','n',
                'o',
                'p','p',
                'q',
                'r','r',
                's','s','s',       // WEIRD # FORMAT 1 OR 3?
                't','t',
                'u','u','u',
                'v',
                'w','w',
                'x','x','x',
                'y','y',
                'z',
                '0','0'};
        List<Character> charList = Arrays.asList(myarray);
        Collections.shuffle(charList);

        TileFactory f = new TileFactory();
        Stack<Tile> deck = new Stack<>();

        for (Character c: charList) {
            Tile t = f.makeTile(c);
            deck.push(t);
        }

        Player p0 = new Player("Player 0");
        Player p1 = new Player("Player 1");

        GameManager gm = new GameManager(deck, p0, p1);
        gm.board.insert(gm.board.getTileStack().pop(), 40, 41);
//        gm.board.insert(gm.board.getTileStack().pop(), 0, 0);
//        gm.board.insert(gm.board.getTileStack().pop(), 0, 0);
        System.out.println(gm.board.getTileOptions());
    }

}