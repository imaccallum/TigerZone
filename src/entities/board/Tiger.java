package entities.board;

// A follower that a player can place in tile sections/regions to earn points.
public class Tiger {
    private final String owningPlayerName;
    private final boolean stacked;

    public Tiger(String owningPlayerName, boolean stacked) {
        this.owningPlayerName = owningPlayerName;
        this.stacked = stacked;
    }

    // MARK: Getters and setters

    // Get the owning player
    public String getOwningPlayerName() {
        return owningPlayerName;
    }

    // Get whether or not the tiger is stacked
    public boolean isStacked() {
        return stacked;
    }
}
