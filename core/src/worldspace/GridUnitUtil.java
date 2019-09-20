package worldspace;

import java.util.ArrayList;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class GridUnitUtil {
        private int bgSize=0;

        public void add(ArrayList<EvalPointsSet> evalPointsSets, EvalPointsSet pointsSet){
        int currIdx;
        int end;
        if(pointsSet.isBackground()){
            currIdx=0;
            end = bgSize;
            
        }
        else{
            currIdx=bgSize;
            end = evalPointsSets.size();
        }
        
        if(evalPointsSets.isEmpty() && pointsSet.isBackground()){
            evalPointsSets.add(currIdx, pointsSet);
            bgSize++;

        }
        else if(evalPointsSets.size() == currIdx && !pointsSet.isBackground()){
            evalPointsSets.add(currIdx, pointsSet);
            
        }
        else{
            boolean isEclipsed=false;
            for(int i=currIdx; i<end; i++){
                if(evalPointsSets.get(i).getGridCase() == 15){
                    isEclipsed=true;     
                    break;
                }
            }
            if(!isEclipsed){
                if(pointsSet.isBackground())
                    bgSize++;
                evalPointsSets.add(currIdx, pointsSet);
                
                EvalPointsSet compare = null;
                if(evalPointsSets.get(currIdx+1).isBackground() == pointsSet.isBackground() &&
                        (evalPointsSets.get(currIdx+1).getEUID() == pointsSet.getEUID() ||
                        evalPointsSets.get(currIdx+1).getEUID() == -1) && currIdx+1 < end){
                    compare = evalPointsSets.get(currIdx+1);

                }else if(currIdx > 0 && evalPointsSets.get(currIdx-1).isBackground() == pointsSet.isBackground() &&
                         (evalPointsSets.get(currIdx - 1).getEUID() == pointsSet.getEUID() ||
                    evalPointsSets.get(currIdx-1).getEUID() == -1)){
                    compare = evalPointsSets.get(currIdx - 1);           
                }
                if(compare !=null){
                    for(int i =0; i<4; i++){
                        if(pointsSet.getData().get(i).getEUID() != -1 ||
                                compare.getData().get(i).getEUID() != -1){
                            pointsSet.getData().get(i).setEUID(
                                   pointsSet.getEUID()
                            );
                        }
                    }
                    evalPointsSets.remove(compare);
                    if(pointsSet.isBackground())
                        bgSize--;

                }     
                
            }
        }

    }

}
