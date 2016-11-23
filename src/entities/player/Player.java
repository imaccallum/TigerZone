package entities.player;

public class Player {
    private String name;
    private int score;
    private int remainingTigers;
    private int remainingCrocodiles;

    public Player(String name){
        this.name = name;
        remainingTigers = 7;
        remainingCrocodiles = 2;
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

    public void incrementRemainingTigers(){
        remainingTigers++;
    }

    public void decrementRemainingTigers(){
        remainingTigers--;
    }

    public boolean hasRemainingTigers(){
        return getRemainingTigers() > 0;
    }

    public void addToScore(int scoreToAdd) {
        score += scoreToAdd;
    }

    public int getRemainingCrocodiles() {
        return remainingCrocodiles;
    }

    public void incrementRemainingCrocodiles(){
        remainingCrocodiles++;
    }

    public void decrementRemainingCrocodiles(){
        remainingCrocodiles--;
    }
}
