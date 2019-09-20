package worldspace;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class SimpleBoundedRegion extends WorldSpaceObject implements BoundedRegion {
    private Coords start;
    private int width;
    private int height;
    protected GridUnit grid[][];
    protected BoundedRegionTools util;
    
    public SimpleBoundedRegion(Coords start, int width, int height){
        super(start);
        this.start = start;
        this.width = width;
        this.height = height;
        grid = new GridUnit[height][width];
        util = new BoundedRegionTools(start, grid);
    }
    
    public SimpleBoundedRegion(){}

    @Override
    public GridUnit[][] getGrid() {
        return grid;
    }

    @Override
    public GridUnit getGridUnit(Coords coord) {
        return util.getGridUnit(coord);
    }
    @Override
    public int[] getInnerCoords(Coords worldCoords){
        return util.getInnerCoords(worldCoords);
    }
    @Override
    public void setGrid(GridUnit[][] grid) {
        this.grid = grid;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
    
    @Override
    public void setDimensions() {
    }

    @Override
    public void setGrid() {
    }

    @Override
    public void initGridUnits() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    
}
