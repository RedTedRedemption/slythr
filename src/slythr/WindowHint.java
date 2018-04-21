package slythr;

import java.awt.*;
import java.util.ArrayList;

/**
 * Various settings and information about the engine. These can be changed by the user.
 */
public class WindowHint<valueType> {

    public static ArrayList<WindowHint> windowHints = new ArrayList<>();

    public int tag;
    private valueType value;
    private SlythrAction onChangeAction;
    private static boolean initialized = false;

    //window hints
    public static WindowHint<Boolean> windowHint_redraw;

    public static WindowHint<String> windowHint_windowTitle = new WindowHint<>(Engine.WINDOW_HINT_WINDOW_TITLE, new SlythrAction() {
        @Override
        public void execute() {
            Engine.frame.setTitle(windowHint_windowTitle.getValue());
        }

        @Override
        public void execute2() {

        }
    }, "Slythr");

    public static WindowHint<Color> windowHint_clear_color = new WindowHint<>(Engine.WINDOW_HINT_CLEAR_COLOR, new SlythrAction() {
        @Override
        public void execute() {
            Game_Window.redrawColor = windowHint_clear_color.value;
        }

        @Override
        public void execute2() {

        }
    }, Color.blue);
    public static WindowHint<Integer> windowHint_redraw_delay;
    public static WindowHint<Integer> windowHint_periodic_delay;
    public static WindowHint<Integer> windowHint_TaskManager_thread_count = new WindowHint<Integer>(Engine.WINDOW_HINT_TASKMANAGER_COUNT, new SlythrAction() {
        @Override
        public void execute() {
            if (Engine.initialized) {
                System.out.println("ERROR: TaskManager thread count cannot be changed after engine launch");
            }
        }

        @Override
        public void execute2() {

        }
    }, EngineSettings.DEFAULT_THREAD_COUNT);

    public static WindowHint<Boolean> windowHint_disable_GLRendering = new WindowHint<>(Engine.WINDOW_HINT_DISABLE_GLRENDERING, new SlythrAction() {
        @Override
        public void execute() {
            if (Engine.initialized) {
                Engine.throwFatalError(new SlythrError("ERROR: Cannot change windowHint_disable_GLRendering after engine launch"));
            }
        }

        @Override
        public void execute2() {

        }
    }, false);
    public static WindowHint<Boolean> windowHint_Allow_Resize = new WindowHint<>(Engine.WINDOW_HINT_ALLOW_RESIZE, new SlythrAction() {
        @Override
        public void execute() {
            if (Engine.initialized) {
                Engine.throwFatalError(new SlythrError("ERROR: Cannot change windowHint_Allow_Resize after engine launch"));
            }
        }

        @Override
        public void execute2() {

        }
    }, false);

    public static void init() {
        System.out.println("===========================");
        System.out.print("Initializing window hints...");


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

        initialized = true;
        System.out.println("done");
        System.out.println("===========================");
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
        if (!initialized) {
            init();
        }
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
        if (!initialized) {
            init();
        }
        return value;
    }





}
