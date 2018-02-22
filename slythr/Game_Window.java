package slythr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * A surface that takes information and can be drawn to.
 */
public class Game_Window extends JPanel {

    /**
     * Timer for repainting the window.
     */
    Timer repaint_timer;
    /**
     * Time to wait between redraws.
     */
    public int draw_wait = 6;
    /**
     * FPS readout text primitive.
     */
    public Primitive fps_readout;
    private Graphics local_g;
    private Stack update_set = new Stack();
    private int window_action_delay = 12;
    /**
     * Input array containing input from keyboard and mouse. Inputs will remain in the array until the key is released.
     */
    public CopyOnWriteArrayList<String> input_array = new CopyOnWriteArrayList<>();
    /**
     * Input array similar to input_array, except input information is only persistent for one frame. Used to indicate the key being pressed.
     */
    public CopyOnWriteArrayList<String> input_pressed_array = new CopyOnWriteArrayList<>();
    /**
     * Input array similar to input_pressed_array, except for when a key is released.
     */
    public CopyOnWriteArrayList<String> input_released_array = new CopyOnWriteArrayList<>();
    /**
     * Same as input_array using keycodes instead of strings.
     */
    public CopyOnWriteArrayList<Integer> input_array_keycodes = new CopyOnWriteArrayList<>();
    /**
     * Same as input_pressed_array except uses keycodes.
     */
    public CopyOnWriteArrayList<Integer> input_pressed_array_keycodes = new CopyOnWriteArrayList<>();
    /**
     * Same as input_released_array except uses keycodes.
     */
    public CopyOnWriteArrayList<Integer> input_released_array_keycodes = new CopyOnWriteArrayList<>();
    private JRootPane rootPane;
    /**
     * Timescale indicating how much time passes between each physics frame. Use to "speed up or slow down" time.
     */
    public int timescale = 1;
    private Primitive console_line_0;
    private Primitive console_line_1;
    private Primitive console_line_2;
    private Primitive console_line_3;
    private Primitive console_input;
    private Stack console_primitives = new Stack();
    private char[] console_chars = new char[144];
    private int console_cursor = 0;
    private boolean console_active = false;

    private Stack point_buffer = new Stack();

    public static Color redrawColor = Color.blue;
    /**
     * Boolean value indicating if window should be redrawn continually. Derived from windowHint_redraw windowhint.
     */
    public static boolean redraw = (boolean) WindowHint.windowHint_redraw.getValue();
    private static Color brushColor = new Color(255, 255, 255);


    private int cursorFar;


    public void addNotify() {
        super.addNotify();
        requestFocus();
    }


