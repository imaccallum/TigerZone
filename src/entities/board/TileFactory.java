package entities.board;

import entities.overlay.Region;
import entities.overlay.TigerDen;
import entities.overlay.TileSection;
import exceptions.IncompatibleTerrainException;

public class TileFactory {

    public Tile makeTile(String s) {
        switch (s) {
            case "JJJJ-": return makeTileA();
            case "JJJJX": return makeTileB();
            case "JJTJX": return makeTileC();
            case "TTTT-": return makeTileD();
            case "TJTJ-": return makeTileE();
            case "TJJT-": return makeTileF();
            case "TJTT-": return makeTileG();
            case "LLLL-": return makeTileH();
            case "JLLL-": return makeTileI();
            case "LLJJ-": return makeTileJ();
            case "JLJL-": return makeTileK();
            case "LJLJ-": return makeTileL();
            case "LJJJ-": return makeTileM();
            case "JLLJ-": return makeTileN();
            case "TLJT-": return makeTileO();
            case "TLJTP": return makeTileP();
            case "JLTT-": return makeTileQ();
            case "JLTTB": return makeTileR();
            case "TLTJ-": return makeTileS();
            case "TLTJD": return makeTileT();
            case "TLLL-": return makeTileU();
            case "TLTT-": return makeTileV();
            case "TLTTP": return makeTileW();
            case "TLLT-": return makeTileX();
            case "TLLTB": return makeTileY();
            case "LJTJ-": return makeTileZ();
            case "LJTJD": return makeTile0();
            case "TLLLC": return makeTile1();
            default: throw new RuntimeException("Invalid tile identifier");
        }
    }

    // |JJJJ---------------------JJJJ-|
    // |    X       JUNGLE      X     |
    // |  JUNGLE    False     JUNGLE  |
    // |    X       JUNGLE      X     |
    // |JJJJ---------------------JJJJ-|
    private Tile makeTileA() {
        // Initialize the Tile and Sections with respective terrain types
        Tile tile = new Tile("JJJJ-");
        TileSection jungle = new TileSection(Terrain.JUNGLE);

        // Create Nodes counterclockwise
        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        // Add nodes to the respective Sections
        jungle.addNodes(e_zero, e_one, e_two, e_three);

        // Fill the Node arrays in the Tile
        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

        // Add the sections to the tile
        tile.addTileSections(jungle);

        // Create all the regions and add the TileSections to them
        Region jungleRegion = new Region(Terrain.JUNGLE);
        try{
            jungleRegion.addTileSection(jungle);
        }
        catch(IncompatibleTerrainException e){}

        //this is where setting prey boolean would go if there is one
        return tile;
    }

