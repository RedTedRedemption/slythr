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


    public static int height;

    public static int width;

    public static JFrame frame = new JFrame("Slythr Game");

    public static Stack rendStack = new Stack();

    public static int loop_delay = 6;
    private static Game_loop local_game_loop;

    public static boolean running = true;
    private static boolean ready = false;

    public static int fps_count;

    public static int fps;

    //WindowHint tags

    public static final int WINDOW_HINT_REDRAW = 1;
    public static final int WINDOW_HINT_CLEAR_COLOR = 2;

    public static boolean drawfps = false;
    private static ArrayList<Game_Window> game_windows;
    private static Thread splashThread;
    private static ArrayList<ConsoleCommand> consoleCommands;
    public static String[] previous_commands = {"", "", "", ""};
    public static Animation_Buffer animation_buffer;


    /**
     * Initialize the engine and create a window.
     *
     * @param Height the height of the window
     * @param Width  the width of the window
     */
    public static void launch(int Height, int Width){
        splashStatus("Initializing Engine...");
        splashThread = new Thread(new SplashThread());
        System.out.println("Showing splash");
        splashThread.start();
        Evar.init();
        WindowHint.init();

        //initializing variables
        splashStatus("Initializing Variables...");
        game_windows = new ArrayList<>();
        consoleCommands = new ArrayList<>();
        animation_buffer = new Animation_Buffer();
        rendStack = new Stack();

        width = Width;
        height = Height;






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

        splashStatus("starting background threads");
        animation_thread.start();

        fps_thread.start();

        engine_thread.start();

        splashStatus("render thread started");

        splashStatus("Engine launch done");




    }

    private static void engine_run(){
        frame.setTitle("Slythr");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(false);
        frame.pack();
        frame.setResizable(false);
        frame.setSize(width, height);
        while (!ready){
            //wait for the splash to end and all initial setup to be complete - requires game-side method joinSplash() to be run
        }
        frame.setVisible(true);



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
        });
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
        SplashScreen.setStatus(text);
        System.out.println(text);

    }

    /**
     * Allow the splash to end, and the main engine window to appear. Call this after you have completed all initial setup
     * that you would like to run before the main window appears.
     */
    public static boolean joinSplash(){
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
        }).start();
    }

    /**
     * Add a subroutine that will run once every 12 seconds.
     *
     * @param subRoutine the subroutine
     */
    public static void addSubRoutine(SubRoutine subRoutine){
        new Timer(12, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subRoutine.routine();
            }
        }).start();
    }

    public static void addSubRoutine(int delay, SubRoutine subRoutine){
        new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subRoutine.routine();
            }
        }).start();
    }

    /**
     * Create a new thread and run the action given.
     *
     * @param action the action
     */
    public static void runAction(Runnable action){
        new Thread(action).start();
    }

    public static void addConsoleCommand(String command, ConsoleOperation operation) {
        consoleCommands.add(new ConsoleCommand(command, operation));
    }

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

    public static void console_print(String s) {
        System.out.println(s);
        previous_commands[3] = previous_commands[2];
        previous_commands[2] = previous_commands[1];
        previous_commands[1] = previous_commands[0];
        previous_commands[0] = s;
    }

    public static void setWindow_hint(int tag, Object value) {
        for (WindowHint windowHint : WindowHint.windowHints) {
            if (windowHint.tag == tag) {
                windowHint.setValue(value);
            }
        }
    }

    public static void throwFatalError(SlythrError error) {
        JOptionPane.showMessageDialog(frame, error.getMessage() + "\n" + error.toString(), "Slythr Error", JOptionPane.ERROR_MESSAGE);
        error.printStackTrace();
        System.exit(1);
    }

    public static Object getWindow_hint(int Tag) {
        for (WindowHint windowHint : WindowHint.windowHints) {
            if (windowHint.tag == Tag) {
                return windowHint.value;
            }
        }
        try {
            throw new SlythrError("SLYTHR ERROR: INVALID WINDOW_HINT TAG" + Integer.toString(Tag));
        } catch (SlythrError slythrError) {
            Engine.throwFatalError(slythrError);
        }
        return null;

    }


}
