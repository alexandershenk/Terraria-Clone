package tools.misc;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class Holder <T>{
    private T value;

    public Holder(T value) {
        setValue(value);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}