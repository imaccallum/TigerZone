package entities.board;

import entities.player.Player;

// A follower that a owningPlayer can place in tile sections/regions to earn points.
public class Tiger {
    private final Player owningPlayer;

    public Tiger(Player owningPlayer) {
        this.owningPlayer = owningPlayer;
    }

    public Player getOwningPlayer() {
        return owningPlayer;
    }
}
