package entities.board;

public class TileCoordinate {
    private int x;
    private int y;

    public TileCoordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(TileCoordinate tc){
        if(this.getX() == tc.getX() && this.getY() == tc.getY())
            return true;
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
