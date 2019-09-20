package worldspace;

/**
 *
 * @author roasstbeef (alex.s)
 */
public abstract class WorldSpaceObject {
        public Coords offset;
        
        public WorldSpaceObject(Coords coords){
            offset = WorldSpace.nearestGridCoordsAt(coords);
        }
        public WorldSpaceObject(){
            offset = new Coords(0,0);
        }
        
        public Coords getOffset(){
            return offset;
        }
        public void setOffset(Coords offset){
            this.offset = offset;
        }            

}
