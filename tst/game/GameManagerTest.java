package game;

import entities.board.Tile;
import entities.board.TileFactory;
import entities.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

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

        return deck;
    }

}