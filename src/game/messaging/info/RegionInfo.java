package game.messaging.info;

import java.util.UUID;

public class RegionInfo {
    /**
     * The id of the region
     */
    public final UUID regionId;

    /**
     * The number of open node connections remaining in the region
     */
    public final int numberOfOpenNodeConnections;

    /**
     * The projected score of the region if it is completed with the minimum number of tiles
     */
    public final int projectedScoreUponCompletion;

    /**
     * Construct a RegionInfo, all properties are final to protect data
     *
     * @param regionId,
     * The unique id of the region
     *
     * @param numberOfOpenNodeConnections,
     * The number of remaining open node connections
     *
     * @param projectedScoreUponCompletion,
     * The projected score of the region if it is completed with the minimum number of tile placements
     */
    public RegionInfo(UUID regionId, int numberOfOpenNodeConnections, int projectedScoreUponCompletion) {
        this.regionId = regionId;
        this.numberOfOpenNodeConnections = numberOfOpenNodeConnections;
        this.projectedScoreUponCompletion = projectedScoreUponCompletion;
    }
}
