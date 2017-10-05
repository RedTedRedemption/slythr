package slythr;

import java.util.ArrayList;

/**
 * Created by teddy on 10/3/17.
 */
public class WindowHint {

    public static ArrayList<WindowHint> windowHints = new ArrayList<>();

    public int tag;
    public Object value;

    //window hints
    static WindowHint windowHint_redraw = new WindowHint(Engine.WINDOW_HINT_REDRAW, true);

    public WindowHint(int Tag, Object initialValue) {

        tag = Tag;
        value = initialValue;
        windowHints.add(this);

    }

    public void setValue(Object Value) {
        value = Value;
    }

}
