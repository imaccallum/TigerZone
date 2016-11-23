package entities.overlay;

import entities.board.Board;
import entities.board.Tile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TigerDenTest {
    private Tile mockTile;
    private Point center;
    private List<Point> surroundingPoints;

    @Before
    public void setup() {
        mockTile = mock(Tile.class);
        int centerY = 70;
        int centerX = 70;
        Point above = new Point(centerX, centerY + 1);
        Point below = new Point(centerX, centerY - 1);
        Point right = new Point(centerX + 1, centerY);
        Point left = new Point(centerX - 1, centerY);
        Point aboveRight = new Point(centerX + 1, centerY + 1);
        Point aboveLeft = new Point(centerX - 1, centerY + 1);
        Point belowRight = new Point(centerX + 1, centerY - 1);
        Point belowLeft = new Point(centerX - 1, centerY - 1);

        center = new Point(centerX, centerY);
        surroundingPoints = Arrays.asList(above, below, left, right, aboveLeft, aboveRight, belowLeft, belowRight);

    }

    @Test
    public void testCompletedDenShowsItIsCompleted() {
        TigerDen den = new TigerDen();
        den.setCenterTileLocation(center);
        den.setBoard(createCompleteDenMockBoard());
        Assert.assertTrue(den.isComplete());
    }

    @Test
    public void testIncompleteDensShouldShowIncomplete() {
        for (Point surroundingPoint : surroundingPoints) {
            Board board = createIncompleteDenMockBoard(surroundingPoint);
            TigerDen den = new TigerDen();
            den.setCenterTileLocation(center);
            den.setBoard(board);
            Assert.assertFalse(den.isComplete());
        }
    }

    private Board createCompleteDenMockBoard() {
        Board board = mock(Board.class);
        //Setup surrounding points to return mock tiles
        when(board.getTile(center)).thenReturn(mockTile);
        for (Point point : surroundingPoints) {
        //    when(board.getTile(point)).thenReturn(mockTile);
        }
        return board;
    }

    private Board createIncompleteDenMockBoard(Point removed) {
        Board board = createCompleteDenMockBoard();
        //Setup to remove the one point wanted to be removed
        when(board.getTile(removed)).thenReturn(null);
        return board;
    }
}
