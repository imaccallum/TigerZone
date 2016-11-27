package wrappers;

/**
 * Created by ianmaccallum on 11/27/16.
 */
public class BeginTurnWrapper {

    private String gid;
    private int time;
    private int moveNumber;
    private String tile;

    public BeginTurnWrapper(String gid, int time, int moveNumber, String tile) {
        this.gid = gid;
        this.time = time;
        this.moveNumber = moveNumber;
        this.tile = tile;
    }

    public String getGid() {
        return gid;
    }

    public int getTime() {
        return time;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public String getTile() {
        return tile;
    }
}
