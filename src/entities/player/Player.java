package entities.player;

import entities.board.Follower;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int playerNum;
    private int score;
    private int remainingFollowers;

    public Player(String name, int playerNum){
        this.name = name;
        this.playerNum = playerNum;
        remainingFollowers = 6;
        score = 0;
    }


    public String getName() {
        return name;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public int getScore() {
        return score;
    }

    public int getRemainingFollowers() {
        return remainingFollowers;
    }
}
