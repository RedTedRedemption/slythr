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
        do{
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
                input_array.add(new String(new char[] {e.getKeyChar()}).toLowerCase());
                input_array_keycodes.add(e.getKeyCode());
                input_pressed_array.add(new String(new char[] {e.getKeyChar()}).toLowerCase());
                input_pressed_array_keycodes.add(e.getKeyCode());
                if (console_active && !new String(new char[] {e.getKeyChar()}).equals("`")) {
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
                input_array.remove(new String(new char[] {e.getKeyChar()}).toLowerCase());
                input_released_array.add(new String(new char[] {e.getKeyChar()}).toLowerCase());
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

        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released A"), aReleased);
        rootPane.getActionMap().put("aReleased", aReleased);








        ActionListener repainter = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Primitive text : update_set.makeArrayList()){
                    try {
                        text.update(local_g);
                    } catch (java.lang.NullPointerException ex){
                        //pass;
                    }
                }
                if ( (boolean) WindowHint.windowHint_redraw.value) {
                    repaint();
                }
            }
        };

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


        repaint_timer = new Timer(((int) Engine.getWindow_hint(Engine.WINDOW_HINT_REDRAW_DELAY)), repainter);
        repaint_timer.start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                repaint_timer.start();
//            }
//        }).start();

//
//        ActionListener periodic_window_actions = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                fps_readout.setText("FPS: " + Integer.toString(Engine.fps));
//                fps_readout.setpos(Engine.width - fps_readout.getWidth(), fps_readout.getHeight());
//                if (console_active) {
//                    console_input.setText(">" + new String(console_chars));
//                }
//                input_pressed_array.clear();
//                input_pressed_array_keycodes.clear();
//                input_released_array.clear();
//                input_released_array_keycodes.clear();
//
//                if (console_cursor < 0) {
//                    console_cursor = 0;
//                }
//
//                if (console_cursor > 143) {
//                    console_cursor = 143;
//                }
//                if (console_active) {
//                    console_line_3.setpos(5, console_line_3.getHeight());
//                    console_line_2.setpos(5, console_line_3.getpos()[1] + console_line_2.getHeight());
//                    console_line_1.setpos(5, console_line_2.getpos()[1] + console_line_1.getHeight());
//                    console_line_0.setpos(5, console_line_1.getpos()[1] + console_line_0.getHeight());
//                    console_input.setpos(5, console_line_0.getpos()[1] + console_input.getHeight());
//                }
//
//
//            }
//        };

        Thread periodic_window_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    fps_readout.setText("FPS: " + Integer.toString(Engine.fps));
                    fps_readout.setpos(Engine.width - fps_readout.getWidth(), fps_readout.getHeight());
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
                        console_line_3.setpos(5, console_line_3.getHeight());
                        console_line_2.setpos(5, console_line_3.getpos()[1] + console_line_2.getHeight());
                        console_line_1.setpos(5, console_line_2.getpos()[1] + console_line_1.getHeight());
                        console_line_0.setpos(5, console_line_1.getpos()[1] + console_line_0.getHeight());
                        console_input.setpos(5, console_line_0.getpos()[1] + console_input.getHeight());
                    }
                }
            }
        });

        periodic_window_thread.start();

      //  Timer periodic_window_action_timer = new Timer(window_action_delay, periodic_window_actions);
      //  periodic_window_action_timer.start();

    }



    public void paintComponent(Graphics g) {
        local_g = g;
        if (redraw) {
            g.setColor(redrawColor);
            g.fillRect(0, 0, Engine.width, Engine.height);
        }
        Engine.rendStack.draw(g);
        point_buffer.draw(g);
        point_buffer.flush();
        Engine.fps_count = Engine.fps_count + 1;
        Physics.simulate(timescale, g);
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

    public void drawPoint(int x, int y, Color color) {
        Rect newrect = new Rect(false);
        newrect.setpos(x, y);
        newrect.setHeight(1);
        newrect.setWidth(1);
        newrect.setColor(color);
        point_buffer.add(newrect);
    }
}
