package entities.board;

// A follower that a player can place in tile sections/regions to earn points.
public class Tiger {
    private String owningPlayerName;
    private boolean stacked;

    public Tiger(String owningPlayerName) {
        this.owningPlayerName = owningPlayerName;
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

    // Set whether or not the tiger is stacked
    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }
}
