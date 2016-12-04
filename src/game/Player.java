package game;

import entities.board.Tiger;
import game.messaging.info.PlayerInfo;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Player {
    private final String name;
    private int score;
    private int remainingTigers;
    private int remainingCrocodiles;
    private Map<Tiger, Point> placedTigers;

    public Player(String name) {
        this.name = name;
        remainingTigers = 7;
        remainingCrocodiles = 2;
        score = 0;
        placedTigers = new HashMap<>();
    }

    public void addPlacedTiger(Tiger tiger, Point location) {
        placedTigers.put(tiger, location);
    }

    public PlayerInfo getPlayerInfo() {
        return new PlayerInfo(name, score, remainingTigers, remainingCrocodiles);
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

    public Map<Tiger, Point> getPlacedTigers() {
        return placedTigers;
    }
}
