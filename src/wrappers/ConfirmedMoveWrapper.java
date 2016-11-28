package wrappers;

/**
 * Created by ianmaccallum on 11/27/16.
 */
public class ConfirmedMoveWrapper {

    private String gid;
    private int moveNumber;
    private String pid;

    private boolean isPlacementMove;
    private PlacementMoveWrapper placementMove;
    private NonplacementMoveWrapper nonplacementMove;

    private boolean hasForfeited = false;
    private String forfeitMessage;

    public ConfirmedMoveWrapper(String gid, int moveNumber, String pid) {
        this.gid = gid;
        this.moveNumber = moveNumber;
        this.pid = pid;
    }

    public boolean isPlacementMove() {
        return isPlacementMove;
    }

    public void setIsPlacementMove(boolean placementMove) {
        isPlacementMove = placementMove;
    }

    public PlacementMoveWrapper getPlacementMove() {
        return placementMove;
    }

    public void setPlacementMove(PlacementMoveWrapper placementMove) {
        this.placementMove = placementMove;
    }

    public NonplacementMoveWrapper getNonplacementMove() {
        return nonplacementMove;
    }

    public void setNonplacementMove(NonplacementMoveWrapper nonplacementMove) {
        this.nonplacementMove = nonplacementMove;
    }

    public boolean hasForfeited() {
        return hasForfeited;
    }

    public void setHasForfeited(boolean hasForfeited) {
        this.hasForfeited = hasForfeited;
    }

    public String getForfeitMessage() {
        return forfeitMessage;
    }

    public void setForfeitMessage(String forfeitMessage) {
        this.forfeitMessage = forfeitMessage;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
