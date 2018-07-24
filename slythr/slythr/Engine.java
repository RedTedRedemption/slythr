package slythr;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Main engine class. This should be initialized first, and will create a new window, and runs all setup that needs to be performed before the
 * engine can function properly.
 */
public class Engine {

    private static boolean do_log = false;


    /**
     * The constant height.
     */
    public static int height;

    /**
     * The Offset.
     */
    public static int[] offset = {0, 20};

    /**
     * The constant width.
     */
    public static int width;

    /**
     * The constant frame.
     */
    public static JFrame frame = new JFrame("Slythr Game");

    /**
     * The constant rendStack.
     */
    public static Stack rendStack = new Stack();

    /**
     * The constant loop_delay.
     */
    public static int loop_delay = 6;
    private static Game_loop local_game_loop;

    /**
     * The constant running.
     */
    public static boolean running = true;
    private static boolean ready = false;
    public static boolean initialized = false;

    /**
     * The constant fps_count.
     */
    public static int fps_count;

    /**
     * The constant fps.
     */
    public static int fps;

    //WindowHint tags

    /**
     * Tag ID for the redraw window hint.
     */
    public static final int WINDOW_HINT_REDRAW = 0;
    /**
     * Tag ID for clear color window hint.
     */
    public static final int WINDOW_HINT_CLEAR_COLOR = 1;
    /**
     * Tag ID for redraw delay window hint.
     */
    public static final int WINDOW_HINT_REDRAW_DELAY = 2;
    /**
     * Tag ID for periodic delay window hint.
     */
    public static final int WINDOW_HINT_PERIODIC_DELAY = 3;
    /**
     * Tag ID for TaskManager thread pool size
     */
    public static final int WINDOW_HINT_TASKMANAGER_COUNT = 4;
    /**
     * Tag ID for GLRendering disable
     */
    public static final int WINDOW_HINT_DISABLE_GLRENDERING = 5;
    /**
     * Tag ID for Window Title windowHint
     */
    public static final int WINDOW_HINT_WINDOW_TITLE = 6;
    /**
     * Tag ID for Window resizing windowHint
     */
    public static final int WINDOW_HINT_ALLOW_RESIZE = 7;
    /**
     * Boolean var indicating whether to continually redraw the window.
     */
    public static boolean drawfps = false;
    /**
     * An ArrayList of all running game windows
     */
    public static ArrayList<Game_Window> game_windows;
    private static Thread splashThread;
    private static ArrayList<ConsoleCommand> consoleCommands;

    /**
     * The Previous commands.
     */
    static String[] previous_commands = {"", "", "", ""};
    /**
     * The constant animation_buffer.
     */
    public static Animation_Buffer animation_buffer;

    private static Thread gl_renderThread;

    public static Stack notifyStack_Key;

    public static Shader plainVertexShader;
    public static Shader plainGeometryShader;
    public static Shader plainFragmentShader;


