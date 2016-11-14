package entities.board;

import entities.overlay.Region;
import entities.player.Player;

// A follower that a player can place in tile sections/regions to earn points.
public class Tiger {
    private Player player;
    private Region region;

    public Tiger(Player player) {
        this.player = player;
    }

    public Player getOwningPlayer() {
        return player;
    }

    public Region getRegion(){
        return region;
    }
    public void setRegion(Region region){
        this.region = region;
    }

}
