package game.messaging.info;

import java.util.List;
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
    public final int scoreIfRegionCompletedNow;

    /**
     * The dominant players in the region
     */
    public final List<String> dominantPlayerNames;

    /**
     * Construct a RegionInfo, all properties are final to protect data
     *
     * @param regionId,
     * The unique id of the region
     *
     * @param numberOfOpenNodeConnections,
     * The number of remaining open node connections
     *
     * @param scoreIfRegionCompletedNow,
     * The score of the region if it were completed now
     *
     * @param dominantPlayerNames,
     * The dominant players in the region
     */
    public RegionInfo(UUID regionId, int numberOfOpenNodeConnections, int scoreIfRegionCompletedNow,
                      List<String> dominantPlayerNames) {
        this.regionId = regionId;
        this.numberOfOpenNodeConnections = numberOfOpenNodeConnections;
        this.scoreIfRegionCompletedNow = scoreIfRegionCompletedNow;
        this.dominantPlayerNames = dominantPlayerNames;
    }
}
