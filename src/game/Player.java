package game;

import entities.board.Tiger;
import game.messaging.info.PlayerInfo;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private final String name;
    private int score;
    private int remainingTigers;
    private int remainingCrocodiles;
    private Set<Tiger> placedTigers;

    public Player(String name){
        this.name = name;
        remainingTigers = 7;
        remainingCrocodiles = 2;
        score = 0;
        placedTigers = new HashSet<>();
    }

    public void addPlacedTiger(Tiger tiger) {
        placedTigers.add(tiger);
    }

    public void removePlacedTiger(Tiger tiger) {
        placedTigers.remove(tiger);
    }

    public PlayerInfo getPlayerInfo() {
        Set<Tiger> placedTigersCopy = new HashSet<>();
        placedTigersCopy.addAll(placedTigers);
        return new PlayerInfo(name, score, remainingTigers, remainingCrocodiles, placedTigersCopy);
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

    public boolean hasRemainingCrocodiles() {
        return getRemainingCrocodiles() > 0;
    }

    public Set<Tiger> getPlacedTigers() {
        return placedTigers;
    }
}
