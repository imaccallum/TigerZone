package game.messaging.response;

public class FollowerPlacementResponse {
    /**
     * The tiger that was placed, can be null
     */
    public final boolean didPlaceTiger;

    /**
     * Whether or not a crocodile was placed
     */
    public final boolean didPlaceCrocodile;

    /**
     * Whether or not that tiger placement was valid
     */
    public final boolean wasValid;

    /**
     * Construct a tiger placement response
     *
     * @param didPlaceTiger,
     * Whether or not the tiger was placed
     *
     * @param didPlaceCrocodile,
     * Whether or not the crocodile was placed
     *
     * @param wasValid,
     * Whether or not the placement was valid
     */
    public FollowerPlacementResponse(boolean didPlaceTiger, boolean didPlaceCrocodile, boolean wasValid) {
        this.didPlaceTiger = didPlaceTiger;
        this.didPlaceCrocodile = didPlaceCrocodile;
        this.wasValid = wasValid;
    }
}
