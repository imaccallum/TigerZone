package entities.board;

import entities.player.Player;

// A follower that a player can place in tile sections/regions to earn points.
public class Follower {
    private Player owningPlayer;

    public Follower(Player player) {
        this.owningPlayer = player;
    }

    public Player getOwningPlayer() {
        return owningPlayer;
    }
}
