package wrappers.networkState;

import exceptions.ParseFailureException;

public abstract class NetworkState {
    NetworkContext context;

    NetworkState(NetworkContext context) {
        this.context = context;
    }

    public abstract String processInput(String input) throws ParseFailureException;
    public abstract NetworkState returnState();
}

/*

abstract class NetworkProtocol {

    NetworkContext context;

    NetworkProtocol(NetworkContext context) {
        this.context = context;
    }

    public String processInput(String input) throws BadServerInputException {
        throw new BadServerInputException("Must Override");
    }
}

class TournamentProtocol extends NetworkProtocol {
    TournamentProtocol(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws BadServerInputException {

//        if (input.startsWith("THIS IS SPARTA!")) {
//
//            // Switch to AuthenticationProtocol
//            AuthenticationProtocol protocol = new AuthenticationProtocol(context);
//            context.setProtocol(protocol);
//            return protocol.processInput(input);
//
//        } else if (input.equals("THANK YOU FOR PLAYING! GOODBYE")) {
//            return null;
//        }
        throw new BadServerInputException(input);
    }

}

class AuthenticationProtocol extends NetworkProtocol {

    public AuthenticationProtocol(NetworkContext context) {
        super(context);
    }

    public String processInput(String input) throws BadServerInputException {

//        ProtocolMessageBuilder messageBuilder = new ProtocolMessageBuilder();
//        ProtocolMessageParser messageParser = new ProtocolMessageParser();
//        String pid;
//
//        if (input.equals("THIS IS SPARTA!")) {
//            return messageBuilder.joinBuilder(context.getTournamentPassword());
//        } else if (input.equals("HELLO!")) {
//            return messageBuilder.identityBuilder(context.getUsername(), context.getPassword());
//        } else if ((pid = messageParser.parseWelcomePID(input)) != null) {
//            context.setProtocol(new ChallengeProtocol(context, pid));
//            return null;
//        } else {
//            throw new BadServerInputException(input);
//        }

        throw new BadServerInputException(input);
    }
}

class ChallengeProtocol extends NetworkProtocol {
    String pid;

    public ChallengeProtocol(NetworkContext context, String pid) {
        super(context);
        this.pid = pid;
    }

    public String processInput(String input) throws BadServerInputException {
        throw new BadServerInputException(input);
    }
}

class RoundProtocol extends NetworkProtocol {
    int currentRound;
    int totalRounds;

    public RoundProtocol(NetworkContext context) {
        super(context);
    }
}

class MatchProtocol extends NetworkProtocol {


    public MatchProtocol(NetworkContext context) {
        super(context);
    }
}

class MoveProtocol extends NetworkProtocol {
    public MoveProtocol(NetworkContext context) {
        super(context);
    }
}

class NetworkContext {
    NetworkProtocol protocol;
    private String tournamentPassword;
    private String username;
    private String password;
    private String pid;
    private String cid;
    private String rid;
    private int round;
    private int roundCount;

    public NetworkProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(NetworkProtocol protocol) {
        this.protocol = protocol;
    }

    public String getTournamentPassword() {
        return tournamentPassword;
    }

    public void setTournamentPassword(String tournamentPassword) {
        this.tournamentPassword = tournamentPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }
}

class Challenge {

}

class Match {
    String pid, opid;
    Map<String, Game> games;


}

class Game {

}
*/