    /**
     * Instantiates a new Game window and runs all initial setup. The new Game_Window is returned to be used by user.
     *
     * @param frame the frame
     */
    public Game_Window(JFrame frame) {
        super();

        console_input = new Text(">", 14, this);
        console_line_0 = new Text("", 14, this);
        console_line_1 = new Text("", 14, this);
        console_line_2 = new Text("", 14, this);
        console_line_3 = new Text("", 14, this);

        console_primitives.add(console_input);
        console_primitives.add(console_line_0);
        console_primitives.add(console_line_1);
        console_primitives.add(console_line_2);
        console_primitives.add(console_line_3);

        console_primitives.disable_all();
        for (Primitive console_primitive : console_primitives) {
            console_primitive.move_to_top();
        }


        fps_readout = new Text("FPS:", 24, this);
        update_set.add(fps_readout);
        fps_readout.disable();
        fps_readout.move_to_top();
        do {
            rootPane = frame.getRootPane();
        } while (rootPane == null);

        Action aPressed = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input_array.add("a");
                System.out.println("a pressed");
            }
        };

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), aPressed);
        rootPane.getActionMap().put("aPressed", aPressed);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                notifyStack_KeyPressed(e);
                input_array.add(new String(new char[]{e.getKeyChar()}).toLowerCase());
                input_array_keycodes.add(e.getKeyCode());
                input_pressed_array.add(new String(new char[]{e.getKeyChar()}).toLowerCase());
                input_pressed_array_keycodes.add(e.getKeyCode());
                if (console_active && !new String(new char[]{e.getKeyChar()}).equals("`")) {
                    console_chars[console_cursor] = e.getKeyChar();
                    console_cursor = console_cursor + 1;

                }


                if (console_active && e.getKeyCode() == 8) {
                    console_cursor = console_cursor - 2;
                    console_chars[console_cursor] = ' ';

                }
                if (console_active && e.getKeyCode() == 10) {
                    Engine.console_command(new String(console_chars));
                    console_line_0.setText(Engine.previous_commands[0]);
                    console_line_1.setText(Engine.previous_commands[1]);
                    console_line_2.setText(Engine.previous_commands[2]);
                    console_line_3.setText(Engine.previous_commands[3]);
                    console_chars = new char[144];
                    console_cursor = 0;
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                synchronized (input_array) {
                    for (String s : input_array) {
                        if (s.equals(new String(new char[]{e.getKeyChar()}).toLowerCase())) {
                            input_array.remove(s);
                        }
                    }
                }
                ///input_array.remove(new String(new char[]{e.getKeyChar()}).toLowerCase());
                input_released_array.add(new String(new char[]{e.getKeyChar()}).toLowerCase());
                input_released_array_keycodes.add(e.getKeyCode());
            }
        });

        Action aReleased = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input_array.remove(input_array.indexOf("a"));
                System.out.println("a pressed");
            }

        };

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                input_array.add("mouse_pressed");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                for (String s : input_array) {
                    if (s.equals("mouse_pressed")) {
                        input_array.remove("mouse_pressed");
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        //     rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released A"), aReleased);
        //   rootPane.getActionMap().put("aReleased", aReleased);


        Engine.addEvent(new SlythrEvent() {
            @Override
            public boolean trigger() {
                return input_pressed_array.contains("`");
            }

            @Override
            public void action() {
                console_active = !console_active;
                if (console_active) {
                    console_primitives.enable();
                } else {
                    console_primitives.disable();
                }
            }

        });

        int repaintDelay = (int) Engine.getWindow_hint(Engine.WINDOW_HINT_REDRAW_DELAY);

        Thread physicsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Physics.simulate(timescale, getGraphics());
                    } catch (NullPointerException e) {
                        //pass;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(repaintDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, "Physics thread");

              physicsThread.start();

        SwingWorker physicsWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                while (!isCancelled()) {
                    try {
                        Physics.simulate(timescale, getGraphics());
                    } catch (NullPointerException e) {
                        //pass;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(repaintDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };

       // physicsWorker.execute();

        SwingWorker renderWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                while (!isCancelled()) {
                    for (Primitive text : update_set) {
                        try {
                            text.update(getGraphics());
                        } catch (NullPointerException ex) {
                            //pass;
                        }
                    }
                    if (WindowHint.windowHint_redraw.getValue()) {
                        repaint();
                    }
                    Thread.sleep(repaintDelay);
                }
                return null;
            }
        };

        renderWorker.execute();

        int periodicDelay = (int) Engine.getWindow_hint(Engine.WINDOW_HINT_PERIODIC_DELAY);

        Timer periodicTimer = new Timer(periodicDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fps_readout.setText("FPS: " + Integer.toString(Engine.fps));
                fps_readout.setpos((double) Engine.width - fps_readout.getWidth(), fps_readout.getHeight());
                if (console_active) {
                    console_input.setText(">" + new String(console_chars));
                }
                input_pressed_array.clear();
                input_pressed_array_keycodes.clear();
                input_released_array.clear();
                input_released_array_keycodes.clear();

                if (console_cursor < 0) {
                    console_cursor = 0;
                }

                if (console_cursor > 143) {
                    console_cursor = 143;
                }
                if (console_active) {
                    console_line_3.setpos(5.0, console_line_3.getHeight());
                    console_line_2.setpos(5.0, console_line_3.getpos()[1] + console_line_2.getHeight());
                    console_line_1.setpos(5.0, console_line_2.getpos()[1] + console_line_1.getHeight());
                    console_line_0.setpos(5.0, console_line_1.getpos()[1] + console_line_0.getHeight());
                    console_input.setpos(5.0, console_line_0.getpos()[1] + console_input.getHeight());
                }
                repaint();
            }
        });

        periodicTimer.start();

    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        local_g = g;
        //Graphics2D g2d = (Graphics2D) g.create();
