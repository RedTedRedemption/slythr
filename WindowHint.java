package slythr;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by teddy on 10/3/17.
 */
public class WindowHint<valueType> {

    public static ArrayList<WindowHint> windowHints;

    public int tag;
    public valueType value;
    private SlythrAction onChangeAction;

    //window hints
    static WindowHint<Boolean> windowHint_redraw;
    static WindowHint<Color> windowHint_clear_color;
    static WindowHint<Integer> windowHint_redraw_delay;

    public static void init() {
        windowHints = new ArrayList<>();
        windowHint_redraw = new WindowHint<>(Engine.WINDOW_HINT_REDRAW, null, true);
        windowHint_clear_color = new WindowHint<>(Engine.WINDOW_HINT_CLEAR_COLOR, null, Color.blue);

        windowHint_redraw_delay = new WindowHint<>(Engine.WINDOW_HINT_REDRAW_DELAY, new SlythrAction() {
            @Override
            public void execute() {
                for (Game_Window window : Engine.game_windows) {
                    window.repaint_timer.setDelay(windowHint_redraw_delay.value);
                }
            }

            @Override
            public void execute2() {

            }
        }, 6);
    }

    public WindowHint(int Tag, SlythrAction onChange, valueType initialValue) {


        tag = Tag;
        value = initialValue;
        if (onChange == null) {
            onChangeAction = new SlythrAction() {
                @Override
                public void execute() {

                }

                @Override
                public void execute2() {

                }
            };
        } else {
            onChangeAction = onChange;
        }
        windowHints.add(this);

    }

    public void setValue(valueType Value) {
        value = Value;
    }

    private void onChange() {
        onChangeAction.execute();
    }

    public valueType getValue() {
        return value;
    }



}
