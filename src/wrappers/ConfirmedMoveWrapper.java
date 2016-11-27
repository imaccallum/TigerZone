package wrappers;

/**
 * Created by ianmaccallum on 11/27/16.
 */
public class ConfirmedMoveWrapper {

    private String gid;
    private int moveNumber;
    private String pid;
    private MoveWrapper move;
    private String error;

    public ConfirmedMoveWrapper(String gid, int moveNumber, String pid, MoveWrapper move, String error) {
        this.gid = gid;
        this.moveNumber = moveNumber;
        this.pid = pid;
        this.move = move;
        this.error = error;
    }

    public String getGid() {
        return gid;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public String getPid() {
        return pid;
    }

    public MoveWrapper getMove() {
        return move;
    }

    public String getError() {
        return error;
    }
}
