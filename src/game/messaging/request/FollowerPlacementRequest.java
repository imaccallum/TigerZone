package game.messaging.request;

import entities.board.Tiger;
import game.messaging.info.RegionTigerPlacement;
import game.messaging.info.TigerDenTigerPlacement;

public class FollowerPlacementRequest {
    /**
     * The name of the player making the request
     */
    public final String playerName;

    /**
     * The Region tiger placement representing where a tiger can be placed
     * Will be null if any other property is not
     */
    public final RegionTigerPlacement regionTigerPlacement;

    /**
     * The TigerDen tiger placement representing where a tiger can be placed
     * Will be null if any other property is not
     */
    public final TigerDenTigerPlacement denTigerPlacement;

    /**
     * The tiger to be stacked
     * Will be null if any other property is not
     */
    public final Tiger tigerToStack;

    /**
     * Whether or not the placement is placing a crocodile
     */
    public final boolean placeCrocodile;

    /**
     * Construct a tiger placement request with niether placement, both placement properties will be null
     *
     * @param playerName,
     * The name of the player making the request
     */
    public FollowerPlacementRequest(String playerName) {
        this.playerName = playerName;
        this.regionTigerPlacement = null;
        this.denTigerPlacement = null;
        this.tigerToStack = null;
        this.placeCrocodile = false;
    }

    /**
     * Construct the FollowerPlacementRequest with a region tiger placement, sets other properties to null or false
     *
     * @param playerName,
     * The name of the player making the request
     *
     * @param regionTigerPlacement,
     * The RegionTigerPlacement representing where the tiger will be placed
     */
    public FollowerPlacementRequest(String playerName, RegionTigerPlacement regionTigerPlacement) {
        this.playerName = playerName;
        this.regionTigerPlacement = regionTigerPlacement;
        this.denTigerPlacement = null;
        this.tigerToStack = null;
        this.placeCrocodile = false;
    }

    /**
     * Construct a TigerPlacement request with a TigerDen placement, sets other properties to null or false
     *
     * @param playerName,
     * The name of the player making the request
     *
     * @param denTigerPlacement,
     * The TigerDenTigerPlacement representing where the tiger will be placed
     */
    public FollowerPlacementRequest(String playerName, TigerDenTigerPlacement denTigerPlacement) {
        this.playerName = playerName;
        this.denTigerPlacement = denTigerPlacement;
        this.regionTigerPlacement = null;
        this.tigerToStack = null;
        this.placeCrocodile = false;
    }

    /**
     * Construct a TigerPlacement request with a tiger to stack, sets other properties to null or false
     *
     * @param playerName,
     * The name of the player making the request
     *
     * @param tigerToStack,
     * The tiger that will be stacked
     */
    public FollowerPlacementRequest(String playerName, Tiger tigerToStack) {
        this.playerName = playerName;
        this.tigerToStack = tigerToStack;
        this.regionTigerPlacement = null;
        this.denTigerPlacement = null;
        this.placeCrocodile = false;
    }

    /**
     * Construct a TigerPlacement with a boolean representing whether or not a crocodile is to be placed, sets other
     * properties to null
     *
     * @param playerName,
     * The name of the player making the request
     *
     * @param placeCrocodile,
     * Whether a crocodile should be placed, any value other than true here is a misuse of this API
     */
    public FollowerPlacementRequest(String playerName, boolean placeCrocodile) {
        this.playerName = playerName;
        this.placeCrocodile = placeCrocodile;
        this.regionTigerPlacement = null;
        this.denTigerPlacement = null;
        this.tigerToStack = null;
    }

    /**
     * Whether or not the placement is a den tiger placement
     *
     * @return
     * The boolean condition of this state
     */
    public boolean isTigerDenPlacement() {
        return denTigerPlacement != null;
    }

    /**
     * Whether or not the placement is a region type placement
     *
     * @return
     * The boolean condition of this state
     */
    public boolean isRegionPlacement() {
        return regionTigerPlacement != null;
    }

    /**
     * Whether or not the placement is stacking a tiger
     *
     * @return
     * The boolean condition of this state
     */
    public boolean isStackingTiger() {
        return tigerToStack != null;
    }

    /**
     * Whether or not the placement is a crocodile placement
     *
     * @return
     * The boolean condition of this states
     */
    public boolean isCrocodilePlacement() {
        return placeCrocodile;
    }
}
