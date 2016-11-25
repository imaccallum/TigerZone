package game;

import ai.AIManager;  // TODO remove
import entities.board.*;
import entities.overlay.Region;
import entities.overlay.TigerDen;
import entities.player.Player;
import exceptions.BadPlacementException;
import exceptions.StackingTigerException;
import exceptions.TigerAlreadyPlacedException;
import game.messaging.GameStatusMessage;
import game.messaging.info.PlayerInfo;
import game.messaging.info.RegionInfo;
import game.messaging.info.RegionTigerPlacement;
import game.messaging.info.TigerDenTigerPlacement;
import game.messaging.request.FollowerPlacementRequest;
import game.messaging.request.TilePlacementRequest;
import game.messaging.response.FollowerPlacementResponse;
import game.messaging.response.TilePlacementResponse;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GameInteractor {
    private Map<String, Player> players;
    private List<Player> playerList;
    private String playerTurn;
    private Stack<Tile> tileStack;

    // *TODO PlayerNotifier notifier;

    private Board board;

    public GameInteractor(Stack<Tile> stack) {
        board = new Board(stack.size(), stack.pop());
        tileStack = stack;
    }

    public void addPlayer(Player player) {
        players.put(player.getName(), player);
        playerList.add(player);
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
     * Plays the game, ending when the tile stack is empty.
     */
    public void playGame() {
        int playerTurn = 0;
        while (!tileStack.isEmpty()) {
            Player player = playerList.get(playerTurn);
            playerTurn = (playerTurn + 1) % playerList.size();
            Tile tileToPlace = tileStack.pop();
            List<LocationAndOrientation> validPlacements = board.findValidTilePlacements(tileToPlace);
            player.getPlayerNotifier().startTurn(tileToPlace, validPlacements);
            GameStatusMessage gameStatusMessage = createGameStatusMessage();
            for (Player notifyingPlayer : playerList) {
                notifyingPlayer.getPlayerNotifier().notifyGameStatus(gameStatusMessage);
            }
        }

        board.regionsAsList().stream().filter(Region::containsTigers).forEach(region -> {
            int score = region.getScorer().score();
            List<String> dominantPlayerNames = region.getDominantPlayerNames();
            for (String name : dominantPlayerNames) {
                players.get(name).addToScore(score);
            }
        });
    }

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
        if (!request.playerName.equals(playerTurn)) {
            // Not the current players turn
            return new FollowerPlacementResponse(false, false, false);
        }
        else if (request.isRegionPlacement()) {
            RegionTigerPlacement placement = request.regionTigerPlacement;
            // TODO Placeholder for push
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

    public TilePlacementResponse handleTilePlacementRequest(TilePlacementRequest request) {
        if (!request.playerName.equals(playerTurn)) {
            // Not the players turn
            return new TilePlacementResponse(false, null, null, null, false);
        }
        else {
            try {
                board.place(request.tileToPlace, request.locationToPlaceAt);
            }
            catch (BadPlacementException exception) {
                System.err.println(exception.getMessage());
                return new TilePlacementResponse(false, null, null, null, false);
            }

            List<RegionTigerPlacement> regionTigerPlacements = new ArrayList<>();
            request.tileToPlace.getTileSections().forEach(tileSection -> {
                Region region = tileSection.getRegion();
                RegionInfo regionInfo = region.getRegionInfo();
                boolean regionJustFinished = region.isFinished();
                regionTigerPlacements.add(new RegionTigerPlacement(regionInfo, regionJustFinished));
            });

            TigerDenTigerPlacement denPlacement = null;
            if (request.tileToPlace.getDen() != null) {
                TigerDen den = request.tileToPlace.getDen();
                denPlacement = new TigerDenTigerPlacement(den.getCenterTileLocation(), den.getRequiredTileLocations());
            }

            List<Tiger> tigersThatCanBeStacked = players.get(request.playerName).getPlacedTigers().stream()
                    .filter(tiger -> !tiger.isStacked())
                    .collect(Collectors.toList());

            boolean canPlaceCrocodile = false; // TODO request.tileToPlace.canPlaceCrocodile();

            return new TilePlacementResponse(true, regionTigerPlacements, denPlacement,
                                             tigersThatCanBeStacked, canPlaceCrocodile);
        }
    }

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
            }
            catch(TigerAlreadyPlacedException exception) {
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
        }
        catch (StackingTigerException exception) {
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

    private GameStatusMessage createGameStatusMessage() {
        List<Region> regions = board.regionsAsList();
        List<RegionInfo> openRegionsInfo = regions.stream()
                .filter(region -> !region.isFinished())
                .map(Region::getRegionInfo)
                .collect(Collectors.toList());
        List<PlayerInfo> playersInfo = playerList.stream()
                .map(Player::getPlayerInfo)
                .collect(Collectors.toList());
        return new GameStatusMessage(openRegionsInfo, playersInfo);
    }

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
            }
            else {
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