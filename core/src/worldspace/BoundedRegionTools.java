package worldspace;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class BoundedRegionTools {
    private Coords offset;
    private GridUnit[][] grid;
    
    public BoundedRegionTools(){}
    public BoundedRegionTools(Coords offset, GridUnit[][] grid){
        this.grid=grid;
        this.offset = offset;
    }
    public GridUnit getGridUnit(Coords coord) {
        int x = getInnerCoords(coord)[0];
        int y = getInnerCoords(coord)[1]; 
        
        return grid[y][x];
    }
    public int[] getInnerCoords(Coords worldCoords){
        return new int[] {
            (worldCoords.x-this.offset.x)/WorldSpace.GRID_SIZE,
            (((worldCoords.y-this.offset.y)/WorldSpace.GRID_SIZE)/-1)}; 
    }

}
