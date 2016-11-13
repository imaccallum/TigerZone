package entities.board;

import entities.player.Player;

// A follower that a player can place in tile sections/regions to earn points.
public class Tiger {
    private Player player;

    public Tiger(Player player) {
        this.player = player;
    }

    public Player getOwningPlayer() {
        return player;
    }
}
