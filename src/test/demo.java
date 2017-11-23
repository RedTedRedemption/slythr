package test;

import slythr.*;

public class demo {

    public static Game_Window game_window;
    public static ShaderProgram positionalColorProgram = new ShaderProgram();
    public static ShaderProgram plainShader = new ShaderProgram();

    public static void main(String[] args) {

        Engine.launch(600, 600);
        game_window = Engine.addWindow();
        Primitive testPlainRect = new Rect();
        Primitive rect = new Rect();
        Primitive movingRect = new Rect();

        testPlainRect.setpos(100, 50);
        movingRect.setHeight(100);
        movingRect.setWidth(100);
        movingRect.setShaderProgram(positionalColorProgram);


        rect.setpos(50, 50);
        rect.setHeight(50);
        rect.setWidth(50);
        rect.setShaderProgram(plainShader);
        positionalColorProgram.linkShader(Render.VERTEX_SHADER, new vertexShader());
        positionalColorProgram.linkShader(Render.GEOMETRY_SHADER, new geometryShader());
        positionalColorProgram.linkShader(Render.FRAGMENT_SHADER, new fragmentShader());

        plainShader.linkShader(Render.VERTEX_SHADER, new vertexShader());
        plainShader.linkShader(Render.GEOMETRY_SHADER, new geometryShader());
        plainShader.linkShader(Render.FRAGMENT_SHADER, new colorBasedFragShader());

        Engine.joinSplash();
        Engine.addSubRoutine(new SubRoutine() {
            @Override
            public void routine() {
                try {
                    movingRect.setpos(game_window.getMousePosition().x, game_window.getMousePosition().y);
                } catch (Exception e) {
                    //pass;
                }
            }
        });

    }

}