//

        g.drawImage(Render.getGLRenderSurface(), 0, 0, null);

        Engine.rendStack.draw(g);
//       for (Vertex_Array vertexBuffer : vertexBuffers) {
//            if (vertexBuffer.drawAction == DRAW_POINT) {
//                for (int cursor = 0; cursor < vertexBuffer.vertexArray.length - (1 + vertexBuffer.stride); cursor = cursor + vertexBuffer.stride) {
//                    g2d.setColor(new Color(vertexBuffer.vertexArray[cursor + vertexBuffer.stride - 2], vertexBuffer.vertexArray[cursor + vertexBuffer.stride - 1], vertexBuffer.vertexArray[cursor + vertexBuffer.stride]));
//                    g2d.drawRect(vertexBuffer.vertexArray[cursor], vertexBuffer.vertexArray[cursor + 1], 1, 1);
//                }
//            }
//            if (vertexBuffer.drawAction == DRAW_LINE) {
//                for (int cursor = 0; cursor < vertexBuffer.vertexArray.length - (1 + vertexBuffer.stride); cursor = cursor + vertexBuffer.stride) {
//                    g2d.setColor(new Color(vertexBuffer.vertexArray[cursor + vertexBuffer.stride - 3], vertexBuffer.vertexArray[cursor + vertexBuffer.stride - 2], vertexBuffer.vertexArray[cursor + vertexBuffer.stride - 1]));
//                    g2d.drawLine(vertexBuffer.vertexArray[cursor], vertexBuffer.vertexArray[cursor + 1], vertexBuffer.vertexArray[cursor + 2], vertexBuffer.vertexArray[cursor + 3]);
//
//                }
//            }
//        }
       // Engine.rendStack.draw(g2d);
        Engine.fps_count = Engine.fps_count + 1;
        //g2d.dispose();

    }


    /**
     * NONFUNCTIONAL: Adds a game_loop thread.
     *
     * @param game_loop the game loop
     */
    public void add_game_loop(Game_loop game_loop){
        Engine.throwFatalError(new SlythrError("SLYTHR internal error: method add_game_loop() has not been implemented"));
    }

    /**
     * Checks if a key is being pressed.
     *
     * @param key key being checked
     * @return true if pressed
     */
    public boolean keyPressed(String key) {
        return input_pressed_array.contains(key);
    }

    /**
     * Checks if a key is depressed.
     *
     * @param key key being checked
     * @return true if pressed
     */
    public boolean keyDepressed(String key) {
        return input_array.contains(key);
    }

    /**
     * Checks if a key is being released.
     *
     * @param key key being checked
     * @return true if released
     */
    public boolean keyReleased(String key) {
        return input_released_array.contains(key);
    }

    /**
     * Checks if mouse is overlapping a primitive
     *
     * @param primitive target primitive
     * @return true if overlapping
     */
    public boolean mouseOverlapping(Primitive primitive) {
        try {
            return Physics.pointInObj(((int) getMousePosition().getX()), ((int) getMousePosition().getY()), primitive);
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Draw point.
     *
     * @param x     x position
     * @param y     y position
     * @param color color
     */
    public void drawPoint(double x, double y, Color color) {
        Rect newrect = new Rect(false);
        newrect.setpos(x, y);
        newrect.setHeight(1);
        newrect.setWidth(1);
        newrect.setColor(color);
        point_buffer.add(newrect);
    }

    private static void notifyStack_KeyPressed(KeyEvent e) {
        for (Primitive primitive : Engine.notifyStack_Key) {
            TaskManager.dispatch(new SlythrAction() {
                @Override
                public void execute() {
                    primitive.eventNotify_Key(e);
                }

                @Override
                public void execute2() {

                }
            });
        }
    }


}
