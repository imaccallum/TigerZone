package game;

import ai.AIManager;
import entities.board.Board;
import entities.board.Tiger;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.player.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws Exception {

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

        GameInteractor gm = new GameInteractor(deck);

        Player p0 = new Player("Player 0", new AIManager(gm, "Player 0"));
        Player p1 = new Player("Player 1", new AIManager(gm, "Player 1"));
        gm.addPlayer(p0);
        gm.addPlayer(p1);

        while(!deck.empty())
        {
            Tile t = deck.pop();
            Board board = gm.getBoard();
//            System.out.println(t);

            List<LocationAndOrientation>  tileOptions = board.findValidTilePlacements(t);
            if(tileOptions.size() > 0) {
                int random = (int) (Math.random() * tileOptions.size());
                LocationAndOrientation optimalPlacement = tileOptions.get(random);
                System.out.println("Inserted " + t.getType() + " @ " + optimalPlacement.getLocation() + " with orientation " + optimalPlacement.getOrientation());
                t.rotateCounterClockwise(optimalPlacement.getOrientation());
                if(Math.random() > .9 && p1.hasRemainingTigers()){
                    t.getTileSections().get(0).placeTiger(new Tiger(p1.getName(), false));
                }
                board.place(t, optimalPlacement.getLocation());
            }
            else {
                System.out.println("No valid moves, discarding tile.");
            }
            if(deck.size() == 0) {
                System.out.println(board.toString());
                board.log();
                System.out.println(board.getNumTiles());
            }

            //         System.out.println("------------------------");

        }
    }
}