    /**
     * Initialize the engine and create a window.
     *
     * @param Height the height of the window
     * @param Width  the width of the window
     */
    public static void launch(int Height, int Width){
        try {
            splashStatus("Initializing Engine...");
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            }));
            do_log = true;
            System.out.println("logging active");
            Logger.init();
            notifyStack_Key = new Stack();

            width = Width;
            height = Height;

            splashThread = new Thread(new SplashThread());
            System.out.println("Showing splash");
            splashThread.start();

            splashStatus("INITIALIZING ENGINE COMPONENTS");
            Evar.init();
            WindowHint.init();
            TaskManager.init();
            if (!WindowHint.windowHint_disable_GLRendering.getValue()) {
                System.out.println("GL Rendering enabled");
                Render.init();
            }
            System.out.println("===========================");

            if (EngineSettings.THREADED_RENDERING) {
                System.out.println("THREADED RENDERING ACTIVE");
                System.out.print("Initializing ");
                System.out.print(EngineSettings.RENDER_THREADS);
                System.out.print(" render threads...");
                SKernel.init();
                System.out.println("rendering threads setup complete");
                System.out.println("===========================");
            }

            //initializing variables
            System.out.print("Initializing stacks and arrayLists...");
            game_windows = new ArrayList<>();
            consoleCommands = new ArrayList<>();
            animation_buffer = new Animation_Buffer();
            rendStack = new Stack();
            System.out.println("done");


            splashStatus("binding default animation buffer");
            Animation.bind_default_animation_buffer(animation_buffer);

            splashStatus("adding default console commands");
            addConsoleCommand("help", new ConsoleOperation() {
                @Override
                public void operation(String args) {
                    String stout = "";
                    for (ConsoleCommand command : consoleCommands) {
                        stout = stout + command.call_command + ", ";
                    }
                    console_print(stout);
                }
            });

            Utils.println_Normal("Initializing plain shaders");
            plainVertexShader = new plainVertexShader();
            plainGeometryShader = new plainGeometryShader();
            plainFragmentShader = new plainFragmentShader();


            Engine.addConsoleCommand("showfps", new ConsoleOperation() {
                @Override
                public void operation(String args) {
                    Engine.drawfps();
                }
            });
            Engine.addConsoleCommand("hidefps", new ConsoleOperation() {
                @Override
                public void operation(String args) {
                    Engine.stop_drawfps();
                }
            });

            Engine.addConsoleCommand("exit", new ConsoleOperation() {
                @Override
                public void operation(String args) {
                    console_print("Exiting by console request");
                    System.exit(0);
                }
            });

            splashStatus("creating threads");

            if (EngineSettings.HARDWARE_ACCELERATION) {
                Utils.println_Verbose("Creating hardware accelerated render thread");
                gl_renderThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Render.hardwareAcceleratedRend();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                if (EngineSettings.THREADED_RENDERING) {
                    Utils.println_Verbose("Creating threaded rendering thread");
                    gl_renderThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Render.threadRend();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    Utils.println_Verbose("Creating simple render thread");
                    gl_renderThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Render.render();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
            Utils.print_Verbose("Creating FPS counter thread...");
            Thread fps_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Timer fps_timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fps = fps_count;
                            fps_count = 0;
                        }
                    });
                    fps_timer.start();
                }
            }, "fps counter thread");


            Utils.print_Verbose("Creating animation thread...");
            Thread animation_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Timer animation_timer = new Timer(12, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            animation_buffer.step();
                        }
                    });

                    animation_timer.start();
                }
            }, "animation thread");
            Utils.print_Verbose("done");

            Thread engine_thread;
            if (WindowHint.windowHint_Allow_Resize.getValue()) {
                engine_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("waiting for splash to end...");
                        while (!ready) {
                            //wait for splash to end
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Engine.height = frame.getHeight();
                            Engine.width = frame.getWidth();

                            //there has to be a Thread.sleep() here or it doesn't work for whatever reason. No matter, doesnt affect performance overall.
                        }
                        System.out.println("done");
                        engine_run();
                    }
                }, "SLYTHR engine thread");
            } else {

                engine_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("waiting for splash to end...");
                        while (!ready) {
                            //wait for splash to end
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            //there has to be a Thread.sleep() here or it doesn't work for whatever reason. No matter, doesnt affect performance overall.
                        }
                        System.out.println("done");
                        engine_run();
                    }
                }, "SLYTHR engine thread");
            }

            splashStatus("starting background threads");
            animation_thread.start();

            fps_thread.start();

            engine_thread.start();

            splashStatus("render thread started");

            splashStatus("Engine launch done");
        } catch (Exception e) {
            throwFatalError(e);
        }

        initialized = true;

        //END ENGINE SETUP




    }
    public static void launch(int Height, int Width, String[] args){
        try {

            for (String arg : args) {
                if (arg.equals("-nolog")) {
                    break;
                }
                do_log = true;
                System.out.println("logging active");
                Logger.init();
                break;
            }
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            }));
            Evar.init(args);
            Utils.println_Normal("Initializing engine with arguments...");
            notifyStack_Key = new Stack();

            width = Width;
            height = Height;

            splashThread = new Thread(new SplashThread());
            Utils.println_Normal("Showing splash");
            splashThread.start();

            splashStatus("INITIALIZING ENGINE COMPONENTS");

            WindowHint.init();
            TaskManager.init();
            if (!WindowHint.windowHint_disable_GLRendering.getValue()) {
                Utils.println_Normal("GL Rendering enabled");
                Render.init();
            }

            Utils.println_Normal("===========================");

            if (EngineSettings.THREADED_RENDERING) {
                Utils.println_Normal("THREADED RENDERING ACTIVE");
                Utils.print_Normal("Initializing ");
                Utils.print_Normal(Integer.toString(EngineSettings.RENDER_THREADS));
                Utils.print_Normal(" render threads...");
                SKernel.init();
                Utils.println_Normal("rendering threads setup complete");
                Utils.println_Normal("===========================");
            }

            //initializing variables
            System.out.print("Initializing stacks and arrayLists...");
            Utils.print_Normal("Initializign stacks and arrayLists...");
            game_windows = new ArrayList<>();
            consoleCommands = new ArrayList<>();
            animation_buffer = new Animation_Buffer();
            rendStack = new Stack();
            Utils.println_Normal("done");


            splashStatus("binding default animation buffer");
            Animation.bind_default_animation_buffer(animation_buffer);

            splashStatus("adding default console commands");
            addConsoleCommand("help", new ConsoleOperation() {
                @Override
                public void operation(String args) {
                    String stout = "";
                    for (ConsoleCommand command : consoleCommands) {
                        stout = stout + command.call_command + ", ";
                    }
                    console_print(stout);
                }
            });

            Engine.addConsoleCommand("showfps", new ConsoleOperation() {
                @Override
                public void operation(String args) {
                    Engine.drawfps();
                }
            });
            Engine.addConsoleCommand("hidefps", new ConsoleOperation() {
                @Override
                public void operation(String args) {
                    Engine.stop_drawfps();
                }
            });

            Engine.addConsoleCommand("exit", new ConsoleOperation() {
                @Override
                public void operation(String args) {
                    console_print("Exiting by console request");
                    System.exit(0);
                }
            });

            splashStatus("creating threads");

            if (EngineSettings.HARDWARE_ACCELERATION) {
                gl_renderThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Render.hardwareAcceleratedRend();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                if (EngineSettings.THREADED_RENDERING) {
                    gl_renderThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Render.threadRend();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    gl_renderThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Render.render();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }

            Thread fps_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Timer fps_timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fps = fps_count;
                            fps_count = 0;
                        }
                    });
                    fps_timer.start();
                }
            }, "fps counter thread");


            Thread animation_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Timer animation_timer = new Timer(12, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            animation_buffer.step();
                        }
                    });

                    animation_timer.start();
                }
            }, "animation thread");

            Thread engine_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Utils.println_Normal("waiting for splash to end...");
                    while (!ready) {
                        //wait for splash to end
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //there has to be a Thread.sleep() here or it doesn't work for whatever reason. No matter, doesnt affect performance overall.
                    }
                    Utils.println_Normal("done");
                    engine_run();
                }
            }, "SLYTHR engine thread");

            splashStatus("starting background threads");
            animation_thread.start();

            fps_thread.start();

            engine_thread.start();

            splashStatus("render thread started");

            splashStatus("Engine launch done");
        } catch (Exception e) {
            throwFatalError(e);
        }

        initialized = true;

        //END ENGINE SETUP



    }

    private static void engine_run(){
        frame.setTitle(WindowHint.windowHint_windowTitle.getValue());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(false);
        frame.pack();
        frame.setResizable(WindowHint.windowHint_Allow_Resize.getValue());
        frame.setSize(width, height);
        while (!ready){
            //wait for the splash to end and all initial setup to be complete - requires game-side method joinSplash() to be run
        }
        frame.setVisible(true);
        gl_renderThread.start();
//        offset[0] = width - frame.getContentPane().getSize().width - 5;
//        System.out.println(offset[0]);
//        offset[1] = height - frame.getContentPane().getSize().height - 10;
//        System.out.println(offset[1]);
//        frame.getContentPane().setSize(height, width);
//        frame.setSize(width + offset[0] + 5, height + offset[1] + 10);
//        System.out.println(frame.getContentPane().getSize());
//        System.out.println(frame.getSize());



        //the engine thread has now handed off action to the game window, which controls rendering for that window.
    }

    /**
     * Create a game window to add to the engine session. The new window is returned to allow it to be manipulated.
     *
     * @return the new game window
     */
    public static Game_Window addWindow(){
        Game_Window new_window = new Game_Window(frame);
        frame.setContentPane(new_window);
        game_windows.add(new_window);
        return new_window;
    }

    /**
     * Add game loop. The code in the method periodic passed to this method will be run periodically in its own thread.
     *
     * @param loop the loop
     * @throws InterruptedException caused by a failure when a Thread.sleep() method is called.
     */
    public static void addGameLoop(Game_loop loop) throws InterruptedException {
        local_game_loop = loop;
        Thread game_loop_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running) {
                    local_game_loop.periodic();
                    try {
                        Thread.sleep(6);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "SLYTHR Game Loop");
        game_loop_thread.start();

    }

    /**
     * Activate the FPS counter in the upper right corner of the window.
     */
    public static void drawfps(){
        for (Game_Window window : game_windows) {
            window.fps_readout.enable();
        }
    }

    /**
     * Deactivate the FPS counter in the upper right corner of the window.
     */
    public static void stop_drawfps(){
        for (Game_Window window : game_windows) {
            window.fps_readout.disable();
        }
    }

    /**
     * Set loading status. This is displayed to the bottom left of the splash screen, and is printed to the console.
     *
     * @param text the text to display and print
     */
    public static void splashStatus(String text){
        Utils.println_Normal(text);
        SplashScreen.setStatus(text);

    }

    /**
     * Allow the splash to end, and the main engine window to appear. Call this after you have completed all initial setup
     * that you would like to run before the main window appears.
     *
     * @return the boolean
     */
    public static boolean joinSplash(){
        if (game_windows.size() == 0) {
            System.out.println("WARNING: no game windows have been initialized");
        }
        SplashScreen.allowEnd();
        try {
            splashThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ready = true;
        return true;
    }

    /**
     * Add event to be run when trigger() returns true. The trigger() method is called once every 12 milliseconds.
     *
     * @param event the event to be run, where if trigger() returns true, action() will be called.
     */
    public static void addEvent(SlythrEvent event){
        new Timer(12, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (event.trigger()){
                    event.action();
                }
            }
        }).start();
    }

    /**
     * Add event.
     *
     * @param delay the delay
     * @param event the event
     */
    public static void addEvent(int delay, SlythrEvent event) {
        new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (event.trigger()) {
                    event.action();
                }
            }
        }).start();
    }

    /**
     * Add task to be run once when trigger() returns true.
     *
     * @param task the task
     */
    public static void addTask(SlythrEvent task){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!task.trigger()){
                    //pass;
                    try {
                        Thread.sleep(12);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                task.action();
            }
        }, "SLYTHR task thread").start();
    }

    /**
     * Add a subroutine that will run once every 12 seconds.
     *
     * @param subRoutine the subroutine
     */
    public static void addSubRoutine(SubRoutine subRoutine){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        subRoutine.routine();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(12);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "SLYTHR subroutine").start();
    }

