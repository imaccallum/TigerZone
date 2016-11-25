package game.messaging.request;

import entities.board.Tile;

import java.awt.*;

public class TilePlacementRequest {
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
     * @param tileToPlace,
     * The tile to be placed on the board, must not already be placed on the board.  Should be in final orientation.
     *
     * @param locationToPlaceAt,
     * Location to place the tile at, should be a valid location to place this tile on the board.
     */
    public TilePlacementRequest(Tile tileToPlace, Point locationToPlaceAt) {
        this.tileToPlace = tileToPlace;
        this.locationToPlaceAt = locationToPlaceAt;
    }
}
