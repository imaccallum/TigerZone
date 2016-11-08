package entities.overlay;

import entities.board.TerrainType;
import entities.board.TileSection;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

// A region represents a specific area on the board where there is an agglomeration of a specific terrain type
// As tiles are placed on the board, new regions are generated and tilesections are added to specific regions, while
// the tilesections are given a region object (composition)
public class Region {
    private UUID tileId;
    private TerrainType terrain;
    private List<TileSection> sections;

    public Region(TerrainType terrain){
        tileId = UUID.randomUUID();
        this.terrain = terrain;
        sections = new ArrayList<>();
    }

    public void addSection(TileSection section){
        sections.add(section);
    }
}
