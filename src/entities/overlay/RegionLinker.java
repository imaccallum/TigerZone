package entities.overlay;

import entities.board.Tiger;

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
        region1.addTiger(region2.getTigerList().toArray(new Tiger[region2.getTigerList().size()]));
        regions.remove(region2);
    }

    public ArrayList<Region> getRegions(){
        return regions;
    }
}