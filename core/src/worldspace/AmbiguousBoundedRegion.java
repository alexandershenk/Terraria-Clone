package worldspace;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class AmbiguousBoundedRegion extends WorldSpaceObject implements BoundedRegion{
    private int width;
    private int height;
    private GridUnit[][] grid;
    private BoundedRegionTools util;
    
    public AmbiguousBoundedRegion(Coords start, int width, int height){
        super(start);
        this.width = width;
        this.height = height;
        this.util = new BoundedRegionTools(start, grid);

        setGrid();
        //grid = new GridUnit[height][width];
    }
    
    public AmbiguousBoundedRegion(){}
    
    @Override
    public void setOffset(Coords offset){   
        /*
        if(!WorldSpace.nearestChunkCoordsAt(offset).equals(
                  public void setOffset(Coords offset){   
  WorldSpace.nearestChunkCoordsAt(this.offset))){
             
        }
        this.offset = offset;*/
    }

    @Override
    public GridUnit[][] getGrid() {
        return grid;
    }

    @Override
    public void setGrid(GridUnit[][] grid) {        
        this.grid = grid;
    }
    @Override
    public void setGrid(){
        setGrid(new GridUnit[height][width]);
        Chunk chunk = WorldSpace.chunkAt(offset);

        int chunkX = chunk.getInnerCoords(offset)[0];
        int chunkY = chunk.getInnerCoords(offset)[1];
        
        for(int y=0; y<height; y++){
            for(int x=0; x<width; x++){
                if(chunkX < 0 || chunkX > WorldSpace.CHUNK_WIDTH-1 || 
                        chunkY < 0 || chunkY > WorldSpace.CHUNK_HEIGHT-1){
                    Coords outterGrid = new Coords(chunk.getOffset().x+
                                    chunkX*WorldSpace.GRID_SIZE, 
                                    chunk.getOffset().y-
                                            chunkY*WorldSpace.GRID_SIZE);
                    chunk = WorldSpace.chunkAt(outterGrid);
                    chunkX = chunk.getInnerCoords(outterGrid)[0];
                    chunkY = chunk.getInnerCoords(outterGrid)[1];
                }
                
                grid[y][x] = chunk.getGrid()[chunkY][chunkX];
                chunkX++;
                
            }
            chunkX=chunk.getInnerCoords(offset)[0];
            chunkY++;
        }
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
    public void initGridUnits() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
