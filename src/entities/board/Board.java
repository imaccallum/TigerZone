package entities.board;

import entities.overlay.Region;
import entities.overlay.TigerDen;
import entities.overlay.TileSection;
import exceptions.BadPlacementException;
import exceptions.IncompatibleTerrainException;
import exceptions.StackingTigerException;
import exceptions.TigerAlreadyPlacedException;
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
    private Set<Tiger> placedTigers;
    private Point centerLocation;
    private Stack<Stack<RegionMerge>> regionMergesForEachPlacedTile;
    private Stack<Tile> tilesPlacedInOrder;

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
        placedTigers = new HashSet<>();
        regionMergesForEachPlacedTile = new Stack<>();
        tilesPlacedInOrder = new Stack<>();

        centerLocation = new Point(numberOfTiles - 1, numberOfTiles - 1);
        // System.out.println(firstTile.type);
        // Put the first tile down and set all of the open tile locations
        setTileAtLocation(firstTile, centerLocation);

        // Add all of the regions of the first tile
        for (TileSection tileSection: firstTile.getTileSections()) {
            regions.put(tileSection.getRegion().getRegionId(), tileSection.getRegion());
        }

        tilesPlacedInOrder.push(firstTile);
        openTileLocations.add(new Point(numberOfTiles - 1, numberOfTiles));
        openTileLocations.add(new Point(numberOfTiles - 2, numberOfTiles - 1));
        openTileLocations.add(new Point(numberOfTiles, numberOfTiles - 1));
        openTileLocations.add(new Point(numberOfTiles - 1, numberOfTiles - 2));
        numTiles = 1;
    }


    /**
     * Remove the last placed tile from the board
     *
     * @return
     * The tile that was removed
     */
    public Tile removeLastPlacedTile() {
        Tile tile = tilesPlacedInOrder.pop();
        Point location = tile.getLocation();
        boardMatrix[location.y][location.x] = null;

        // Disconnect the nodes
        for (Node node : tile.nodesClockwise()) {
            if(node!=null) {
                for (Node connectedNode : node.getConnectedNodes()) {
                    connectedNode.getConnectedNodes().remove(node);
                }
                node.getConnectedNodes().clear();
            }
        }

        Stack<RegionMerge> mergesToUndo = regionMergesForEachPlacedTile.pop();

        // Undo the merges in reverse order
        while (!mergesToUndo.isEmpty()) {
            undoRegionMerge(mergesToUndo.pop());
        }

        tile.setLocation(null, centerLocation);

        numTiles--;
        openTileLocations.add(location);
        return tile;
    }

    /**
     * Places a tile on the board
     *
     * @param tile,
     * The tile to be placed on the board
     *
     * @param location,
     * The location to be placed
     *
     * @throws BadPlacementException if the placement is not a good one.
     */
    public void place(Tile tile, Point location) throws BadPlacementException {
        Stack<RegionMerge> regionMerges = new Stack<>();

        // For naming consistent with orientation of tile matrix, get x and y as row, col integers
        int row = location.y + centerLocation.y;
        int col = location.x + centerLocation.x;

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
        setTileAtLocation(tile, location);
        numTiles++;
        openTileLocations.remove(location);

        // Add all of the regions to the list.
        for (TileSection tileSection: tile.getTileSections()) {
            regions.put(tileSection.getRegion().getRegionId(), tileSection.getRegion());
        }

        // For each non-null tile, connect the tile's tileSections / regions / nodes so that the overlay is updated
        if (leftTile != null) {
            // Add all at 0 to preserve order of stack
            regionMerges.addAll(connectLaterally(tile, leftTile));
        } else {
            openTileLocations.add(new Point(col-1, row));
        }
        if (rightTile != null) {
            // Add all at 0 to preserve order of stack
            regionMerges.addAll(connectLaterally(rightTile, tile));
        } else {
            openTileLocations.add(new Point(col+1, row));
        }
        if (topTile != null) {
            // Add all at 0 to preserve order of stack
            regionMerges.addAll(connectVertically(tile, topTile));
        } else {
            openTileLocations.add(new Point(col, row-1));
        }
        if (bottomTile != null) {
            // Add all at 0 to preserve order of stack
            regionMerges.addAll(connectVertically(bottomTile, tile));
        } else {
            openTileLocations.add(new Point(col, row+1));
        }

        if (tile.getDen() != null) {
            TigerDen den = tile.getDen();
            den.setCenterTileLocation(location);
            den.setBoard(this);
            tigerDens.add(den);
        }

        tilesPlacedInOrder.push(tile);
        regionMergesForEachPlacedTile.push(regionMerges);
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
                    continue;
                }
                if (right != null && !lateralConnectionIsValid(right, tile)) {
                    continue;
                }
                if (bottom != null && !verticalConnectionIsValid(bottom, tile)) {
                    continue;
                }
                if (left != null && !lateralConnectionIsValid(tile, left)) {
                    continue;
                }

                Point current = new Point(openTileLocation.x, openTileLocation.y);
                LocationAndOrientation locationAndOrientation = new LocationAndOrientation(current, tileOrientation);
                validPlacements.add(locationAndOrientation);
            }
        }
        return validPlacements;
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


    public Tile getTileFromServerLocation(Point serverLocation) {
        int x = serverLocation.x + centerLocation.x;
        int y = serverLocation.y + centerLocation.y;
        return getTile(new Point(x, y));
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
        return tilesPlacedInOrder.peek();
    }

    /**
     * Get a string representation of the board and all the tiles on it
     *
     * @return
     * A string representation of the board
     */
    public String toString() {
        int rowStart = boardSize;
        int rowStop = 0;
        int colStart = boardSize;
        int colStop = 0;

        int numLogged = 0;
        String output = "";

        for (Point p : openTileLocations) {
            if (p.y < rowStart) {
                rowStart = p.y;
            }
            if (p.y > rowStop) {
                rowStop = p.y;
            }
            if (p.x < colStart) {
                colStart = p.x;
            }
            if (p.x > colStop) {
                colStop = p.x;
            }
        }

        for (int row = rowStart; row < rowStop; row++) {          // for each row
            ArrayList<String> splits = new ArrayList<>();
            for (int col = colStart; col < colStop; col++) {     // and each column
                if (boardMatrix[row][col] != null) {         // where there is a tile

                    String[] temp = (boardMatrix[row][col].toString().split("\n")); // seperate the lines

                    for (int i = 0; i < temp.length; i++) {  // removes line breaks
                        temp[i].replace("\n", "");
                    }

                    splits.addAll(Arrays.asList(temp));     // and store them in a list
                    numLogged++;


                } else if (numLogged < getNumTiles()) {
                    String[] temp = {
                            "                                ",
                            "                                ",
                            "                                ",
                            "                                ",
                            "                                ",
                            "                                "};
                    splits.addAll(Arrays.asList(temp));
                }
            }
            if (splits.size() != 0) {
                int lineIndex = 0;
                int count = 0;
                while (lineIndex < 6) {    // for each line in the list
                    output += splits.get(lineIndex + 6 * count);
                    count++;
                    if (count * 6 >= splits.size()) {
                        count = 0;
                        lineIndex++;
                        output += "\n";
                    }
                }
            }
        }

        return output;
    }

    /**
     * Gets the region associated with a region id (UUID)
     *
     * @param regionId,
     * The UUID associated with the region
     *
     * @return
     * The region for that id
     */
    public Region regionForId(UUID regionId) {
        return regions.get(regionId);
    }

    /**
     * Add a tiger to the board that is already placed on the board
     *
     * @param tiger,
     * The tiger to be added
     */
    public void addPlacedTiger(Tiger tiger) {
        placedTigers.add(tiger);
    }

    /**
     * Remove a tiger from the board
     *
     * @param location,
     * The tile location to remove the tiger from
     */
    public void removeTigerFromTileAt(Point location) {
        Tile tileToRemoveTigerFrom = getTile(location);
        Tiger removedTiger = null;
        if (tileToRemoveTigerFrom.getDen().getTiger() != null) {
            removedTiger = tileToRemoveTigerFrom.getDen().getTiger();
            tileToRemoveTigerFrom.getDen().removeTiger();
        }
        else {
            for (TileSection tileSection : tileToRemoveTigerFrom.getTileSections()) {
                if  (tileSection.getTiger() != null) {
                    removedTiger = tileSection.getTiger();
                    tileSection.removeTiger();
                }
            }
        }
        if (removedTiger != null) {
            placedTigers.remove(removedTiger);
        }
    }

    /**
     * Stacks a tiger
     * @param location,
     * The location of the tile to stack the tiger at
     *
     * @throws StackingTigerException if there is already a tiger or whatever
     */
    public void stackTigerAt(Point location) throws StackingTigerException {
        Tile tile = getTileFromServerLocation(location);
        Tiger tiger;
        if (tile.getDen().getTiger() != null) {
            tiger = tile.getDen().getTiger();
            if (!placedTigers.contains(tiger)) {
                throw new StackingTigerException("Tiger not placed on the board");
            } else if (tiger.isStacked()) {
                throw new StackingTigerException("Tiger is already stacked");
            } else {
                // Tigers are final value types, remove old, create new that is stacked, add to placed tigers
                tiger = new Tiger(tiger.getOwningPlayerName(), true);
            }
            tile.getDen().removeTiger();
            try {
                tile.getDen().placeTiger(tiger);
            } catch (TigerAlreadyPlacedException e) {
                throw new StackingTigerException(e.getMessage());
            }
        }
        else {
            for (TileSection tileSection : tile.getTileSections()) {
                if  (tileSection.getTiger() != null) {
                    tiger = tileSection.getTiger();
                    if (!placedTigers.contains(tiger)) {
                        throw new StackingTigerException("Tiger not placed on the board");
                    } else if (tiger.isStacked()) {
                        throw new StackingTigerException("Tiger is already stacked");
                    } else {
                        // Tigers are final value types, remove old, create new that is stacked, add to placed tigers
                        tiger = new Tiger(tiger.getOwningPlayerName(), true);
                    }
                    tileSection.removeTiger();
                    try {
                        tileSection.placeTiger(tiger);
                    } catch (TigerAlreadyPlacedException e) {
                        throw new StackingTigerException(e.getMessage());
                    }
                }
            }
        }
    }

    //
    // Get the possible tile sections where a tile can be placed
    //
    // @return
    // The list of the tile sections
    //
    public List<TileSection> getPossibleTileSectionTigerPlacements() {
        List<TileSection> tigerPlacementPossibilities = new ArrayList<>();
        List<TileSection> lastTileSections = getLastPlacedTile().getTileSections();
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
    private Stack<RegionMerge> connectLaterally(Tile rightTile, Tile leftTile) throws BadPlacementException {
        Stack<RegionMerge> mergedRegionsStack = new Stack<>();
        // The edges to be connected
        Node leftEdge = rightTile.getEdge(EdgeLocation.LEFT);
        Node rightEdge = leftTile.getEdge(EdgeLocation.RIGHT);
        RegionMerge merge = connectNodes(leftEdge, rightEdge);

        if (merge != null) {
            mergedRegionsStack.push(merge);
        }

        if (leftEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            // Since the middle terrain is a trail, connect the corners
            Node topLeftCorner = rightTile.getCorner(CornerLocation.TOP_LEFT);
            Node topRightCorner = leftTile.getCorner(CornerLocation.TOP_RIGHT);
            merge = connectNodes(topLeftCorner, topRightCorner);
            if (merge != null) {
                mergedRegionsStack.push(merge);
            }

            Node bottomLeftCorner = rightTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node bottomRightCorner = leftTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            merge = connectNodes(bottomLeftCorner, bottomRightCorner);
            if (merge != null) {
                mergedRegionsStack.push(merge);
            }
        }
        return mergedRegionsStack;
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
    private Stack<RegionMerge> connectVertically(Tile bottomTile, Tile topTile) throws BadPlacementException {
        Stack<RegionMerge> mergedRegionsStack = new Stack<>();
        // The edges to be connected
        Node bottomEdge = topTile.getEdge(EdgeLocation.BOTTOM);
        Node topEdge = bottomTile.getEdge(EdgeLocation.TOP);
        RegionMerge merge = connectNodes(topEdge, bottomEdge);
        if (merge != null) {
            mergedRegionsStack.push(merge);
        }

        if (bottomEdge.getTileSection().getTerrain() == Terrain.TRAIL) {
            // Since the middle terrain is a trail, connect the corners
            Node bottomRightCorner = topTile.getCorner(CornerLocation.BOTTOM_RIGHT);
            Node topRightCorner = bottomTile.getCorner(CornerLocation.TOP_RIGHT);
            merge = connectNodes(topRightCorner, bottomRightCorner);
            if (merge != null) {
                mergedRegionsStack.push(merge);
            }

            Node bottomLeftCorner = topTile.getCorner(CornerLocation.BOTTOM_LEFT);
            Node topLeftCorner = bottomTile.getCorner(CornerLocation.TOP_LEFT);
            merge = connectNodes(topLeftCorner, bottomLeftCorner);
            if (merge != null) {
                mergedRegionsStack.push(merge);
            }
        }
        return mergedRegionsStack;
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
    private RegionMerge connectNodes(Node first, Node second) throws BadPlacementException {
        if (first == null || second == null) {
            throw new BadPlacementException("One of two nodes to be connected is null");
        } else if (first.getTileSection().getTerrain() != second.getTileSection().getTerrain()) {
            throw new BadPlacementException("Nodes have a mismatch of terrain: " +
                    first.getTileSection().getTerrain() +
                    " != " +
                    second.getTileSection().getTerrain());
        }

        first.addConnectedNode(second);
        second.addConnectedNode(first);
        if (first.getTileSection().getRegion() != null && second.getTileSection().getRegion() != null &&
                first.getTileSection().getRegion().getRegionId() != second.getTileSection().getRegion().getRegionId()) {
            try {
                Region firstRegion = first.getTileSection().getRegion();
                Region secondRegion = second.getTileSection().getRegion();
                Region newRegion = new Region(firstRegion, secondRegion);
                RegionMerge merge = new RegionMerge(firstRegion, secondRegion, newRegion);
                regions.remove(firstRegion.getRegionId());
                regions.remove(secondRegion.getRegionId());
                regions.put(newRegion.getRegionId(), newRegion);
                return merge;
            } catch (IncompatibleTerrainException e) {
                throw new BadPlacementException(e.getMessage());
            }
        }
        return null;
    }

    // Sets a tile to a location in the board matrix and gives the tile that location
    private void setTileAtLocation(Tile tile, Point location) {
        boardMatrix[location.y][location.x] = tile;
        tile.setLocation(location, new Point(boardSize/2 - 1, boardSize/2 - 1));
    }

    //
    // Undoes a region merge
    //
    // @param mergeToUndo
    //
    private void undoRegionMerge(RegionMerge mergeToUndo) {
        Region newRegion = mergeToUndo.newRegion;
        List<Region> oldRegions = Arrays.asList(mergeToUndo.firstOldRegion, mergeToUndo.secondOldRegion);
        for (Region oldRegion : oldRegions) {
            newRegion.getTileSections().stream()
                    .filter(tileSection -> oldRegion.getTileSections().contains(tileSection))
                    .forEach(tileSection -> {
                        tileSection.setRegion(oldRegion);
                    });
            regions.put(oldRegion.getRegionId(), oldRegion);
        }
        regions.remove(newRegion.getRegionId());
    }

    // Checks to see if a tiger can be placed
    public boolean canPlaceTiger(TileSection section) {
        Region region = section.getRegion();
        if (region.containsTigers()) {
            return false;
        }
        return true;
    }

    // Log the state of the board to a file.
    public void log() throws IOException {
        // Get the output string for the board.
        String output = toString();

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

    public List<Region> regionsAsList() {
        return new ArrayList<>(regions.values());
    }

    public Point getServerLocation(Point serverLocation) {
        return new Point(serverLocation.x - centerLocation.x, serverLocation.y - centerLocation.y);
    }
}
