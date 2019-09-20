package worldspace;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class Coords {
    public int x, y;

    public static enum OffsetType {
        GRID, CHUNK
    }
    
    
    public boolean equals(Object o) {
        Coords c = (Coords) o;
        return c.x == x && c.y == y;
    }

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Coords(){
        this.x=0;
        this.y=0;
    }
    
    public void offset(OffsetType offsetType, int x, int y){
        switch(offsetType){
            case CHUNK:
                this.x += x*WorldSpace.CHUNK_WIDTH*WorldSpace.GRID_SIZE;
                this.y += y*WorldSpace.CHUNK_HEIGHT*WorldSpace.GRID_SIZE;
            case GRID:
                this.x += x*WorldSpace.GRID_SIZE;
                this.y += y*WorldSpace.GRID_SIZE;

        }
    }

    public int hashCode() {
      int tmp = ( y +  ((x+1)/2));
               return x +  ( tmp * tmp);
    }
}
