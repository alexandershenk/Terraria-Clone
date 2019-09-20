package worldspace;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class WorldWindow extends WorldSpaceObject {

    public ArrayList<ArrayList<Chunk>> window;
    public final int width=3;
    public final int height=3;

    public WorldWindow(Coords offset) throws IOException {
        super(offset);
        this.window = new ArrayList<ArrayList<Chunk>>();

        //init fill window: 
        for (int y = 0; y < height; y++) {
            ArrayList<Chunk>  chunkRow = new ArrayList<Chunk>();
            window.add(chunkRow);
            for (int x = 0; x < width; x++) {
                chunkRow.add(WorldSpace.chunkAt(
                        new Coords(
                                offset.x + (WorldSpace.CHUNK_WIDTH
                                * WorldSpace.GRID_SIZE * x ),
                                offset.y
                                - WorldSpace.CHUNK_HEIGHT
                                * WorldSpace.GRID_SIZE * y), true));
            }
        }
    }

    public void update(Coords position) throws IOException {
        Coords nearest = WorldSpace.nearestChunkCoordsAt(position);
        if (!nearest.equals(offset)) {
            if (offset.x > nearest.x) {
                this.offset = window.get(0).get(0).getOffset();

                for(int y=0;y<height;y++){
                    window.get(y).remove(height-1);

                    //add to begin
                    window.get(y).add(0, WorldSpace.chunkAt(
                            new Coords(nearest.x, nearest.y
                            - WorldSpace.CHUNK_HEIGHT * 
                                    WorldSpace.GRID_SIZE * y), true));
                                       
                }
                //window.get(0).add(WorldSpace.loadChunkAt(nearest));
                System.out.println("left");

            } else if (offset.x < nearest.x) {
                this.offset = window.get(0).get(0).getOffset();

                for(int y=0;y<height;y++){
                    window.get(y).remove(0);

                    window.get(y).add(WorldSpace.chunkAt(
                            new Coords(nearest.x + WorldSpace.CHUNK_WIDTH *
                                    WorldSpace.GRID_SIZE * (width-1),
                                    nearest.y - WorldSpace.CHUNK_HEIGHT * 
                                            WorldSpace.GRID_SIZE * y), true));
                }
                System.out.println("right");

            }

            if (offset.y > nearest.y  && (offset.y > WorldSpace.CHUNK_HEIGHT
                                    * WorldSpace.GRID_SIZE * -6)) {
                window.remove(0);
                this.offset = window.get(0).get(0).getOffset();

                ArrayList<Chunk>  chunkRow = new ArrayList<Chunk>();

                for (int x = 0; x < width; x++) {
                    chunkRow.add(WorldSpace.chunkAt(
                            new Coords(
                                    offset.x + WorldSpace.CHUNK_WIDTH
                                    * WorldSpace.GRID_SIZE * x,
                                    offset.y
                                    - WorldSpace.CHUNK_HEIGHT
                                    * WorldSpace.GRID_SIZE * height), true));
                }
                window.add(chunkRow);

                System.out.println("down");

            } else if (offset.y < nearest.y) {
                window.remove(height-1);                 
                this.offset = window.get(0).get(0).getOffset();

                ArrayList<Chunk>  chunkRow = new ArrayList<Chunk>();

                for (int x = 0; x < width; x++) {
                    chunkRow.add(WorldSpace.chunkAt(
                            new Coords(
                                    offset.x + WorldSpace.CHUNK_WIDTH
                                    * WorldSpace.GRID_SIZE * x,
                                    offset.y), true));
                }
                window.add(chunkRow);

                System.out.println("up");
            }
        }
    }
}
