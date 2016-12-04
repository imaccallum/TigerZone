package game.messaging.response;

import game.messaging.info.RegionInfo;
import game.messaging.info.TigerDenTigerPlacement;

import java.util.Map;

public class TilePlacementResponse {
    /**
     * Whether or not the tile placement was valid
     */
    public final boolean wasValid;
    /**
     * The possible placement in a TigerDen if there is one
     */
    public final TigerDenTigerPlacement tigerDenPlacementOption;

    /**
     * Whether or not a crocodile can be placed in the current tile
     */
    public final boolean canPlaceCrocodile;

    /**
     * The mapping of region infos to the score differentials
     */
    public final Map<RegionInfo, Integer> regionsEffected;

    /**
     * Construct a TilePlacement response, All properties are constant to protect board data
     *
     * @param wasValid,
     * Whether or not the tile placement was valid
     *
     * @param tigerDenPlacementOption,
     * The tiger placement option of a den if the tile has one
     *
     * @param canPlaceCrocodile,
     * Represents whether a crocodile can be placed.
     *
     * @param regionsEffected,
     * The map of region info to score differentials representing the regions effected by this placement
     */
    public TilePlacementResponse(boolean wasValid,
                                 TigerDenTigerPlacement tigerDenPlacementOption,
                                 boolean canPlaceCrocodile,
                                 Map<RegionInfo, Integer> regionsEffected) {
        this.wasValid = wasValid;
        this.tigerDenPlacementOption = tigerDenPlacementOption;
        this.canPlaceCrocodile = canPlaceCrocodile;
        this.regionsEffected = regionsEffected;
    }
}
