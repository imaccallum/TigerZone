package server;

import entities.board.Placement;
import wrappers.NonplacementMoveWrapper;
import wrappers.PlacementMoveWrapper;
import wrappers.UnplaceableType;

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
            return "GAME " + gameId + " MOVE " + move.getMoveNumber() + " PLACE " + move.getTile() + " AT " +
                    location.x + " " + location.y + " " + move.getOrientation() + " TIGER " + zone;
        }
        else if (move.getPlacedObject() == Placement.CROCODILE) {
            return "GAME " + gameId + " MOVE " + move.getMoveNumber() + " PLACE " + move.getTile() + " AT " +
                    location.x + " " + location.y + " " + move.getOrientation() + " " + "CROCODILE";
        }
        else {
            return "GAME " + gameId + " MOVE " + move.getMoveNumber() + " PLACE " + move.getTile() + " AT " +
                    location.x + " " + location.y + " " + move.getOrientation() + " NONE";
        }
    }

    public String messageForNonplacementMove(NonplacementMoveWrapper move, String gameId) {
        if (move.getType() == UnplaceableType.ADDED_TIGER) {
            Point location = move.getTigerLocation();
            return "GAME " + gameId + " MOVE " + move.getMoveNumber() + " TILE " + move.getTile() +
                    " UNPLACEABLE ADD ANOTHER TIGER TO " + location.x + " " + location.y;
        }
        else if (move.getType() == UnplaceableType.RETRIEVED_TIGER) {
            Point location = move.getTigerLocation();
            return "GAME " + gameId + " MOVE " + move.getMoveNumber() + " TILE " + move.getTile() +
                    " UNPLACEABLE RETRIEVE TIGER AT " + location.x + " " + location.y;
        }
        else {
            return "GAME " + gameId + " MOVE " + move.getMoveNumber() + " TILE " + move.getTile() +
                    " UNPLACEABLE PASS";
        }
    }
}
