package worldspace;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.IOException;
import java.util.HashMap;
import static worldspace.WorldSpace.ExistingChunks;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class Chunk extends SimpleBoundedRegion {
    
    private HashMap<Integer, Boolean> EvaluatedEvaluators;
    private boolean preGenChunk= false;
    
    
    public Chunk( Coords coords){
        super(coords, WorldSpace.CHUNK_WIDTH, WorldSpace.CHUNK_HEIGHT);

        initGridUnits();
    }
    public Chunk(){

    }
    public void setIsPreGenChunk(boolean isPreGenChunk){
        this.preGenChunk = isPreGenChunk;
    }
    @Override
    public void initGridUnits(){
        setGrid(
            new GridUnit[WorldSpace.CHUNK_HEIGHT]
                    [WorldSpace.CHUNK_WIDTH]);
        
        for(int y = 0; y < WorldSpace.CHUNK_HEIGHT; y++){
            for(int x = 0; x < WorldSpace.CHUNK_WIDTH; x++){                
                GridUnit unit = new GridUnit(new Coords(offset.x+
                        (x*WorldSpace.GRID_SIZE),
                        offset.y-y*WorldSpace.GRID_SIZE));
                //System.out.println(unit.offset.x+", "+unit.offset.y);
                getGrid()[y][x] = unit;
            }
        }
    }                  
    
    public void fill(){
        if(!preGenChunk){
            BaseTerrainEvaluator terrain = new BaseTerrainEvaluator();
            terrain.eval(this);       
        }
        
    }
    
    public void dispose() throws IOException{
        FileHandle handle = Gdx.files.local("data/"+offset.x+"x"+offset.y+".json");
        handle.writeBytes(new byte[] {0}, true);
        
        WorldSpace.objectMapper.writeValue(handle.file(), this); 

        //System.out.println("Chunk saved!");
  
        
    }
}
