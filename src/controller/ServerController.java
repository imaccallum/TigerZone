package controller;

import entities.player.PlayerNotifier;
import exceptions.ParseFailureException;
import server.ProtocolMessageBuilder;
import server.ProtocolMessageParser;
import server.ServerMatchMessageHandler;
import wrappers.ConfirmedMoveWrapper;
import wrappers.NonplacementMoveWrapper;
import wrappers.PlacementMoveWrapper;

public class ServerController implements PlayerNotifier {
    private ServerMatchMessageHandler matchMessageHandler;
    private ProtocolMessageBuilder messageBuilder;
    private ProtocolMessageParser messageParser;

    public ServerController(ServerMatchMessageHandler matchMessageHandler) {
        this.matchMessageHandler = matchMessageHandler;
        messageBuilder = new ProtocolMessageBuilder();
        messageParser = new ProtocolMessageParser();
    }

    @Override
    public void startTurn() {
        String turnConfirmed = "";
        try {
            turnConfirmed = matchMessageHandler.getServerInput();
        } catch (InterruptedException exception) {
            System.err.println(exception.getMessage());
        }

        ConfirmedMoveWrapper confirmedMove;
        try {
            confirmedMove = messageParser.parseConfirmMove(turnConfirmed);
        }
        catch (ParseFailureException exception) {
            System.out.println("Failed to parse placement turn");
        }
    }
}
