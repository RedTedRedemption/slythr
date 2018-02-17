package test;


import slythr.*;

import java.util.Random;

public class threadingDemo {

    public static Game_Window game_window;

    public static Random random = new Random();

    static int z = 1;

    public static void main(String[] args) {

        Engine.launch(600, 600);
        game_window = Engine.addWindow();


        ShaderProgram positionalColorProgram = new ShaderProgram();
        positionalColorProgram.linkShader(Render.VERTEX_SHADER, new plainVertexShader());
        positionalColorProgram.linkShader(Render.GEOMETRY_SHADER, new plainGeometryShader());
        positionalColorProgram.linkShader(Render.FRAGMENT_SHADER, new colorBasedFragShader());



        Engine.joinSplash();
        Primitive rect = new Rect();
        rect.setColor(255, 0, 0);
        Primitive rect2 = new Rect();
        rect2.setColor(0, 255, 0);
        rect2.setpos(150, 150);
        rect2.setHeight(300);
        rect2.setWidth(300);
        rect2.setShaderProgram(positionalColorProgram);
        Primitive rect3 = new Rect();
        rect3.setShaderProgram(positionalColorProgram);
        rect3.setColor(0, 0, 255);
        rect3.setpos(100, 100);

        Vertex_Array lineArray = Render.bindVertexArray(Render.DRAW_LINE, 7, positionalColorProgram, new int[] {300, 300, 400, 400, 255, 255, 255});
        Vertex_Array lineArray1 = Render.bindVertexArray(Render.DRAW_LINE, 7, positionalColorProgram, new int[] {300, 300, 400, 450, 255, 255, 255});
        //Vertex_Array triangle = Render.bindVertexArray(Render.FILL_TRIANGLE, 12, positionalColorProgram, new int[] {300, 50, 0, 120, 125, 0, 500, 500, 0, 255, 255, 255});


        Particle particle = new Particle();
        particle.setBehavior(new ParticleAction() {
            @Override
            public void action(Primitive Particle) {
                //Particle.setPhysics_velocity(0, 1);
            }
        });
        particle.setSpawnAction(new ParticleAction() {
            @Override
            public void action(Primitive Particle) {
                Particle.setHeight(25);
                Particle.setWidth(25);
                Particle.TEMP_setZ(3);
                Particle.setPhysics_velocity(0, 1);
                Particle.setShaderProgram(positionalColorProgram);

            }
        });

        Engine.addSubRoutine(new SubRoutine() {
            @Override
            public void routine() {

                //colorBasedFragShader.blue.update(random.nextInt(255));
            }
        });


        Engine.addSubRoutine(new SubRoutine() {
            @Override

            public void routine() {
                try {
                    //lineArray.setData(new int[] {300, 300, 400 / z, 400 / z, 255, 255, 255});
                    //lineArray1.setData(new int[] {300, 300, 400 / z, 450 / z, 255, 255, 255});
                    //z++;
                    while (game_window.input_array.contains("a")) {
                        particle.spawn(game_window.getMousePosition().x, game_window.getMousePosition().y, 50);
                    }
                } catch (java.lang.NullPointerException e) {
                    //pass;
                }
            }
        });

    }

}
