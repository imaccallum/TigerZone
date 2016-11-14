package entities.player;

public class Player {
    private String name;
    private int score;
    private int remainingTigers;

    public Player(String name){
        this.name = name;
        remainingTigers = 6;
        score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getRemainingTigers() {
        return remainingTigers;
    }

    public void addToScore(int scoreToAdd) {
        score += scoreToAdd;
    }
}
