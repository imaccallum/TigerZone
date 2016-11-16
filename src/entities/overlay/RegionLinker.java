package entities.overlay;

import java.util.ArrayList;

public class RegionLinker {
    private ArrayList<Region> regions;

    public RegionLinker() {
        regions = new ArrayList<Region>();
    }

    public void addRegion(Region region){
        regions.add(region);
    }

    public void linkRegions(Region region1, Region region2){
        for (TileSection tileSection : region2.getTileSections()) {
            if (tileSection.getTiger() != null) {
                region1.addTiger(tileSection.getTiger());
            }
            region1.addTileSection(tileSection);
        }
        regions.remove(region2);
    }

    public ArrayList<Region> getRegions(){
        return regions;
    }
}