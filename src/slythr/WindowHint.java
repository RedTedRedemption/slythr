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
    public static WindowHint<Boolean> windowHint_redraw;
    public static WindowHint<Color> windowHint_clear_color;
    public static WindowHint<Integer> windowHint_redraw_delay;
    public static WindowHint<Integer> windowHint_periodic_delay;

    public static void init() {
        windowHints = new ArrayList<>();

        windowHint_periodic_delay = new WindowHint<>(Engine.WINDOW_HINT_PERIODIC_DELAY, null, 12);

        windowHint_redraw = new WindowHint<>(Engine.WINDOW_HINT_REDRAW, new SlythrAction() {
            @Override
            public void execute() {
                Game_Window.redraw = windowHint_redraw.value;
            }

            @Override
            public void execute2() {

            }
        }, true);
        windowHint_clear_color = new WindowHint<>(Engine.WINDOW_HINT_CLEAR_COLOR, new SlythrAction() {
            @Override
            public void execute() {
                Game_Window.redrawColor = windowHint_clear_color.value;
            }

            @Override
            public void execute2() {

            }
        }, Color.blue);

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
        onChangeAction.execute();
    }

    private void onChange() {
        if (onChangeAction != null) {
            onChangeAction.execute();
        } else {
            //pass;
        }
    }

    public valueType getValue() {
        return value;
    }



}
