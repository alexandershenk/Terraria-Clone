package worldspace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author roasstbeef (alex.s)
 */
//@JsonIgnoreProperties(value = { "data" })
public class PolyPointsSet extends PointsSet implements Elemental{
    
    private int gridCase;
    private int sideA, sideB, sideC, sideD;
    private Coords nodes[];
    private int EUID;
    private boolean isBackground;
    
    public PolyPointsSet(){}
    public PolyPointsSet(Coords coords, int gridCase) {
        super(coords);
        nodes = WorldSpace.getSimpleGridCoords(coords);

        setGridCase(gridCase);
    }
    public PolyPointsSet(Coords coords, UUID UID,  int gridCase) {
        super(coords, UID);
        nodes = WorldSpace.getSimpleGridCoords(coords);

        setGridCase(gridCase);
    }
    @Override
    public int getGridCase(){
        return gridCase;
    }
    @Override
    public void setGridCase(int gridCase){
        this.gridCase = gridCase;
        setData(gridCase);
    }    
    public void setData(int gridCase) {
        switch(gridCase){
            case 1:
                setSideA(nodes[0].x+(WorldSpace.GRID_SIZE/2));
                setSideD(nodes[0].y-(WorldSpace.GRID_SIZE/2));
                break;
            case 2:
                setSideA(nodes[1].x-(WorldSpace.GRID_SIZE/2));
                setSideB(nodes[1].y-(WorldSpace.GRID_SIZE/2));                
                break;
            case 3:
                setSideB(nodes[0].y-(WorldSpace.GRID_SIZE/2));
                setSideD(nodes[1].y-(WorldSpace.GRID_SIZE/2));                                
                break;
            case 4:
                setSideB(nodes[2].y+(WorldSpace.GRID_SIZE/2));
                setSideC(nodes[2].x-(WorldSpace.GRID_SIZE/2));                                                
                break;                
            case 5:
                setSideA(nodes[0].x+(WorldSpace.GRID_SIZE/2));
                setSideD(nodes[0].y-(WorldSpace.GRID_SIZE/2));
                
                setSideB(nodes[2].y+(WorldSpace.GRID_SIZE/2));  
                setSideC(nodes[2].x-(WorldSpace.GRID_SIZE/2));                
                break;                                
            case 6:
                setSideA(nodes[0].x+(WorldSpace.GRID_SIZE/2));
                setSideC(nodes[3].x+(WorldSpace.GRID_SIZE/2));                
                break;                                
            case 7:
                setSideD(nodes[0].y-(WorldSpace.GRID_SIZE/2));
                setSideC(nodes[3].x+(WorldSpace.GRID_SIZE/2));               
                break;
            case 8:
                setSideD(nodes[0].y-(WorldSpace.GRID_SIZE/2));
                setSideC(nodes[3].x+(WorldSpace.GRID_SIZE/2));                
                break;
            case 9:
                setSideA(nodes[0].x+(WorldSpace.GRID_SIZE/2));
                setSideC(nodes[3].x+(WorldSpace.GRID_SIZE/2));                                
                break;
            case 10:
                setSideA(nodes[0].x+(WorldSpace.GRID_SIZE/2));
                setSideD(nodes[0].y-(WorldSpace.GRID_SIZE/2));

                setSideB(nodes[2].y+(WorldSpace.GRID_SIZE/2));
                setSideC(nodes[2].x-(WorldSpace.GRID_SIZE/2));                                                                
                break;
            case 11:
                setSideB(nodes[2].y+(WorldSpace.GRID_SIZE/2));
                setSideC(nodes[2].x-(WorldSpace.GRID_SIZE/2));                                                                
                break;
            case 12:
                setSideB(nodes[0].y-(WorldSpace.GRID_SIZE/2));
                setSideD(nodes[1].y-(WorldSpace.GRID_SIZE/2));                                
                break;
            case 13:
                setSideA(nodes[1].x-(WorldSpace.GRID_SIZE/2));
                setSideB(nodes[1].y-(WorldSpace.GRID_SIZE/2));                
                break;
            case 14:
                setSideA(nodes[0].x+(WorldSpace.GRID_SIZE/2));
                setSideD(nodes[0].y-(WorldSpace.GRID_SIZE/2));
                break;
        }
    }

    public int getSideA() {
        return sideA;
    }

    public void setSideA(int sideA) {
        this.sideA = sideA;
        
    }

    public int getSideB() {
        return sideB;
    }

    public void setSideB(int sideB) {
        this.sideB = sideB;
    }

    public int getSideC() {
        return sideC;
    }

    public void setSideC(int sideC) {
        this.sideC = sideC;
    }

    public int getSideD() {
        return sideD;
    }

    public void setSideD(int sideD) {
        this.sideD = sideD;
    }

    @Override
    public void update() {
    }

    @Override
    public void setEUID(int EUID) {
        this.EUID = EUID;
    }

    @Override
    public int getEUID() {
        return this.EUID;
    }

    @Override
    public boolean isBackground() {
        return this.isBackground;
    }

    @Override
    public void isBackground(Boolean isBackground) {
        this.isBackground = isBackground;
    }
}
