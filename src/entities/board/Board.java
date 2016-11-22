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
    private HashSet<Point> openTileLocations;
    private Map<UUID, Region> regions;
    private List<TigerDen> tigerDens;
    private Tile lastTilePlaced;

    private int boardSize;
    private int numTiles;

    /**
     * Constucts a board with a given tile stack size and a first tile.
     *
     * @param numberOfTiles,
     * The number of tiles that will eventually be placed on the board
     *
     * @param firstTile
     * The first tile to be placed on the board.
     */
    public Board(int numberOfTiles, Tile firstTile) {
        boardMatrix = new Tile[numberOfTiles * 2][numberOfTiles * 2];
        boardSize = numberOfTiles * 2;
        openTileLocations = new HashSet<>();
        regions = new HashMap<>();
        tigerDens = new ArrayList<>();

        // System.out.println(firstTile.type);
        // Put the first tile down and set all of the open tile locations
        setTileAtPoint(firstTile, new Point(numberOfTiles - 1, numberOfTiles - 1));
        lastTilePlaced = firstTile;
        openTileLocations.add(new Point(numberOfTiles - 1, numberOfTiles));
        openTileLocations.add(new Point(numberOfTiles - 2, numberOfTiles - 1));
        openTileLocations.add(new Point(numberOfTiles, numberOfTiles - 1));
        openTileLocations.add(new Point(numberOfTiles - 1, numberOfTiles - 2));
        numTiles = 1;
    }

    // HAS TESTS - Bookkeeping
    /**
     * places a given tile at a point on the board.
     *
     * @param tile,
     * the tile to be put on the board.
     *
     * @param location,
     * the location as a Point, standard array indexing, x = column, y = row in the matrix.
     *
     * @throws BadPlacementException
     * if a point is out of bounds or a tile does not match the surrounding terrain.
     */
    public void place(Tile tile, Point location) throws BadPlacementException {
        // For naming consistent with orientation of tile matrix, get x and y as row, col integers
        int row = location.y;
        int col = location.x;

        // Get the surrounding tiles of the placement.
        Tile leftTile = boardMatrix[row][col-1];
        Tile rightTile = boardMatrix[row][col+1];
        Tile bottomTile = boardMatrix[row+1][col];
        Tile topTile = boardMatrix[row-1][col];

        // If they are all null, we are trying to place a tile that will not be adjacent to any other tile and thus
        // throw a bad tile placement exception.
        if (leftTile == null && rightTile == null && topTile == null && bottomTile == null) {
            throw new BadPlacementException("Index given is out of bounds");
        }

        // Put the tile in the matrix, get ready to connect the tiles.
        setTileAtPoint(tile, location);
        numTiles++;
//        openTileLocations.remove(new Point(col, row));
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

    // HAS TEST - Bookkeeping

    /**
     * Finds the valid tile placements as a list of locations and orientations in location, orientation order
     *
     * @param tile,
     * the tile that we are trying to find valid placements for
     *
     * @return
     * The list of type LocationAndOrientation, contains a Location
     * as a point and an Orientation as an integer
     */
    public List<LocationAndOrientation> findValidTilePlacements(Tile tile) {
        List<LocationAndOrientation> validPlacements = new ArrayList<>();
        for (Point openTileLocation : openTileLocations) {   // for each open tile
            int col = openTileLocation.x;
            int row = openTileLocation.y;
            Tile left = boardMatrix[row][col - 1];
            Tile right = boardMatrix[row][col + 1];
            Tile top = boardMatrix[row - 1][col];
            Tile bottom = boardMatrix[row + 1][col];

            for (int tileOrientation = 0; tileOrientation < 4; ++tileOrientation, tile.rotateCounterClockwise(1)) {
                // By placing this at the end the tile is rotated 4 times and thus comes back to original position
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

                System.out.println("Adding point " + openTileLocation +
                        " with tile orientation " + tileOrientation +
                        " to valid tile placements.");
                Point current = new Point(openTileLocation.x, openTileLocation.y);
                LocationAndOrientation locationAndOrientation = new LocationAndOrientation(current, tileOrientation);
                validPlacements.add(locationAndOrientation);
                tile.rotateCounterClockwise(1);  // Rotate the tile 1 to check next orientation
            }
        }
        return validPlacements;
    }

    public int getFilledValues(){
        int val = 0;
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(boardMatrix[i][j] != null)
                    val++;
            }
        }
        return val;
    }

    // HAS TESTS - bookkeeping

    /**
     * Gets a tile at a given point on the board.  Can return null.
     * @param tileLocation,
     * the point for the tile to be retrieved
     *
     * @return
     * The tile that was found at the given tile location.
     */
    public Tile getTile(Point tileLocation) {
        return boardMatrix[tileLocation.y][tileLocation.x];
    }

    /**
     * Get the number of tiles
     *
     * @return
     * Get the number of tiles that have been placed on the board
     */
    public int getNumTiles() {
        return numTiles;
    }

    /**
     * Get the Open tile locations as a list of points
     *
     * @return
     * The HashSet of Points
     */
    public HashSet<Point> getOpenTileLocations() {
        return openTileLocations;
    }

    /**
     * Get the last tile placed tile on the board
     *
     * @return
     * The last tile placed
     */
    public Tile getLastPlacedTile() {
        return lastTilePlaced;
    }

    //
    // Get the possible tile sections where a tile can be placed
    //
    // @return
    // The list of the tile sections
    //
    public List<TileSection> getPossibleTileSectionTigerPlacements() {
        List<TileSection> tigerPlacementPossibilities = new ArrayList<>();
        List<TileSection> lastTileSections = lastTilePlaced.getTileSections();
        tigerPlacementPossibilities.addAll(lastTileSections.parallelStream().filter(this::canPlaceTiger)
                                           .collect(Collectors.toList()));
        return tigerPlacementPossibilities;
    }

    //
    // Find out whether a given lateral connection is valid
    //
    // @param rightTile,
    // The right tile in the lateral connection
    //
    // @param leftTile,
    // The left tile in the lateral connection
    //
    // @return
    // The boolean as to whether the connection is valid.
    private boolean lateralConnectionIsValid(Tile rightTile, Tile leftTile) {
        // Get the edges to be connected
        Node leftEdge = rightTile.getEdge(EdgeLocation.LEFT);
        Node rightEdge = leftTile.getEdge(EdgeLocation.RIGHT);
        boolean result = nodeConnectionIsValid(leftEdge, rightEdge);

        if (leftEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            // Since the middle terrain is a trail, get the corners and connect them up as well
            Node topLeftCorner = rightTile.getCorner(CornerLocation.TOP_LEFT);
            Node topRightCorner = leftTile.getCorner(CornerLocation.TOP_RIGHT);
            result = result && nodeConnectionIsValid(topLeftCorner, topRightCorner);

            Node bottomLeftCorner = rightTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node bottomRightCorner = leftTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            result = result && nodeConnectionIsValid(bottomLeftCorner, bottomRightCorner);
        }

        return result;
    }

    //
    // Connect two tiles laterally
    //
    // @param rightTile,
    // The right tile of the lateral connection
    //
    // @param leftTile,
    // The left tile of the lateral connection
    //
    private void connectLaterally(Tile rightTile, Tile leftTile) throws BadPlacementException {
        // The edges to be connected
        Node leftEdge = rightTile.getEdge(EdgeLocation.LEFT);
        Node rightEdge = leftTile.getEdge(EdgeLocation.RIGHT);
        connectNodes(leftEdge, rightEdge);

        if (leftEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            // Since the middle terrain is a trail, connect the corners
            Node topLeftCorner = rightTile.getCorner(CornerLocation.TOP_LEFT);
            Node topRightCorner = leftTile.getCorner(CornerLocation.TOP_RIGHT);
            connectNodes(topLeftCorner, topRightCorner);

            Node bottomLeftCorner = rightTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node bottomRightCorner = leftTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            connectNodes(bottomLeftCorner, bottomRightCorner);
        }
    }

    //
    // Find out whether a given vertical connection is valid
    //
    // @param bottomTile,
    // The bottom tile in the vertical connection
    //
    // @param topTile,
    // The top tile in the vertical connection
    //
    // @return
    // The boolean as to whether the connection is valid.
    private boolean verticalConnectionIsValid(Tile bottomTile, Tile topTile) {
        // The edges to be connected
        Node bottomEdge = topTile.getEdge(EdgeLocation.BOTTOM);
        Node topEdge = bottomTile.getEdge(EdgeLocation.TOP);
        boolean result = nodeConnectionIsValid(topEdge, bottomEdge);

        if (bottomEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            // Since the middle terrain is a trail, get the corners and connect them up as well
            Node bottomRightCorner = topTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            Node topRightCorner = bottomTile.getCorner(CornerLocation.TOP_RIGHT);
            result = result && nodeConnectionIsValid(topRightCorner, bottomRightCorner);

            Node bottomLeftCorner = topTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node topleftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
            result = result && nodeConnectionIsValid(topleftCorner, bottomLeftCorner);
        }

        return result;
    }

    //
    // Connect two tiles vertically
    //
    // @param bottomTile,
    // The bottom tile of the vertical connection
    //
    // @param topTile,
    // The top tile of the vertical connection
    //
    private void connectVertically(Tile bottomTile, Tile topTile) throws BadPlacementException {
        // The edges to be connected
        Node bottomEdge = topTile.getEdge(EdgeLocation.BOTTOM);
        Node topEdge = bottomTile.getEdge(EdgeLocation.TOP);
        connectNodes(topEdge, bottomEdge);

        if (bottomEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            // Since the middle terrain is a trail, connect the corners
            Node bottomRightCorner = topTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            Node topRightCorner = bottomTile.getCorner(CornerLocation.TOP_RIGHT);
            connectNodes(topRightCorner, bottomRightCorner);

            Node bottomLeftCorner = topTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node topLeftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
            connectNodes(topLeftCorner, bottomLeftCorner);
        }
    }

    //
    // Attempt to connect two nodes, check for existance, same terrain, and not connected
    //
    // @param first,
    // The first node in the connection
    //
    // @param second,
    // The second node in the connection
    //
    // @return
    // The boolean result if all conditions for a connection is correct.
    //
    private boolean nodeConnectionIsValid(Node first, Node second) {
        if (first == null || second == null || first.isConnected() || second.isConnected()) {
            return false;
        }
        else if (first.getTileSection().getTerrain() != second.getTileSection().getTerrain()) {
            return false;
        }
        return true;
    }

    //
    // Connect two nodes
    //
    // @param first,
    // The first node to be connected
    //
    // @param second,
    // The second node to be connected
    //
    // @throws BadPlacementException if the two nodes cannot be connected
    //
    private void connectNodes(Node first, Node second) throws BadPlacementException {
        if (first == null || second == null) {
            throw new BadPlacementException("One of two nodes to be connected is null");
        } else if (first.getTileSection().getTerrain() != second.getTileSection().getTerrain()) {
            throw new BadPlacementException("Nodes have a mismatch of terrain: " +
                    first.getTileSection().getTerrain() +
                    " != " +
                    second.getTileSection().getTerrain());
        }

        if (first.getTileSection().getRegion() != null && second.getTileSection().getRegion() != null &&
                first.getTileSection().getRegion().getRegionId() != second.getTileSection().getRegion().getRegionId()) {
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
            // Connect the nodes to each other
            first.setConnectedNode(second);
            second.setConnectedNode(first);

            // If the first region exists, add the second tile section to the first region.
            try {
                first.getTileSection().getRegion().addTileSection(second.getTileSection());
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
        }
        else if (second.getTileSection().getRegion() != null) {
            // Connect the nodes to each other
            first.setConnectedNode(second);
            second.setConnectedNode(first);

            // If the second region exists, add the first tile section to the second region.
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

    //
    private void setTileAtPoint(Tile tile, Point point) {
        boardMatrix[point.y][point.x] = tile;
    }

    // Checks to see if a tiger can be placed
    public boolean canPlaceTiger(TileSection section) {
        Region region = section.getRegion();
        if (region.containsTigers()) {
            return false;
        }
        return true;
    }


    // Create the log string for the board.
    private String logString() {
        int rowStart = boardSize;
        int rowStop = 0;
        int colStart = boardSize;
        int colStop = 0;

        int numLogged = 0;
        String output = "";

        for(Point p: openTileLocations) {
            if(p.y < rowStart) {
                rowStart = p.y;
            }
            if(p.y > rowStop) {
                rowStop = p.y;
            }
            if(p.x < colStart) {
                colStart = p.x;
            }
            if(p.x > colStop) {
                colStop = p.x;
            }
        }

        for(int row = rowStart; row < rowStop; row++) {          // for each row
            ArrayList<String> splits = new ArrayList<>();
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

        return output;
    }

    // Log the state of the board to a file.
    public void log() throws IOException {
        // Get the output string for the board.
        String output = logString();

        // Create a destination for a file.
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH.mm");
        Date date = new Date();
        String destination = "logs/"+ dateFormat.format(date) + ".txt";
        File file = new File(destination);

        // Write to the file.
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "utf-8"))) {
            writer.write(output);
        }

    }

}
