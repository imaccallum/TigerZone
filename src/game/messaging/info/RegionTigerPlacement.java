package game.messaging.info;

public class RegionTigerPlacement {
    /**
     * The region info for the region the tiger is being placed on
     */
    public final RegionInfo regionInfo;

    /**
     * Whether or not the region was just completed by the last tile placement
     */
    public final boolean justCompleted;

    /**
     * Constructs a RegionTigerPlacement, all properties are final to protect data
     *
     * @param regionInfo,
     * The info about the region a tiger can be placed in
     *
     * @param justCompleted,
     * Whether or not the region was just completed by the last tile placement
     */
    public RegionTigerPlacement(RegionInfo regionInfo, boolean justCompleted) {
        this.regionInfo = regionInfo;
        this.justCompleted = justCompleted;
    }
}
