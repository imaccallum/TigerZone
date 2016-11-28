package server;

import entities.board.Tile;
import game.LocationAndOrientation;
import wrappers.PlacementMoveWrapper;

import java.awt.*;

public class ServerOutputHandler {
    private String gameId;
    private ProtocolMessageBuilder protocolMessageBuilder;

    public ServerOutputHandler(String gameId) {
        this.gameId = gameId;
        protocolMessageBuilder = new ProtocolMessageBuilder();
    }

    public void didPlaceTile(Tile tile, LocationAndOrientation locationAndOrientation) {
        String serverOutput = "";
        if (tile.hasTiger()) {
            int zone = tile.getTigerZone();

        }
        else if (tile.hasCrocodile()) {
            Point serverLocation = tile.getServerLocation();
            serverOutput = protocolMessageBuilder.placeTileWithCrocodile(gameId, tile.getType(), serverLocation.x,
                                                                         serverLocation.y, tile.getOrientation());
        }
        else {

        }
    }

    public void didMakeMove(PlacementMoveWrapper move) {
        String serverOutput = protocolMessageBuilder.messageForMove(move, gameId);

    }
}
