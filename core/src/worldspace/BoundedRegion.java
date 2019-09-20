package worldspace;

/**
 *
 * @author roasstbeef (alex.s)
 */
public interface BoundedRegion {
    
    public GridUnit[][] getGrid();
    public GridUnit getGridUnit(Coords coord);
    public void setGrid(GridUnit[][] EvalGrid);
    public void setGrid();

    //public void setWidth(int width);
   // public void setHeight(int height);
    public void setDimensions();

    public int getWidth();
    public int getHeight();
    public int[] getInnerCoords(Coords worldCoords);
    public void initGridUnits();

}
