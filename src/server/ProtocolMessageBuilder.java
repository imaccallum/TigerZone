package server;

public class ProtocolMessageBuilder {
    public String joinBuilder(String tournamentPassword){
        return "JOIN " + tournamentPassword;
    }

    public String identityBuilder(String username, String password){
        return "I AM " + username + " " + password;
    }

    public String placeTile(String gameId, String tileCode, int x, int y, int orientation){
        return "GAME " + gameId + " PLACE " + tileCode + " AT " + x + " " + y + " NONE";
    }

    public String placeTileWithTiger(String gameId, String tileCode, int x, int y, int orientation, int zone){
        return "GAME " + gameId + " PLACE " + tileCode + " AT " + x + " " + y + " TIGER " + zone;
    }

    public String placeTileWithCrocodile(String gameId, String tileCode, int x, int y, int orientation){
        return "GAME " + gameId + " PLACE " + tileCode + " AT " + x + " " + y + " " + "CROCODILE";
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
