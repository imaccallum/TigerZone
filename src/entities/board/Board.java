package entities.board;

import entities.overlay.Region;
import entities.overlay.TigerDen;
import entities.overlay.TileSection;
import exceptions.BadPlacementException;
import exceptions.IncompatibleTerrainException;
import game.LocationAndOrientation;

import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    private Tile[][] boardMatrix;
    private List<Point> openTileLocations;
    private Map<UUID, Region> regions;
    private List<TigerDen> tigerDens;
    private Tile lastTilePlaced;

    private int boardSize;
    private int numTiles;


    public Board(int stackSize, Tile firstTile) {
        boardMatrix = new Tile[stackSize * 2][stackSize * 2];
        boardSize = stackSize * 2;
        openTileLocations = new ArrayList<>();
        regions = new HashMap<>();
        tigerDens = new ArrayList<>();

//        System.out.println(firstTile.type);
        // Put the first tile down and set all of the open tile locations
        setTileAtPoint(firstTile, new Point(stackSize - 1, stackSize - 1));
        lastTilePlaced = firstTile;
        openTileLocations.add(new Point(stackSize - 1, stackSize));
        openTileLocations.add(new Point(stackSize - 2, stackSize - 1));
        openTileLocations.add(new Point(stackSize, stackSize - 1));
        openTileLocations.add(new Point(stackSize - 1, stackSize - 2));
        numTiles = 1;
    }

    // HAS TESTS
    public void insert(Tile tile, Point location) throws BadPlacementException {
        // For naming consistent with orientation of tile matrix, get x and y as row, col integers
        int row = location.y;
        int col = location.x;

        // Get the surrounding tiles of the placement.
        Tile leftTile = getTile(new Point(col-1, row));
        Tile rightTile = getTile(new Point(col+1, row));
        Tile bottomTile = getTile(new Point(col, row+1));
        Tile topTile = getTile(new Point(col, row-1));

        // If they are all null, we are trying to place a tile that will not be adjacent to any other tile and thus
        // throw a bad tile placement exception.
        if (leftTile == null && rightTile == null && topTile == null && bottomTile == null) {
            throw new BadPlacementException("Index given is out of bounds");
        }

        // Put the tile in the matrix, get ready to connect the tiles.
        setTileAtPoint(tile, location);
        numTiles++;
        openTileLocations.remove(location);

        // For each non-null tile, connect the tile's tileSections / regions / nodes so that the overlay is updated
        if (leftTile != null) {
            connectLaterally(tile, leftTile);
        } else {
            openTileLocations.add(new Point(col-1, row));
        }
        if (rightTile != null) {
            connectLaterally(rightTile, tile);
        } else {
            openTileLocations.add(new Point(col+1, row));
        }
        if (topTile != null) {
            connectVertically(tile, topTile);
        } else {
            openTileLocations.add(new Point(col, row-1));
        }
        if (bottomTile != null) {
            connectVertically(bottomTile, tile);
        } else {
            openTileLocations.add(new Point(col, row+1));
        }

        if (tile.hasDen()) {
            tigerDens.add(new TigerDen(location, this));
        }

        lastTilePlaced = tile;
    }

    // HAS TEST
    public List<LocationAndOrientation>findValidTilePlacements(Tile tile) {
        List<LocationAndOrientation> validPlacements = new ArrayList<>();
        for (Point openTileLocation : openTileLocations) {   // for each open tile
            int col = openTileLocation.x;
            int row = openTileLocation.y;
            Tile left = boardMatrix[row][col - 1];
            Tile right = boardMatrix[row][col + 1];
            Tile top = boardMatrix[row - 1][col];
            Tile bottom = boardMatrix[row + 1][col];

//            System.out.println("For openTile " + openTileLocation + ": ");
//            System.out.println("    Top = " + (top != null));
//            System.out.println("    Right = " + (right != null));
//            System.out.println("    Bottom = " + (bottom != null));
//            System.out.println("    Left = " + (left != null));

            for (int tileOrientation = 0; tileOrientation < 4; ++tileOrientation, tile.rotateClockwise(1)) {
                // By placing this at the end the tile is rotated 4 times and thus comes back to original position
//                tile.rotateClockwise(1);  // Rotate the tile 1 to check next orientation

                if (top != null && !verticalConnectionIsValid(tile, top)) {
//                    System.out.println("Vertical connection to top tile is invalid.");
                    continue;
                }
                if (right != null && !lateralConnectionIsValid(right, tile)) {
//                    System.out.println("Lateral connection to right tile is invalid.");
                    continue;
                }
                if (bottom != null && !verticalConnectionIsValid(bottom, tile)) {
//                    System.out.println("Vertical connection to bottom tile is invalid.");
                    continue;
                }
                if (left != null && !lateralConnectionIsValid(tile, left)) {
//                    System.out.println("Lateral connection to left tile is invalid.");
                    continue;
                }

//                System.out.println("Adding point " + openTileLocation +
//                        " with tile orientation " + tileOrientation +
//                        " to valid tile placements.");
                Point current = new Point(openTileLocation.x, openTileLocation.y);
                LocationAndOrientation locationAndOrientation = new LocationAndOrientation(current, tileOrientation);
                validPlacements.add(locationAndOrientation);
            }
        }
        return validPlacements;
    }

    // HAS TESTS
    public Tile getTile(Point tileLocation) {
        return boardMatrix[tileLocation.y][tileLocation.x];
    }

    public int getNumTiles(){
        return numTiles;
    }

    public List<Point> getOpenTileLocations(){
        return openTileLocations;
    }

    public Tile getLastPlacedTile(){
        return lastTilePlaced;
    }

    // HAS TEST
    public List<TileSection> getPossibleTileSectionTigerPlacements() {
        List<TileSection> tigerPlacementPossibilities = new ArrayList<>();
        List<TileSection> lastTileSections = lastTilePlaced.getTileSections();
        tigerPlacementPossibilities.addAll(lastTileSections.parallelStream().filter(this::canPlaceTiger)
                                           .collect(Collectors.toList()));
        return tigerPlacementPossibilities;
    }

    private boolean lateralConnectionIsValid(Tile rightTile, Tile leftTile) {
        Node leftEdge = rightTile.getEdge(EdgeLocation.LEFT);
        Node rightEdge = leftTile.getEdge(EdgeLocation.RIGHT);
        boolean result = nodeConnectionIsValid(leftEdge, rightEdge);

        if (leftEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node topLeftCorner = rightTile.getCorner(CornerLocation.TOP_LEFT);
            Node topRightCorner = leftTile.getCorner(CornerLocation.TOP_RIGHT);
            result = result && nodeConnectionIsValid(topLeftCorner, topRightCorner);

            Node bottomLeftCorner = rightTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node bottomRightCorner = leftTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            result = result && nodeConnectionIsValid(bottomLeftCorner, bottomRightCorner);
        }

        return result;
    }

    private void connectLaterally(Tile rightTile, Tile leftTile) throws BadPlacementException {
        Node leftEdge = rightTile.getEdge(EdgeLocation.LEFT);
        Node rightEdge = leftTile.getEdge(EdgeLocation.RIGHT);
        connectNodes(leftEdge, rightEdge);

        if (leftEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node topLeftCorner = rightTile.getCorner(CornerLocation.TOP_LEFT);
            Node topRightCorner = leftTile.getCorner(CornerLocation.TOP_RIGHT);
            connectNodes(topLeftCorner, topRightCorner);

            Node bottomLeftCorner = rightTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node bottomRightCorner = leftTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            connectNodes(bottomLeftCorner, bottomRightCorner);
        }
    }

    private boolean verticalConnectionIsValid(Tile bottomTile, Tile topTile) {
        Node bottomEdge = topTile.getEdge(EdgeLocation.BOTTOM);
        Node topEdge = bottomTile.getEdge(EdgeLocation.TOP);
        boolean result = nodeConnectionIsValid(topEdge, bottomEdge);

        if (bottomEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node bottomRightCorner = topTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            Node topRightCorner = bottomTile.getCorner(CornerLocation.TOP_RIGHT);
            result = result && nodeConnectionIsValid(topRightCorner, bottomRightCorner);

            Node bottomLeftCorner = topTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node topleftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
            result = result && nodeConnectionIsValid(topleftCorner, bottomLeftCorner);
        }

        return result;
    }

    private void connectVertically(Tile bottomTile, Tile topTile) throws BadPlacementException {
        Node bottomEdge = topTile.getEdge(EdgeLocation.BOTTOM);
        Node topEdge = bottomTile.getEdge(EdgeLocation.TOP);
        connectNodes(topEdge, bottomEdge);

        if (bottomEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            Node bottomRightCorner = topTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            Node topRightCorner = bottomTile.getCorner(CornerLocation.TOP_RIGHT);
            connectNodes(topRightCorner, bottomRightCorner);

            Node bottomLeftCorner = topTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node topLeftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
            connectNodes(topLeftCorner, bottomLeftCorner);
        }
    }

    private boolean nodeConnectionIsValid(Node first, Node second) {
        if ((first == null && second != null) || (first != null && second == null) ||
            first.getTileSection().getTerrain() != second.getTileSection().getTerrain()) {
            return false;
        }
        else {
            return true;
        }
    }

    private void connectNodes(Node first, Node second) throws BadPlacementException {
        if ((first == null && second != null) || (first != null && second == null)) {
            throw new BadPlacementException("One corner is null and another is not");
        }

        if (first.getTileSection().getTerrain() != second.getTileSection().getTerrain()) {
            throw new BadPlacementException("Nodes have a mismatch of terrain: " +
                    first.getTileSection().getTerrain() +
                    " != " +
                    second.getTileSection().getTerrain());
        }

        if (first.getTileSection().getRegion() != null && second.getTileSection().getRegion() != null &&
                first.getTileSection().getRegion().getRegionId() !=  second.getTileSection().getRegion().getRegionId()) {
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            try {
                first.getTileSection().getRegion().combineWithRegion(second.getTileSection().getRegion());
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
            regions.remove(first.getTileSection().getRegion().getRegionId());
        }
        else if (first.getTileSection().getRegion() != null) {
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            try {
                first.getTileSection().getRegion().addTileSection(second.getTileSection());
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
        }
        else if (second.getTileSection().getRegion() != null) {
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            try {
                second.getTileSection().getRegion().addTileSection(first.getTileSection());
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
        }
        else {
            Region newRegion = new Region(first.getTileSection().getTerrain());
            first.setConnectedNode(second);
            second.setConnectedNode(first);
            try {
                newRegion.addTileSection(first.getTileSection());
                newRegion.addTileSection(second.getTileSection());
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
            regions.put(newRegion.getRegionId(), newRegion);
        }
    }

    private void setTileAtPoint(Tile tile, Point point) {
        boardMatrix[point.y][point.x] = tile;
    }

    // Checks to see if a tiger can be placed
    private boolean canPlaceTiger(TileSection section) {
        Region region = section.getRegion();
        if (region.containsTigers()) {
            return false;
        }
        return true;
    }

    public void log() throws IOException {

        int rowStart = boardSize;
        int rowStop = 0;
        int colStart = boardSize;
        int colStop = 0;

        int numLogged = 0;
        String output = "";

        for(Point p: openTileLocations) {
            if(p.x < rowStart) {
                rowStart = p.x;
            }
            if(p.x > rowStop) {
                rowStop = p.x;
            }
            if(p.y < colStart) {
                colStart = p.y;
            }
            if(p.y > colStop) {
                colStop = p.y;
            }
        }

        for(int row = rowStart; row < rowStop; row++) {          // for each row
            ArrayList<String> splits = new ArrayList<String>();
            for (int col = colStart; col < colStop; col++) {     // and each column
                if(boardMatrix[row][col] != null) {         // where there is a tile

                    String[] temp = (boardMatrix[row][col].toString().split("\n")); // seperate the lines

                    for(int i = 0; i < temp.length; i++) {  // removes line breaks
                        temp[i].replace("\n", "");
                    }

                    splits.addAll(Arrays.asList(temp));     // and store them in a list
                    numLogged++;


                } else if(numLogged < getNumTiles()) {
                    String[] temp = {
                            "                                ",
                            "                                ",
                            "                                ",
                            "                                ",
                            "                                "};
                    splits.addAll(Arrays.asList(temp));
                }
            }
            if(splits.size() != 0) {
                int lineIndex = 0;
                int count = 0;
                while (lineIndex < 5) {    // for each line in the list
                    output += splits.get(lineIndex + 5 * count);
                    count++;
                    if (count * 5 >= splits.size()) {
                        count = 0;
                        lineIndex++;
                        output += "\n";
                    }
                }
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH.mm");
        Date date = new Date();
        String dest = "logs/"+ dateFormat.format(date).toString() + ".txt";
 //       System.out.println(dest); //2016/11/16_12:08:43

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File("logs/"+dateFormat.format(date).toString()+".txt"), true), "utf-8"))) {
            writer.write(output);
        }

    }

}
