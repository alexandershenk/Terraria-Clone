package worldspace;

import java.util.UUID;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class LightEvaluator extends Evaluator {
        
    //provide a transform matrix in Eval class for the evaluation inputs
    private UUID UID;
    
    public LightEvaluator() {
        this.UID= UUID.randomUUID();
    }

    @Override
    public void eval(BoundedRegion chunk) {
        setBoundary(chunk);
        
        
    }

    @Override
    public void eval(Coords coord) {
    }

    @Override
    public void eval() {
    }


}
