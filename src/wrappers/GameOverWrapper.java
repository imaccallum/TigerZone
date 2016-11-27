package wrappers;

/**
 * Created by ianmaccallum on 11/27/16.
 */
public class GameOverWrapper {

    private String gid;
    private String pid0, pid1;
    private int score0, score1;

    public GameOverWrapper(String gid, String pid0, int score0, String pid1, int score1) {
        this.gid = gid;
        this.pid0 = pid0;
        this.score0 = score0;
        this.pid1 = pid1;
        this.score1 = score1;
    }

    public String getGid() {
        return gid;
    }

    public String getPid0() {
        return pid0;
    }

    public String getPid1() {
        return pid1;
    }

    public int getScore0() {
        return score0;
    }

    public int getScore1() {
        return score1;
    }
}
