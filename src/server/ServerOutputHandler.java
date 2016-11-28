package server;

import entities.board.Tile;
import game.LocationAndOrientation;
import wrappers.NonplacementMoveWrapper;
import wrappers.PlacementMoveWrapper;

import java.awt.*;

public class ServerOutputHandler {
    private String gameId;
    private ProtocolMessageBuilder protocolMessageBuilder;

    public ServerOutputHandler(String gameId) {
        this.gameId = gameId;
        protocolMessageBuilder = new ProtocolMessageBuilder();
    }

    public void didMakeNonPlacementMove(NonplacementMoveWrapper move) {
        String serverOutput = protocolMessageBuilder.messageForNonplacementMove(move, gameId);

    public void didMakeMove(PlacementMoveWrapper move) {
        String serverOutput = protocolMessageBuilder.messageForMove(move, gameId);
    }
}
