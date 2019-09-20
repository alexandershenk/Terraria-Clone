package worldspace;

import java.util.ArrayList;
import java.util.UUID;
import tools.noise.Noise1d;
import tools.noise.Noise2d;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class BaseTerrainEvaluator extends Evaluator {
    
    //evaluation input, how evaluator determines if point is filled or not
    private Noise1d noise = new Noise1d();
    private Noise2d noise2 = new Noise2d();

    //provide a transform matrix in Eval class for the evaluation inputs
    private UUID UID;
    
    public BaseTerrainEvaluator() {
        this.UID= UUID.randomUUID();
    }

    @Override
    public void eval(BoundedRegion chunk) {
        setBoundary(chunk);
        setEUID(1);
        
        isBackground(true);
        condition = new Condition(getEUID(), isBackground()){
            @Override
            public void action(ElementalPoint point){
                if(point.getEUID() != getEUID()){
                    double total = noise.perlinNoise(6, .2, 5, point.offset.x+.01);
                    if(total > point.offset.y)
                        point.setEUID(1);                       
                }
            }              
        };      
        eval(this.boundary, condition);          

        isBackground(false);          
        condition = new Condition(getEUID(), isBackground()){
        @Override
        public void action(ElementalPoint point){
            if(point.getEUID() != getEUID()){
                double total = noise.perlinNoise(6, .2, 5, point.offset.x+.01);
                double res =  
                        (Math.abs(noise2.perlinNoise(6, .25, 5, 
                                (double) (point.offset.x+.01), 
                                (double) (point.offset.y+.01))));
                

                if(total > point.offset.y && res > 50)
                    point.setEUID(1);                       
            }
        }              
        };        
        eval(this.boundary, condition);      


    }

    @Override
    public void eval(Coords coord) {
    }

    @Override
    public void eval() {
    }


}
