//package controller;
//
//import entities.board.Tile;
//import entities.board.TileFactory;
//import game.Player;
//import game.GameInteractor;
//import org.junit.Before;
//import org.junit.Test;
//import server.ServerMatchMessageHandler;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Stack;
//
//public class AIControllerTest {
//    private AIController AI;
//    private GameInteractor game;
//    private TileFactory factory;
//
//    @Before
//    public void setup(){
//        String[] myarray = {"JJJJ-",
//                "JJJJX", "JJJJX", "JJJJX", "JJJJX",
//                "JJTJX", "JJTJX",
//                "TTTT-",
//                "TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-","TJTJ-",
//                "TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-","TJJT-",
//                "TJTT-","TJTT-","TJTT-","TJTT-",
//                "LLLL-",
//                "JLLL-","JLLL-","JLLL-","JLLL-",
//                "LLJJ-","LLJJ-","LLJJ-","LLJJ-","LLJJ-",
//                "JLJL-","JLJL-","JLJL-",
//                "LJLJ-","LJLJ-","LJLJ-",
//                "LJJJ-","LJJJ-","LJJJ-","LJJJ-","LJJJ-",
//                "JLLJ-","JLLJ-",
//                "TLJT-",
//                "TLJTP","TLJTP",
//                "JLTT-",
//                "JLTTB","JLTTB",
//                "TLTJ-","TLTJ-",
//                "TLTJD","TLTJD",
//                "TLLL-",
//                "TLTT-",
//                "TLTTP","TLTTP",
//                "TLLT-","TLLT-","TLLT-",
//                "TLLTB","TLLTB",
//                "LJTJ-",
//                "LJTJD","LJTJD",
//                "TLLLC", "TLLLC"};
//
//        List<String> stringList = Arrays.asList(myarray);
//        Collections.shuffle(stringList);
//
//        factory = new TileFactory();
//        Stack<Tile> deck = new Stack<>();
//
//        for (String s: stringList) {
//            Tile t = factory.makeTile(s);
//            deck.push(t);
//        }
//
//        game = new GameInteractor(factory.makeTile("TLTJ-"), deck.size());
//
//        AI = new AIController(game, "odfhgisge", new ServerMatchMessageHandler("Orange"));
//        Player p0 = new Player("Player 0", new AIController(game, "Player 0", new ServerMatchMessageHandler("Orange")));
//        Player p1 = new Player("Player 1", new AIController(game, "Player 1", new ServerMatchMessageHandler("Orange")));
//        game.addPlayer(p0);
//        game.addPlayer(p1);
//    }
//
//    @Test
//    public void testAIChoosesLargestValueMove() throws IOException{
//        game.playGame();
//        game.log();
//    }
//}