    // |JJJJX--------------------JJJJX|
    // |    X       JUNGLE      X     |
    // |  JUNGLE     True     JUNGLE  |
    // |    X       JUNGLE      X     |
    // |JJJJX--------------------JJJJX|
    private Tile makeTileB() {
        Tile tile = new Tile("JJJJX");
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        tile.setDen(new TigerDen());

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();


        jungle.addNodes(e_zero, e_one, e_two, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

        tile.addTileSections(jungle);

        Region jungleRegion = new Region(Terrain.JUNGLE);
        try{
            jungleRegion.addTileSection(jungle);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |JJTJX--------------------JJTJX|
    // |    X       JUNGLE      X     |
    // |  JUNGLE     True     JUNGLE  |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |JJTJX--------------------JJTJX|
    private Tile makeTileC() {
        Tile tile = new Tile("JJTJX");
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        tile.setDen(new TigerDen());
        TileSection trail = new TileSection(Terrain.TRAIL);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node c_two = new Node();
        Node e_two = new Node();
        Node c_three = new Node();
        Node e_three = new Node();


        jungle.addNodes(e_zero, e_one, c_two, c_three, e_three);
        trail.addNodes(e_two);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle, trail);

        Region jungleRegion = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        try{
            jungleRegion.addTileSection(jungle);
            trailRegion.addTileSection(trail);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |TTTT---------------------TTTT-|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False     TRAIL   |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |TTTT---------------------TTTT-|
    private Tile makeTileD() {
        Tile tile = new Tile("TTTT-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection jungle2 = new TileSection(Terrain.JUNGLE);
        TileSection jungle3 = new TileSection(Terrain.JUNGLE);
        TileSection trail0 = new TileSection(Terrain.TRAIL);
        TileSection trail1 = new TileSection(Terrain.TRAIL);
        TileSection trail2 = new TileSection(Terrain.TRAIL);
        TileSection trail3 = new TileSection(Terrain.TRAIL);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one);
        jungle2.addNodes(c_two);
        jungle3.addNodes(c_three);
        trail0.addNodes(e_zero);
        trail1.addNodes(e_one);
        trail2.addNodes(e_two);
        trail3.addNodes(e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, jungle2, jungle3, trail0, trail1, trail2, trail3);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion0 = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region trailRegion1 = new Region(Terrain.TRAIL);
        Region jungleRegion2 = new Region(Terrain.JUNGLE);
        Region trailRegion2 = new Region(Terrain.TRAIL);
        Region jungleRegion3 = new Region(Terrain.JUNGLE);
        Region trailRegion3 = new Region(Terrain.TRAIL);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion0.addTileSection(trail0);
            jungleRegion1.addTileSection(jungle1);
            trailRegion1.addTileSection(trail1);
            jungleRegion2.addTileSection(jungle2);
            trailRegion2.addTileSection(trail2);
            jungleRegion3.addTileSection(jungle3);
            trailRegion3.addTileSection(trail3);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |TJTJ---------------------TJTJ-|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  JUNGLE    False     JUNGLE  |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |TJTJ---------------------TJTJ-|
    private Tile makeTileE() {
        Tile tile = new Tile("TJTJ-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection trail = new TileSection(Terrain.TRAIL);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero, e_three, c_three);
        jungle1.addNodes(c_one, e_one, c_two);
        trail.addNodes(e_zero, e_two);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, trail);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |TJJT---------------------TJJT-|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False     JUNGLE  |
    // |  JUNGLE    JUNGLE    JUNGLE  |
    // |TJJT---------------------TJJT-|
    private Tile makeTileF() {
        Tile tile = new Tile( "TJJT-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection trail = new TileSection(Terrain.TRAIL);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_three, e_two, c_one, e_one, c_two);
        jungle1.addNodes(c_zero);
        trail.addNodes(e_zero, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, trail);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
        }
        catch(IncompatibleTerrainException e){}

        System.out.println(tile.toString());
        return tile;
    }

    // |TJTT---------------------TJTT-|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False     JUNGLE  |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |TJTT---------------------TJTT-|
    private Tile makeTileG() {
        Tile tile = new Tile( "TJTT-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection jungle2 = new TileSection(Terrain.JUNGLE);
        TileSection trail0 = new TileSection(Terrain.TRAIL);
        TileSection trail1 = new TileSection(Terrain.TRAIL);
        TileSection trail2 = new TileSection(Terrain.TRAIL);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one, e_one, c_two);
        jungle2.addNodes(c_three);
        trail0.addNodes(e_zero);
        trail1.addNodes(e_two);
        trail2.addNodes(e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, jungle2, trail0, trail1, trail2);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion0 = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region trailRegion1 = new Region(Terrain.TRAIL);
        Region jungleRegion2 = new Region(Terrain.JUNGLE);
        Region trailRegion2 = new Region(Terrain.TRAIL);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion0.addTileSection(trail0);
            jungleRegion1.addTileSection(jungle1);
            trailRegion1.addTileSection(trail1);
            jungleRegion2.addTileSection(jungle2);
            trailRegion2.addTileSection(trail2);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |LLLL---------------------LLLL-|
    // |    X        LAKE       X     |
    // |   LAKE     False      LAKE   |
    // |    X        LAKE       X     |
    // |LLLL---------------------LLLL-|
    private Tile makeTileH() {
        Tile tile = new Tile("LLLL-");
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        lake.addNodes(e_zero, e_one, e_two, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

        tile.addTileSections(lake);

        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |JLLL---------------------JLLL-|
    // |  JUNGLE    JUNGLE    JUNGLE  |
    // |   LAKE     False      LAKE   |
    // |    X        LAKE       X     |
    // |JLLL---------------------JLLL-|
    private Tile makeTileI() {
        Tile tile = new Tile( "JLLL-");
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection jungle = new TileSection(Terrain.JUNGLE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();

        lake.addNodes(e_one, e_two, e_three);
        jungle.addNodes(c_zero, e_zero, c_one);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);

        tile.addTileSections(lake, jungle);

        Region jungleRegion = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion.addTileSection(jungle);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |LLJJ---------------------LLJJ-|
    // |  JUNGLE     LAKE       X     |
    // |  JUNGLE    False      LAKE   |
    // |  JUNGLE    JUNGLE    JUNGLE  |
    // |LLJJ---------------------LLJJ-|
    private Tile makeTileJ() {
        Tile tile = new Tile( "LLJJ-");
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection jungle = new TileSection(Terrain.JUNGLE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        lake.addNodes(e_zero, e_one);
        jungle.addNodes(c_zero, c_two, e_two, c_three, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(lake, jungle);

        Region jungleRegion = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion.addTileSection(jungle);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;

    }

    // |JLJL---------------------JLJL-|
    // |    X       JUNGLE      X     |
    // |   LAKE     False      LAKE   |
    // |    X       JUNGLE      X     |
    // |JLJL---------------------JLJL-|
    private Tile makeTileK() {
        Tile tile = new Tile("JLJL-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        jungle0.addNodes(e_zero);
        jungle1.addNodes(e_two);
        lake.addNodes(e_one, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

        tile.addTileSections(jungle0, jungle1, lake);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |LJLJ---------------------LJLJ-|
    // |    X        LAKE       X     |
    // |  JUNGLE    False     JUNGLE  |
    // |    X        LAKE       X     |
    // |LJLJ---------------------LJLJ-|
    Tile makeTileL() {
        Tile tile = new Tile( "LJLJ-");
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection lake2 = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        jungle.addNodes(e_one, e_three);
        lake.addNodes(e_zero);
        lake2.addNodes(e_two);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

        tile.addTileSections(jungle, lake, lake2);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region lakeRegion0 = new Region(Terrain.LAKE);
        Region lakeRegion1 = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle);
            lakeRegion0.addTileSection(lake);
            lakeRegion1.addTileSection(lake2);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |LJJJ---------------------LJJJ-|
    // |  JUNGLE     LAKE     JUNGLE  |
    // |  JUNGLE    False     JUNGLE  |
    // |    X       JUNGLE      X     |
    // |LJJJ---------------------LJJJ-|
    private Tile makeTileM() {
        Tile tile = new Tile("LJJJ-");
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();

        jungle.addNodes(c_zero, e_three, e_two, e_one, c_one);
        lake.addNodes(e_zero);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);

        tile.addTileSections(jungle, lake);

        Region jungleRegion = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion.addTileSection(jungle);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |JLLJ---------------------JLLJ-|
    // |    X       JUNGLE      X     |
    // |  JUNGLE    False      LAKE   |
    // |    X        LAKE       X     |
    // |JLLJ---------------------JLLJ-|
    private Tile makeTileN() {
        Tile tile = new Tile("JLLJ-");
        TileSection jungle = new TileSection(Terrain.JUNGLE);
        TileSection lake0 = new TileSection(Terrain.LAKE);
        TileSection lake1 = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        jungle.addNodes(e_zero, e_three);
        lake0.addNodes(e_one);
        lake1.addNodes(e_two);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

        tile.addTileSections(jungle, lake0, lake1);

        Region jungleRegion = new Region(Terrain.JUNGLE);
        Region lakeRegion0 = new Region(Terrain.LAKE);
        Region lakeRegion1 = new Region(Terrain.LAKE);
        try{
            jungleRegion.addTileSection(jungle);
            lakeRegion0.addTileSection(lake0);
            lakeRegion1.addTileSection(lake1);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |TLJT---------------------TLJT-|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False      LAKE   |
    // |  JUNGLE    JUNGLE    JUNGLE  |
    // |TLJT---------------------TLJT-|
    private Tile makeTileO() {
        Tile tile = new Tile("TLJT-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection trail = new TileSection(Terrain.TRAIL);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one, c_two, e_two, c_three);
        lake.addNodes(e_one);
        trail.addNodes(e_zero, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, lake, trail);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |TLJTP--------------------TLJTP|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False      LAKE   |
    // |  JUNGLE    JUNGLE    JUNGLE  |
    // |TLJTP--------------------TLJTP|
    private Tile makeTileP() {
        Tile tile = new Tile("TLJTP");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection trail = new TileSection(Terrain.TRAIL);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one, c_two, e_two, c_three);
        lake.addNodes(e_one);
        trail.addNodes(e_zero, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, lake, trail);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        tile.setPreyAnimal(PreyAnimal.BOAR);
        return tile;
    }

    // |JLTT---------------------JLTT-|
    // |  JUNGLE    JUNGLE      X     |
    // |  TRAIL     False      LAKE   |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |JLTT---------------------JLTT-|
    private Tile makeTileQ() {
        Tile tile = new Tile("JLTT-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection trail = new TileSection(Terrain.TRAIL);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero, e_zero, c_two);
        jungle1.addNodes(c_three);
        lake.addNodes(e_one);
        trail.addNodes(e_two, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, lake, trail);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |JLTTB--------------------JLTTB|
    // |  JUNGLE    JUNGLE      X     |
    // |  TRAIL     False      LAKE   |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |JLTTB--------------------JLTTB|
    private Tile makeTileR() {
        Tile tile = new Tile( "JLTTB");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection trail = new TileSection(Terrain.TRAIL);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero, e_zero, c_two);
        jungle1.addNodes(c_three);
        lake.addNodes(e_one);
        trail.addNodes(e_two, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, lake, trail);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        tile.setPreyAnimal(PreyAnimal.BUFFALO);
        return tile;
    }

    // |TLTJ---------------------TLTJ-|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  JUNGLE    False      LAKE   |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |TLTJ---------------------TLTJ-|
    private Tile makeTileS() {
        Tile tile = new Tile( "TLTJ-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero, e_three, c_three);
        trail.addNodes(e_zero, e_two);
        jungle1.addNodes(c_one, c_two);
        lake.addNodes(e_one);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, trail, jungle1, lake);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |TLTJD--------------------TLTJD|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  JUNGLE    False      LAKE   |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |TLTJD--------------------TLTJD|
    private Tile makeTileT() {
        Tile tile = new Tile( "TLTJD");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero, e_three, c_three);
        trail.addNodes(e_zero, e_two);
        jungle1.addNodes(c_one, c_two);
        lake.addNodes(e_one);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, trail, jungle1, lake);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        tile.setPreyAnimal(PreyAnimal.DEER);
        return tile;
    }

    // |TLLL---------------------TLLL-|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |   LAKE     False      LAKE   |
    // |    X        LAKE       X     |
    // |TLLL---------------------TLLL-|
    private Tile makeTileU() {
        Tile tile = new Tile( "TLLL-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();

        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one);
        trail.addNodes(e_zero);
        lake.addNodes(e_one, e_two, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);

        tile.addTileSections(jungle0, jungle1, trail, lake);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |TLTT---------------------TLTT-|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False      LAKE   |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |TLTT---------------------TLTT-|
    private Tile makeTileV() {
        Tile tile = new Tile( "TLTT-");
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection trail0 = new TileSection(Terrain.TRAIL);
        TileSection trail1 = new TileSection(Terrain.TRAIL);
        TileSection trail2 = new TileSection(Terrain.TRAIL);
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection jungle2 = new TileSection(Terrain.JUNGLE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        lake.addNodes(e_one);
        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one, c_two);
        jungle2.addNodes(c_three);
        trail0.addNodes(e_zero);
        trail1.addNodes(e_two);
        trail2.addNodes(e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(lake, trail0, trail1, trail2, jungle0, jungle1, jungle2);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion0 = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region trailRegion1 = new Region(Terrain.TRAIL);
        Region jungleRegion2 = new Region(Terrain.JUNGLE);
        Region trailRegion2 = new Region(Terrain.TRAIL);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion0.addTileSection(trail0);
            jungleRegion1.addTileSection(jungle1);
            trailRegion1.addTileSection(trail1);
            jungleRegion2.addTileSection(jungle2);
            trailRegion2.addTileSection(trail2);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |TLTTP--------------------TLTTP|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False      LAKE   |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |TLTTP--------------------TLTTP|
    private Tile makeTileW() {
        Tile tile = new Tile( "TLTTP");
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection trail0 = new TileSection(Terrain.TRAIL);
        TileSection trail1 = new TileSection(Terrain.TRAIL);
        TileSection trail2 = new TileSection(Terrain.TRAIL);
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection jungle2 = new TileSection(Terrain.JUNGLE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        lake.addNodes(e_one);
        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one, c_two);
        jungle2.addNodes(c_three);
        trail0.addNodes(e_zero);
        trail1.addNodes(e_two);
        trail2.addNodes(e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(lake, trail0, trail1, trail2, jungle0, jungle1, jungle2);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion0 = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region trailRegion1 = new Region(Terrain.TRAIL);
        Region jungleRegion2 = new Region(Terrain.JUNGLE);
        Region trailRegion2 = new Region(Terrain.TRAIL);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion0.addTileSection(trail0);
            jungleRegion1.addTileSection(jungle1);
            trailRegion1.addTileSection(trail1);
            jungleRegion2.addTileSection(jungle2);
            trailRegion2.addTileSection(trail2);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        tile.setPreyAnimal(PreyAnimal.BOAR);
        return tile;
    }

    // |TLLT---------------------TLLT-|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False      LAKE   |
    // |  JUNGLE     LAKE       X     |
    // |TLLT---------------------TLLT-|
    Tile makeTileX() {
        Tile tile = new Tile( "TLLT-");
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_three = new Node();

        lake.addNodes(e_one, e_two);
        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one, c_three);
        trail.addNodes(e_zero, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(lake, trail, jungle0, jungle1);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |TLLTB--------------------TLLTB|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |  TRAIL     False      LAKE   |
    // |  JUNGLE     LAKE       X     |
    // |TLLTB--------------------TLLTB|
    private Tile makeTileY() {
        Tile tile = new Tile( "TLLTB");
        TileSection lake = new TileSection(Terrain.LAKE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_three = new Node();

        lake.addNodes(e_one, e_two);
        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one, c_three);
        trail.addNodes(e_zero, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(lake, trail, jungle0, jungle1);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        tile.setPreyAnimal(PreyAnimal.BUFFALO);
        return tile;
    }

    // |LJTJ---------------------LJTJ-|
    // |  JUNGLE     LAKE     JUNGLE  |
    // |  JUNGLE    False     JUNGLE  |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |LJTJ---------------------LJTJ-|
    private Tile makeTileZ() {
        Tile tile = new Tile( "LJTJ-");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero, e_three, c_three);
        jungle1.addNodes(c_one, e_one, c_two);
        trail.addNodes(e_two);
        lake.addNodes(e_zero);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

       tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
       tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
       tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
       tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, trail, lake);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        return tile;
    }

    // |LJTJD--------------------LJTJD|
    // |  JUNGLE     LAKE     JUNGLE  |
    // |  JUNGLE    False     JUNGLE  |
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |LJTJD--------------------LJTJD|
    private Tile makeTile0() {
        Tile tile = new Tile("LJTJD");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();
        Node c_two = new Node();
        Node c_three = new Node();

        jungle0.addNodes(c_zero, e_three, c_three);
        jungle1.addNodes(c_one, e_one, c_two);
        trail.addNodes(e_two);
        lake.addNodes(e_zero);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

        tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
        tile.setCorner(c_one, CornerLocation.TOP_RIGHT);
        tile.setCorner(c_two, CornerLocation.BOTTOM_RIGHT);
        tile.setCorner(c_three, CornerLocation.BOTTOM_LEFT);

        tile.addTileSections(jungle0, jungle1, trail, lake);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        tile.setPreyAnimal(PreyAnimal.DEER);
        return tile;
    }

    // |TLLLC--------------------TLLLC|
    // |  JUNGLE    TRAIL     JUNGLE  |
    // |   LAKE     False      LAKE   |
    // |    X        LAKE       X     |
    // |TLLLC--------------------TLLLC|
    private Tile makeTile1() {
        Tile tile = new Tile("TLLLC");
        TileSection jungle0 = new TileSection(Terrain.JUNGLE);
        TileSection trail = new TileSection(Terrain.TRAIL);
        TileSection jungle1 = new TileSection(Terrain.JUNGLE);
        TileSection lake = new TileSection(Terrain.LAKE);

        Node e_zero = new Node();
        Node e_one = new Node();
        Node e_two = new Node();
        Node e_three = new Node();

        Node c_zero = new Node();
        Node c_one = new Node();

        jungle0.addNodes(c_zero);
        jungle1.addNodes(c_one);
        trail.addNodes(e_zero);
        lake.addNodes(e_one, e_two, e_three);

        tile.setEdge(e_zero, EdgeLocation.TOP);
        tile.setEdge(e_one, EdgeLocation.RIGHT);
        tile.setEdge(e_two, EdgeLocation.BOTTOM);
        tile.setEdge(e_three, EdgeLocation.LEFT);

        tile.setCorner(c_zero, CornerLocation.TOP_LEFT);
        tile.setCorner(c_one, CornerLocation.TOP_RIGHT);

        tile.addTileSections(jungle0, jungle1, trail, lake);

        Region jungleRegion0 = new Region(Terrain.JUNGLE);
        Region trailRegion = new Region(Terrain.TRAIL);
        Region jungleRegion1 = new Region(Terrain.JUNGLE);
        Region lakeRegion = new Region(Terrain.LAKE);
        try{
            jungleRegion0.addTileSection(jungle0);
            trailRegion.addTileSection(trail);
            jungleRegion1.addTileSection(jungle1);
            lakeRegion.addTileSection(lake);
        }
        catch(IncompatibleTerrainException e){}

        tile.setHasCrocodile(true);
        return tile;
    }
}

