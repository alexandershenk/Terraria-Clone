package worldspace;

import com.badlogic.gdx.math.Circle;
import java.util.ArrayList;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class ModifierEvaluator extends Evaluator {
    private modifyType type;
    
    public ModifierEvaluator(){}
        
    public void setModifier(modifyType m){
        this.type= m;
    }  

    public modifyType getModifier() {
        return this.type;
    }   

    @Override
    public void eval(Coords coord) {

        if(type == modifyType.ORGANIC){            
            int circlDiam=144;
            setBoundary(new AmbiguousBoundedRegion(coord, 
                    circlDiam/WorldSpace.GRID_SIZE,circlDiam/WorldSpace.GRID_SIZE));
            
            final Circle circle = new Circle(coord.x+circlDiam/2, coord.y-circlDiam/2, 
                    circlDiam/2);

            condition = new Condition(getEUID(), isBackground()){
                @Override
                public void action(ElementalPoint point){
                    if(point.getEUID() != getEUID()){
                        if(circle.contains(point.offset.x, point.offset.y)){
                            point.setEUID(getEUID());
                        }
                    }
                }              
            };        
            eval(this.boundary, condition);
        }else{
            setBoundary(new AmbiguousBoundedRegion(coord, 1,1));      

            condition = new Condition(getEUID(), isBackground()){
                @Override
                public void action(ElementalPoint point){
                    if(point.getEUID() != getEUID()){
                        point.setEUID(getEUID());                      
                    }
                }              
            };        
            eval(this.boundary, condition);
        }
    }

    @Override
    public void eval(BoundedRegion b) {
    }

    @Override
    public void eval() {
    }    
}
