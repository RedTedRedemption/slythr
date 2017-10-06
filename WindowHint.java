package slythr;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by teddy on 10/3/17.
 */
public class WindowHint {

    public static ArrayList<WindowHint> windowHints;

    public int tag;
    public Object value;

    //window hints
    static WindowHint windowHint_redraw;
    static WindowHint windowHint_clear_color;

    public static void init() {
        windowHints = new ArrayList<>();
        windowHint_redraw = new WindowHint(Engine.WINDOW_HINT_REDRAW, true);
        windowHint_clear_color = new WindowHint(Engine.WINDOW_HINT_CLEAR_COLOR, Color.blue);
    }

    public WindowHint(int Tag, Object initialValue) {

        tag = Tag;
        value = initialValue;
        windowHints.add(this);

    }

    public void setValue(Object Value) {
        value = Value;
    }



}
