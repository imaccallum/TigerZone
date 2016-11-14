package game;

import entities.board.Tile;
import entities.board.TileFactory;
import entities.player.Player;
import org.junit.Before;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by ianmaccallum on 11/13/16.
 */
public class GameManagerTest {
    private GameManager gameManager;

    @Before
    public void setup() {

        Stack<Tile> deck = setupDeck();
        Player p0 = new Player("Player 0");
        Player p1 = new Player("Player 1");

        GameManager gm = new GameManager(deck, p0, p1);
    }

    public Stack<Tile> setupDeck() {

        Character[] myarray = {'a', 'b', 'c', 'd'};
        List<Character> charList = Arrays.asList(myarray);
        Collections.shuffle(charList);

        TileFactory f = new TileFactory();
        Stack<Tile> deck = new Stack<>();

        for (Character c: charList) {
            Tile t = f.makeTile(c);
            deck.push(t);
        }

        return deck;
    }
}