package game.messaging.info;

public class PlayerInfo {
    /**
     * The player's name associated with this player info
     */
    public final String playerName;

    /**
     * The score associated with the with the player
     */
    public final int score;

    /**
     * The remaining number of tigers the player has
     */
    public final int remainingTigers;

    /**
     * The remaining number of crocodiles the player has
     */
    public final int remainingCrocodiles;

    /**
     * Constructs the player info, all properties are final to protect data
     *
     * @param playerName,
     * The name of the player associated with this info
     *
     * @param score,
     * The score of the player
     *
     * @param remainingTigers,
     * The number of remaining tigers the player has
     *
     * @param remainingCrocodiles,
     * The number of remaining crocodiles the player has
     */
    public PlayerInfo(String playerName, int score, int remainingTigers, int remainingCrocodiles) {
        this.playerName = playerName;
        this.score = score;
        this.remainingTigers = remainingTigers;
        this.remainingCrocodiles = remainingCrocodiles;
    }
}
