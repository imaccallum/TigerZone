package game;

import entities.board.Board;
import entities.board.Tiger;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.player.Player;
import exceptions.TigerAlreadyPlacedException;

import java.io.*;
import java.util.*;
import java.util.List;

public class GameManager {

    private Player p1;
    private Player p2;
    private int playerTurn;

    // *TODO PlayerNotifier notifier;
    // *TODO RegionLinker regionLinker;

    private Board board;

    public GameManager(Stack<Tile> stack, Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
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

    public static void main(String[] args) throws IOException, BadPlacementException, exceptions.BadPlacementException, TigerAlreadyPlacedException {

        //region deckArray
        String[] myarray = {"JJJJ-",
                "JJJJX", "JJJJX", "JJJJX", "JJJJX",
                "JJTJX", "JJTJX",
                "TTTT-",
                "TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-",
                "TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-",
                "TJTT-","TJTT-","TJTT-","TJTT-",
                "LLLL-",
                "JLLL-","JLLL-","JLLL-","JLLL-",
                "LLJJ-","LLJJ-","LLJJ-","LLJJ-","LLJJ-",
                "JLJL-","JLJL-","JLJL-",
                "LJLJ-","LJLJ-","LJLJ-",
                "LJJJ-","LJJJ-","LJJJ-","LJJJ-","LJJJ-",
                "JLLJ-","JLLJ-",
                "TLJT-",
                "TLJTP","TLJTP",
                "JLTT-",
                "JLTTB","JLTTB",
                "TLTJ-","TLTJ-","TLTJ-",       // WEIRD # FORMAT 1 OR 3?
                "TLTJD","TLTJD",
                "TLLL-",
                "TLTT-",
                "TLTTP","TLTTP",
                "TLLT-","TLLT-","TLLT-",
                "TLLTB","TLLTB",
                "LJTJ-",
                "LJTJD","LJTJD",
                "TLLLC", "TLLLC"};
        //endregion

//        Character[] testDeck = {'b', 'a', 'a'};
        List<String> stringList = Arrays.asList(myarray);
        Collections.shuffle(stringList);

        TileFactory f = new TileFactory();
        Stack<Tile> deck = new Stack<>();

        for (String s: stringList) {
            Tile t = f.makeTile(s);
            deck.push(t);
        }

        Player p0 = new Player("Player 0");
        Player p1 = new Player("Player 1");

        GameManager gm = new GameManager(deck, p0, p1);

        while(!deck.empty())
        {
            Tile t = deck.pop();
//            System.out.println(t);

            List<LocationAndOrientation>  tileOptions = gm.getBoard().findValidTilePlacements(t);
            if(tileOptions.size() > 0) {
                int random = (int) (Math.random() * tileOptions.size());
                LocationAndOrientation optimalPlacement = tileOptions.get(random);
                System.out.println("Inserted " + t.type + " @ " + optimalPlacement.getLocation() + " with orientation " + optimalPlacement.getOrientation());
                t.rotateCounterClockwise(optimalPlacement.getOrientation());
                if(Math.random() > .9 && p1.hasRemainingTigers()){
                    t.getTileSections().get(0).placeTiger(new Tiger(p1));
                }
                gm.getBoard().place(t, optimalPlacement.getLocation());
            } else {
                System.out.println("No valid moves, discarding tile.");
            }
            if(deck.size() == 0) {
                gm.getBoard().log();
                System.out.println(gm.getBoard().getFilledValues());
            }

   //         System.out.println("------------------------");

        }

    }
}
