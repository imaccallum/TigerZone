package server;

import entities.board.Placement;
import wrappers.PlacementMoveWrapper;

import java.awt.*;

public class ProtocolMessageBuilder {
    public String joinBuilder(String tournamentPassword){
        return "JOIN " + tournamentPassword;
    }

    public String identityBuilder(String username, String password){
        return "I AM " + username + " " + password;
    }

    public String messageForMove(PlacementMoveWrapper move, String gameId) {
        Point location = move.getLocation();
        if (move.getPlacedObject() == Placement.TIGER) {
            int zone = move.getZone();
            return "GAME " + gameId + " PLACE " + move.getTile() + " AT " + location.x + " " + location.y +
                    " TIGER " + zone;
        }
        else if (move.getPlacedObject() == Placement.CROCODILE) {
            return "GAME " + gameId + " PLACE " + move.getTile() + " AT " + location.x + " " + location.y +
                    " " + "CROCODILE";
        }
        else {
            return "GAME " + gameId + " PLACE " + move.getTile() + " AT " + location.x + " " + location.y + " NONE";
        }
    }

    public String unplaceableTilePass(String gameId, String tileCode){
        return "GAME " + gameId + " TILE " + tileCode + " UNPLACEABLE PASS";
    }

    public String unplaceableTileRetrieveTiger(String gameId, String tileCode, int x, int y){
        return "GAME " + gameId + " TILE " + tileCode + " UNPLACEABLE RETRIEVE TIGER AT " + x + " " + y;
    }

    public String unplaceableTileAddTiger(String gameId, String tileCode, int x, int y){
        return "GAME " + gameId + " TILE " + tileCode + " UNPLACEABLE ADD ANOTHER TIGER TO " + x + " " + y;
    }
}
