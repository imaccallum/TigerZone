package entities.board;

import entities.overlay.Region;

public class RegionMerge {
    public final Region firstOldRegion;
    public final Region secondOldRegion;
    public final Region newRegion;

    public RegionMerge(Region firstOldRegions, Region secondOldRegion, Region newRegion) {
        this.firstOldRegion = firstOldRegions;
        this.secondOldRegion = secondOldRegion;
        this.newRegion = newRegion;
    }
}
