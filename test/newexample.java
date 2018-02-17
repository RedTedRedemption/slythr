package test;

import slythr.Engine;
import slythr.Game_Window;
import slythr.Primitive;
import slythr.Text;

public class newexample {

    public static void main(String[] args) {
        Engine.launch(600, 600);
        Game_Window game_window = Engine.addWindow();
        Primitive helloWorld = new Text("Hello, world!", 24, game_window);
        Engine.joinSplash();
    }

}
