package game.messaging.request;

import entities.board.Tile;

import java.awt.*;

public class TilePlacementRequest {
    /**
     * The name of the player making the request
     */
    public final String playerName;

    /**
     * The tile to be placed on the board, must be oriented in the correct manner
     */
    public final Tile tileToPlace;

    /**
     * The location to be placed at on the board
     */
    public final Point locationToPlaceAt;

    /**
     * Construct a tile placement request, all properties final to protect data
     *
     * @param playerName,
     * The name of the player making the request
     *
     * @param tileToPlace,
     * The tile to be placed on the board, must not already be placed on the board.  Should be in final orientation.
     *
     * @param locationToPlaceAt,
     * Location to place the tile at, should be a valid location to place this tile on the board.
     */
    public TilePlacementRequest(String playerName, Tile tileToPlace, Point locationToPlaceAt) {
        this.playerName = playerName;
        this.tileToPlace = tileToPlace;
        this.locationToPlaceAt = locationToPlaceAt;
    }
}
