package worldspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author roasstbeef (alex.s)
 */
public abstract class PointsSet extends WorldSpaceObject{
    protected UUID UID;
    private ArrayList<ElementalPoint> data = 
            new ArrayList<ElementalPoint>();

    public PointsSet(){}
    public PointsSet(Coords coords) {
        super(coords);
        this.UID  = UUID.randomUUID();

        //initPointsSet();
    }    
    public PointsSet(Coords coords, UUID UID) {
        super(coords);
        this.UID  = UID;

        //initPointsSet();
    }
    public abstract void update();
    public abstract int getGridCase();
    public abstract void setGridCase(int gridCase);
    
    public UUID getUID() {
        return UID;
    }

    public void setUID(UUID UID) {
        this.UID = UID;
    }

    public ArrayList<ElementalPoint> getData() {
        return data;
    }

    public void setData(ArrayList<ElementalPoint> data) {
        this.data = data;
    }
    

    
    
    
}
