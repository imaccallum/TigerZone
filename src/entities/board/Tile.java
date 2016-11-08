package entities.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

// Class Tile
//
// Represents a tile on the board
// Has 12 nodes, and points to the top, bottom, left, and right adjacent tiles
//
//                 top
//                  ^
//                  |
//               1  2  3
//               -------
//           12 |       | 4
//   left <- 11 |       | 5 -> right
//           10 |       | 6
//               -------
//               9  8  7
//                  |
//                  v
//                bottom
//
// These are the "standard locations" of these node numbers.
// The Tile is composed of sections, each of which has a list of the node numbers that it connects to, and this is how
// tiles are validated as being able to be placed next to each other.
//
public class Tile {
    private UUID tileId;
    private List<TileSection> sections;
    private int rotationalOffset;
    private Tile leftAdjacentTile;
    private Tile rightAdjacentTile;
    private Tile topAdjacentTile;
    private Tile bottomAdjacentTile;


    public Tile() {
        tileId = UUID.randomUUID();  // Create a random unique id to identify the tile
        sections = new ArrayList<>();  // Initialize the list to have no sections
        rotationalOffset = 0;  // Rotational offset starts at 0 for no rotations
    }

    // Rotates a tile counter clockwise a specified number of times
    public void rotateCounterClockwise(int numberOfTimes) {
        for (int i = 0; i < numberOfTimes; ++i) {
            rotationalOffset = (rotationalOffset + 3) % 12;
        }
    }

    // Gets the section associated with a given standard location node number
    public TileSection sectionForNodeNumber(int nodeNumber) {
        // Transform the node number based on possible tile rotations.
        int localNodeNumber = nodeNumber + rotationalOffset;
        for (TileSection section : sections) {
            // Find the section that contains the node number.
            if (section.contains(localNodeNumber)) {
                return section;
            }
        }
        return null;
    }

    public void addSections(TileSection... sectionsToAdd) {
        sections.addAll(Arrays.asList(sectionsToAdd));
    }


    // MARK: Getters and setters


    public Tile getLeftAdjacentTile() {
        return leftAdjacentTile;
    }

    public void setLeftAdjacentTile(Tile leftAdjacentTile) {
        this.leftAdjacentTile = leftAdjacentTile;
    }

    public Tile getRightAdjacentTile() {
        return rightAdjacentTile;
    }

    public void setRightAdjacentTile(Tile rightAdjacentTile) {
        this.rightAdjacentTile = rightAdjacentTile;
    }

    public Tile getTopAdjacentTile() {
        return topAdjacentTile;
    }

    public void setTopAdjacentTile(Tile topAdjacentTile) {
        this.topAdjacentTile = topAdjacentTile;
    }

    public Tile getBottomAdjacentTile() {
        return bottomAdjacentTile;
    }

    public void setBottomAdjacentTile(Tile bottomAdjacentTile) {
        this.bottomAdjacentTile = bottomAdjacentTile;
    }

    public UUID getTileId() {
        return tileId;
    }

}
