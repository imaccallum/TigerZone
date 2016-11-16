package entities.player;

public class PlayerStatus { //but seriously, what's the point of this class
    private Player player;
    private int score;
    private int remainingTigers;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRemainingTigers() {
        return remainingTigers;
    }

    public void setRemainingTigers(int remainingTigers) {
        this.remainingTigers = remainingTigers;
    }
}
