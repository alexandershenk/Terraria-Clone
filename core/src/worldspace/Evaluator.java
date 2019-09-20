package worldspace;

import java.util.ArrayList;
import tools.misc.Holder;

/**
 *
 * @author roasstbeef (alex.s)
 * @param <B>
 */
public abstract class Evaluator  <B extends BoundedRegion>  implements Elemental{
    protected B boundary;
    protected final Holder fillVal = new Holder(-1);
    protected final Holder isBackground = new Holder(false);
    
    protected class Condition {
        public int fill;
        public boolean isBackground;

        private GridUnit currGridUnit;
        
        Condition(int fill, boolean isBackground){
            this.fill=fill;
            this.isBackground = isBackground;
        }
        Condition(int fill, boolean isBackground, GridUnit unit){
            this(fill, isBackground);
            this.currGridUnit = unit;
        }
        Condition(){}

        public void action(ElementalPoint point){
            
        }
        public void setCurrGridUnit(GridUnit unit){
            currGridUnit = unit;
        }
    }
    protected Condition condition;
    
    @Override
    public int getEUID() {
        return (Integer) fillVal.getValue();
    }
    @Override
    public void setEUID(int val){
        fillVal.setValue(val);
    }
    @Override
    public boolean isBackground() {
        return (Boolean) (isBackground.getValue());
    }
    @Override
    public void isBackground(Boolean isBackground){
        this.isBackground.setValue(isBackground);
    }  
    public abstract void eval(Coords coord);
    public abstract void eval();
    public abstract void eval(B b);
    public void eval(B b, Condition condition){
        for(int y = 0; y < boundary.getHeight(); y++){
            for(int x = 0; x < boundary.getWidth(); x++){                  
                ArrayList<EvalPointsSet> data =
                        boundary.getGrid()[y][x].getData();
                ArrayList<EvalPointsSet> toModify = 
                        new ArrayList<EvalPointsSet>();
                             
                condition.setCurrGridUnit(boundary.getGrid()[y][x]);
                if(getEUID() == -1){      
                    toModify = data;
                }
                else{
                    EvalPointsSet set = new EvalPointsSet(
                            boundary.getGrid()[y][x].getOffset());
                    set.setEUID(getEUID());      
                    set.isBackground(isBackground());
                    toModify.add(set);
                                    
                }
                for(EvalPointsSet set : toModify){
                    
                    for (ElementalPoint point : set.getData()) {
                        if(toModify.equals(data) && set.isBackground() !=
                                isBackground()){
                            continue;
                        }
                        condition.action( point);
                    }
                    set.update();
                }
                if(!toModify.equals(data)){
                    for(EvalPointsSet set: toModify){
                        boundary.getGrid()[y][x].add(set);
                        set.update();
                    }
                }

            }        
        }
    }
    public Evaluator(){
    }      


    public void setBoundary(B boundary){
        this.boundary=boundary;
    }     
    
}
