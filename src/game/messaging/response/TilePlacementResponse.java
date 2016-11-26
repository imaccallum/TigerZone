package game.messaging.response;

import entities.board.Tiger;
import game.messaging.info.RegionTigerPlacement;
import game.messaging.info.TigerDenTigerPlacement;

import java.util.List;

public class TilePlacementResponse {
    /**
     * Whether or not the tile placement was valid
     */
    public final boolean wasValid;

    /**
     * The list of tiger placements for the regions
     */
    public final List<RegionTigerPlacement> regionTigerPlacementOptions;

    /**
     * The possible placement in a TigerDen if there is one
     */
    public final TigerDenTigerPlacement tigerDenPlacementOption;

    /**
     * The possible tigers that can be stacked
     */
    public final List<Tiger> tigersThatCanBeStacked;

    /**
     * Whether or not a crocodile can be placed in the current tile
     */
    public final boolean canPlaceCrocodile;

    /**
     * Construct a TilePlacement response, All properties are constant to protect board data
     *
     * @param wasValid,
     * Whether or not the tile placement was valid
     *
     * @param regionTigerPlacementOptions,
     * The tiger placement options pertaining to board regions such as lakes, jungles, and trails
     *
     * @param tigerDenPlacementOption,
     * The tiger placement option of a den if the tile has one
     *
     * @param canPlaceCrocodile,
     * Represents whether a crocodile can be placed.
     */
    public TilePlacementResponse(boolean wasValid,
                                 List<RegionTigerPlacement> regionTigerPlacementOptions,
                                 TigerDenTigerPlacement tigerDenPlacementOption,
                                 List<Tiger> tigersThatCanBeStacked,
                                 boolean canPlaceCrocodile) {
        this.wasValid = wasValid;
        this.regionTigerPlacementOptions = regionTigerPlacementOptions;
        this.tigerDenPlacementOption = tigerDenPlacementOption;
        this.tigersThatCanBeStacked = tigersThatCanBeStacked;
        this.canPlaceCrocodile = canPlaceCrocodile;
    }
}
