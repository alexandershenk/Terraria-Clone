package worldspace;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class ElementNames {
    public static Map<Integer,String> data = new HashMap();
    static {
        data.put(-1, "NULL_ELEM");
        data.put(0, "stone");
        data.put(1, "dirt");
        //data.put(2, "stone_wall");
        //data.put(2, "brick");
        data.put(2, "leaf");
        data.put(3, "tree_bark");

    }
}
