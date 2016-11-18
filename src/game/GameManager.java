package game;

import entities.board.Board;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.overlay.Region;
import entities.player.Player;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

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
        board = new Board(stack.size(), stack.pop());
    }

    /*
    public void completeRegion(Region region){
        List<Player> playersToGetScore = region.getDominantPlayers();
        int score = region.getScorer().score(region);
        for(Player p : playersToGetScore){
            p.addToScore(score);
        }
    }
    */

    public Board getBoard() {
        return board;
    }

    public static void main(String[] args) throws IOException, BadPlacementException {

        //region deckArray
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
        //endregion

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


//        TileFactory tf = new TileFactory();
//        Tile t1 = tf.makeTile('a');
//        Tile t2 = tf.makeTile('a');

        while(!deck.empty())
        {
            Tile t = deck.pop();
            System.out.println(t.type);
            List<LocationAndOrientation>  tileOptions = gm.getBoard().findValidTilePlacements(t);
            if(tileOptions.size() > 0) {
                LocationAndOrientation optimalPlacement = tileOptions.get(0);
                System.out.println("Inserted @ " + optimalPlacement.getLocation() + " with orientation " + optimalPlacement.getOrientation());
                t.rotateClockwise(optimalPlacement.getOrientation());
                gm.getBoard().insert(t, optimalPlacement.getLocation());
            } else {
                System.out.println("No valid moves, discarding tile.");
            }
        }
    }
}