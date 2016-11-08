package entities.board;

// A follower that a player can place in tile sections/regions to earn points.
public class Follower {
    private int owningPlayerNumber;

    public Follower(int owningPlayerNumber) {
        this.owningPlayerNumber = owningPlayerNumber;
    }

    public int getOwningPlayerNumber() {
        return owningPlayerNumber;
    }
}
