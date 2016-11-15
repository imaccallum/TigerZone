package entities.player;

public class Player {
    private String name;
    private int score;
    private int remainingFollowers;

    public Player(String name){
        this.name = name;
        remainingFollowers = 7;
        score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getRemainingFollowers() {
        return remainingFollowers;
    }

    public void incrementRemainingFollowers(){
        remainingFollowers++;
    }

    public void addToScore(int scoreToAdd) {
        score += scoreToAdd;
    }
}
