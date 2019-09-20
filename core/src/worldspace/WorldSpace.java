package worldspace;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class WorldSpace {
    public static final int GRID_SIZE = 16;
    public static final int CHUNK_WIDTH = 32; //Units are grids
    public static final int CHUNK_HEIGHT = 32; //Units are grids
    public static HashMap<Coords, Chunk> ExistingChunks = 
            new HashMap<Coords, Chunk>();
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static WorldSpaceRender render = new WorldSpaceRender();
    
    public static void init(){
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibilityChecker(
                objectMapper.getSerializationConfig()
                        .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        
        WorldSpace.objectMapper.setVisibility(PropertyAccessor.ALL, 
                JsonAutoDetect.Visibility.NONE);
        WorldSpace.objectMapper.setVisibility(PropertyAccessor.FIELD, 
                JsonAutoDetect.Visibility.ANY);

    }
    
    //tools
    public static Coords[] getSimpleGridCoords(Coords topLeftCoords){
        return new Coords[]{topLeftCoords, 
            new Coords(topLeftCoords.x+WorldSpace.GRID_SIZE, 
            topLeftCoords.y), 
            new Coords(topLeftCoords.x+WorldSpace.GRID_SIZE, 
            topLeftCoords.y-WorldSpace.GRID_SIZE),
            new Coords(topLeftCoords.x, 
                topLeftCoords.y-WorldSpace.GRID_SIZE)};
    }
    public static Coords nearestChunkCoordsAt(Coords coords){
                //convertii
        return new Coords((int) (Math.floor( (double)coords.x/
                (double)(CHUNK_WIDTH*GRID_SIZE)) * (CHUNK_WIDTH*GRID_SIZE)),
                (int) (Math.ceil( (double) coords.y/
                (double) (CHUNK_HEIGHT*GRID_SIZE)) * (CHUNK_HEIGHT*GRID_SIZE)));

    }

    public static Coords nearestGridCoordsAt(Coords coords){
                //convertii
        return new Coords((int) (Math.floor( (double)coords.x/
                (double)(GRID_SIZE)) * (GRID_SIZE)),
                (int) (Math.ceil( (double) coords.y/
                (double) (GRID_SIZE)) * (GRID_SIZE)));

    }
    
    public static Chunk chunkAt(Coords coords){
        return ExistingChunks.get(nearestChunkCoordsAt(coords));
    }
    
    public static Chunk chunkAt(Coords coords, boolean loadIfUnloaded) throws IOException{
        
        if(loadIfUnloaded){
            if(ExistingChunks.get(nearestChunkCoordsAt(coords)) == null)
                loadChunkAt(coords);
        }
        return ExistingChunks.get(nearestChunkCoordsAt(coords));
        
    }    
    public static void loadChunkAt(Coords coords) throws IOException{
        long start = System.currentTimeMillis();
        Chunk chunk;

        //if(Gdx.files.internal("data/"+coords.x+"x"+coords.y+".json").exists()){
        if(false){
            chunk = objectMapper.readValue(
                    Gdx.files.internal("data/"+coords.x+"x"+coords.y+".json")
                            .readString(), Chunk.class);
            chunk.setIsPreGenChunk(true);
        }
        else{
            chunk=new Chunk(coords);
            //System.out.println("fe");
        }
        ExistingChunks.put(coords, chunk);
        
        chunk.fill();
        //System.out.println(System.currentTimeMillis() - start);
    }
}
