package slythr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by teddy on 8/13/17.
 */
public class Game_Window extends JPanel {

    Timer repaint_timer;
    public int draw_wait = 6;
    public Primitive fps_readout;
    private Graphics local_g;
    private Stack update_set = new Stack();
    private int window_action_delay = 12;
    public ArrayList<String> input_array = new ArrayList<>();
    public ArrayList<String> input_pressed_array = new ArrayList<>();
    public ArrayList<String> input_released_array = new ArrayList<>();
    public ArrayList<Integer> input_array_keycodes = new ArrayList<>();
    public ArrayList<Integer> input_pressed_array_keycodes = new ArrayList<>();
    public ArrayList<Integer> input_released_array_keycodes = new ArrayList<>();
    private JRootPane rootPane;
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
    public Stack point_buffer = new Stack();
    public static Color redrawColor = Color.blue;
    public static boolean redraw = (boolean) WindowHint.windowHint_redraw.value;
    private static Color brushColor = new Color(255, 255, 255);
    public static ArrayList<Vertex_Array> vertexBuffers = new ArrayList();
    public static final int DRAW_POINT = 0;
    public static final int DRAW_LINE = 1;
    public static final int FILL_RECT = 2;
    private int cursorFar;


    public void addNotify() {
        super.addNotify();
        requestFocus();
    }


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
        for (Primitive console_primitive : console_primitives.makeArrayList()) {
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
                input_array.remove(new String(new char[]{e.getKeyChar()}).toLowerCase());
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
                input_array.remove("mouse_pressed");
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
                    } catch (java.lang.NullPointerException e) {
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
                    } catch (java.lang.NullPointerException e) {
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
                    for (Primitive text : update_set.makeArrayList()) {
                        try {
                            text.update(getGraphics());
                        } catch (java.lang.NullPointerException ex) {
                            //pass;
                        }
                    }
                    if ((boolean) WindowHint.windowHint_redraw.value) {
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
                fps_readout.setpos((double) Engine.width - fps_readout.getWidth(), (double) fps_readout.getHeight());
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
        Graphics2D g2d = (Graphics2D) g.create();
        if (redraw) {
            g2d.setColor(redrawColor);
            g2d.fillRect(0, 0, Engine.width, Engine.height);
        }

        g2d.drawImage(Render.GLRenderSurface, 0, 0, null);
//        for (Vertex_Array vertexBuffer : vertexBuffers) {
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
        Engine.rendStack.draw(g2d);
        point_buffer.draw(g2d);
        point_buffer.flush();
        Engine.fps_count = Engine.fps_count + 1;
        g2d.dispose();

    }


    public void add_game_loop(Game_loop game_loop){

    }

    public boolean keyPressed(String key) {
        return input_pressed_array.contains(key);
    }

    public boolean keyDepressed(String key) {
        return input_array.contains(key);
    }

    public boolean keyReleased(String key) {
        return input_released_array.contains(key);
    }

    public boolean mouseOverlapping(Primitive primitive) {
        try {
            return Physics.pointInObj(((int) getMousePosition().getX()), ((int) getMousePosition().getY()), primitive);
        } catch (java.lang.NullPointerException e) {
            return false;
        }
    }

    public void drawPoint(double x, double y, Color color) {
        Rect newrect = new Rect(false);
        newrect.setpos(x, y);
        newrect.setHeight(1);
        newrect.setWidth(1);
        newrect.setColor(color);
        point_buffer.add(newrect);
    }

    public static void bindVertexArray(int drawAction, int stride, int[] array) {
        vertexBuffers.add(new Vertex_Array(drawAction, stride, array));

    }
}