//    public static void addSubRoutine(int delay, SubRoutine subRoutine){
//        new Timer(delay, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                subRoutine.routine();
//            }
//        }).start();
//    }

    /**
     * Create a new thread and run the action given.
     *
     * @param action the action
     */
    public static void runAction(Runnable action){
        new Thread(action).start();
    }

    /**
     * Add console command.
     *
     * @param command   the command
     * @param operation the operation
     */
    public static void addConsoleCommand(String command, ConsoleOperation operation) {
        consoleCommands.add(new ConsoleCommand(command, operation));
    }

    /**
     * Console command.
     *
     * @param console_line the console line
     */
    public static void console_command(String console_line) {
        int timesfound = 0;
        if (console_line.equals("")) {
            console_print("");
        }
        for (ConsoleCommand command : consoleCommands) {
            if (console_line.startsWith(command.call_command)) {
                command.operate(console_line);
                timesfound = timesfound + 1;
            }
        }
        if (timesfound == 0) {
            console_line = "Error command '" + console_line + "' not found";
        }

        console_print(console_line);



    }

    /**
     * Console print.
     *
     * @param s the s
     */
    public static void console_print(String s) {
        System.out.println(s);
        previous_commands[3] = previous_commands[2];
        previous_commands[2] = previous_commands[1];
        previous_commands[1] = previous_commands[0];
        previous_commands[0] = s;
    }

    /**
     * Sets window hint.
     *
     * @param tag   the tag
     * @param value the value
     */
    public static void setWindow_hint(int tag, Object value) {
        for (WindowHint windowHint : WindowHint.windowHints) {
            if (windowHint.tag == tag) {
                windowHint.setValue(value);
            }
        }
    }

    /**
     * Throw fatal error.
     *
     * @param error the error
     */
    public static void throwFatalError(Exception error) {
        String stackTrace = new String();
        Logger.log("throwing fatal error: " + error.getMessage());
        for (StackTraceElement element : error.getStackTrace()) {
            stackTrace = stackTrace + "    " + element.toString() + "\n";
            Logger.log(element.toString());
        }
        JOptionPane.showMessageDialog(frame, "SLYTHR has encountered a fatal error and must shut down! \n \n " + error.getMessage() + "\n \n" + error.toString() + "\n" + stackTrace, "Slythr Has Encountered a Fatal Error", JOptionPane.ERROR_MESSAGE);
        error.printStackTrace();
        System.exit(1);
    }

    public static void throwFatalError(String s) {
        String stackTrace = new String();
        SlythrError error = new SlythrError(s);
        for (StackTraceElement element : error.getStackTrace()) {
            stackTrace = stackTrace + "    " + element.toString() + "\n";
        }
        JOptionPane.showMessageDialog(frame, "SLYTHR has encountered a fatal error and must shut down! \n \n " + error.getMessage() + "\n \n" + error.toString() + "\n" + stackTrace, "Slythr Has Encountered a Fatal Error", JOptionPane.ERROR_MESSAGE);
        Logger.log("Throwing fatal error: SLYTHR has encountered a fatal error and must shut down! \n \n " + error.getMessage() + "\n \n" + error.toString() + "\n" + stackTrace);
        error.printStackTrace();
        System.exit(1);
    }

    /**
     * Gets window hint.
     *
     * @param Tag the tag
     * @return the window hint
     */
    public static Object getWindow_hint(int Tag) {
        for (WindowHint windowHint : WindowHint.windowHints) {
            if (windowHint.tag == Tag) {
                return windowHint.getValue();
            }
        }
        try {
            throw new SlythrError("SLYTHR ERROR: INVALID WINDOW_HINT TAG" + Integer.toString(Tag));
        } catch (SlythrError slythrError) {
            Engine.throwFatalError(slythrError);
        }
        return null;

    }

    private static void close() {
        Utils.println_Normal("Slythr shutting down");
        if (do_log) {
            Logger.close();
        }
    }


}
