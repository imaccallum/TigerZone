package game;

import ai.AIManager;
import entities.board.Board;
import entities.board.Tiger;
import entities.board.Tile;
import entities.board.TileFactory;
import entities.player.Player;
import exceptions.StackingTigerException;
import exceptions.TigerAlreadyPlacedException;
import game.messaging.info.RegionTigerPlacement;
import game.messaging.info.TigerDenTigerPlacement;
import game.messaging.request.FollowerPlacementRequest;
import game.messaging.request.TilePlacementRequest;
import game.messaging.response.FollowerPlacementResponse;
import game.messaging.response.TilePlacementResponse;

import java.io.*;
import java.util.*;
import java.util.List;

public class GameInteractor {
    private Map<String, Player> players;
    private String playerTurn;

    // *TODO PlayerNotifier notifier;

    private Board board;

    public GameInteractor(Stack<Tile> stack) {
        board = new Board(stack.size(), stack.pop());
    }

    public void addPlayer(Player player) {
        players.put(player.getName(), player);
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

    /**
     * Handle the placement of a follower as described in a follower placement request
     *
     * @param request,
     * The request describing the placement of the follower
     *
     * @return
     * The response to this request as a FollowerPlacementResponse
     */
    public FollowerPlacementResponse handleFollowerPlacementRequest(FollowerPlacementRequest request) {
        if (request.isRegionPlacement()) {
            RegionTigerPlacement placement = request.regionTigerPlacement;
            // Placeholder for push
            return new FollowerPlacementResponse(false, false, false);
        }
        else if (request.isTigerDenPlacement()) {
            boolean success = attemptTigerPlacementInDen(request);
            if (success) {
                players.get(request.playerName).decrementRemainingTigers();
                return new FollowerPlacementResponse(true, false, true);
            }
            else {
                // Failed to place tiger, invalid move
                return new FollowerPlacementResponse(false, false, false);
            }
        }
        else if (request.isStackingTiger()) {
            boolean success = attemptStackingTiger(request);
            if (success) {
                // Valid move
                players.get(request.playerName).decrementRemainingTigers();
                return new FollowerPlacementResponse(true, false, true);
            }
            else {
                // Failed to place tiger, invalid move
                return new FollowerPlacementResponse(false, false, false);
            }
        }
        else if (request.isCrocodilePlacement()) {
            boolean success = attemptCrocodilePlacement(request);
            if (success) {
                players.get(request.playerName).decrementRemainingCrocodiles();
                return new FollowerPlacementResponse(false, true, true);
            }
            else {
                return new FollowerPlacementResponse(false, false, false);
            }
        }
        else {
            // Valid placement, placed nothing
            return new FollowerPlacementResponse(false, false, true);
        }
    }

    /*
    public TilePlacementResponse handleTilePlacementRequest(TilePlacementRequest request) {

    }
    */

    private boolean attemptTigerPlacementInDen(FollowerPlacementRequest request) {
        TigerDenTigerPlacement placement = request.denTigerPlacement;
        if (!players.get(request.playerName).hasRemainingTigers()) {
            // No tigers for the player to place
            return false;
        }
        else if (placement.centerPoint != board.getLastPlacedTile().getLocation()) {
            // Point does not match last placed tile location, placed nothing, invalid move
            return false;
        }
        else if (board.getLastPlacedTile().getDen() == null) {
            // No den on last placed tile, placed nothing, invalid move
            return false;
        }
        else {
            try {
                Tiger tigerToPlace = new Tiger(request.playerName, false);
                board.getLastPlacedTile().getDen().placeTiger(tigerToPlace);
                board.addPlacedTiger(tigerToPlace);
                return true;
            } catch(TigerAlreadyPlacedException exception) {
                System.err.println(exception.getMessage());
                // Tiger already in den, placed nothing, invalid move
                return false;
            }
        }
    }

    private boolean attemptStackingTiger(FollowerPlacementRequest request) {
        if (!players.get(request.playerName).hasRemainingTigers()) {
            // No tigers left to place
            return false;
        }
        Tiger tigerToStack = request.tigerToStack;
        try {
            board.stackTiger(tigerToStack);
            players.get(request.playerName).decrementRemainingTigers();
            return true;
        } catch (StackingTigerException exception) {
            System.err.println(exception.getMessage());
            return false;
        }
    }

    private boolean attemptCrocodilePlacement(FollowerPlacementRequest request) {
        return false;
        /*
        if (players.get(request.playerName).getRemainingCrocodiles() <= 0) {
            // No crocodiles to place
            return false;
        }
        else if (board.getLastPlacedTile().hasCrocodile()) {
            // Corocodile already on tile
            return false;
        }
        else if (board.getLastPlacedTile().canPlaceCrocodile()) {
            return true;
        } else {
            // Not sure what else could go wrong
            return false;
        }
        */
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

        GameInteractor gm = new GameInteractor(deck);

        Player p0 = new Player("Player 0", new AIManager(gm, "Player 0"));
        Player p1 = new Player("Player 1", new AIManager(gm, "Player 1"));
        gm.addPlayer(p0);
        gm.addPlayer(p1);

        while(!deck.empty())
        {
            Tile t = deck.pop();
//            System.out.println(t);

            List<LocationAndOrientation>  tileOptions = gm.board.findValidTilePlacements(t);
            if(tileOptions.size() > 0) {
                int random = (int) (Math.random() * tileOptions.size());
                LocationAndOrientation optimalPlacement = tileOptions.get(random);
                System.out.println("Inserted " + t.getType() + " @ " + optimalPlacement.getLocation() + " with orientation " + optimalPlacement.getOrientation());
                t.rotateCounterClockwise(optimalPlacement.getOrientation());
                if(Math.random() > .9 && p1.hasRemainingTigers()){
                    t.getTileSections().get(0).placeTiger(new Tiger(p1.getName(), false));
                }
                gm.board.place(t, optimalPlacement.getLocation());
            } else {
                System.out.println("No valid moves, discarding tile.");
            }
            if(deck.size() == 0) {
                System.out.println(gm.board.toString());
                gm.board.log();
                System.out.println(gm.board.getNumTiles());
            }

            //         System.out.println("------------------------");

        }

    }
}