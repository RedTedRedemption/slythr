package test;


import slythr.*;

import java.util.Random;

public class demo {

    public static Game_Window game_window;
    public static ShaderProgram positionalColorProgram = new ShaderProgram();
    public static ShaderProgram mirrorShader = new ShaderProgram();
    static int pos = 0;
    public static Particle testParticle;

    public static Random random = new Random();

    public static void main(String[] args) {

        Engine.launch(600, 600);
        game_window = Engine.addWindow();
        Primitive testPlainRect = new Rect();
        Primitive rect = new Rect();


        testPlainRect.setpos(100, 50);

        testParticle = new Particle();
        testParticle.setSpawnAction(new ParticleAction() {
            @Override
            public void action(Primitive Particle) {
                Particle.setHeight(3);
                Particle.setWidth(3);
                //Particle.setPhysics_velocity(random.nextInt(10) - 10, -random.nextInt(15));
                Particle.setPhysics_velocity(0, 1);
                Particle.setShaderProgram(positionalColorProgram);
            }
        });
        testParticle.setBehavior(new ParticleAction() {
            @Override
            public void action(Primitive Particle) {
                //
            }
        });



        mirrorShader.linkShader(Render.VERTEX_SHADER, new vertexShader());
        mirrorShader.linkShader(Render.GEOMETRY_SHADER, new geometryShader());
        mirrorShader.linkShader(Render.FRAGMENT_SHADER, new mirrorFragmentShader());


        rect.setpos(50, 50);
        rect.setHeight(50);
        rect.setWidth(50);
        rect.setShaderProgram(positionalColorProgram);
        positionalColorProgram.linkShader(Render.VERTEX_SHADER, new vertexShader());
        positionalColorProgram.linkShader(Render.GEOMETRY_SHADER, new geometryShader());
        positionalColorProgram.linkShader(Render.FRAGMENT_SHADER, new colorBasedFragShader());



        Primitive movingRect = new Rect();
        movingRect.setHeight(100);
        movingRect.setWidth(100);
        movingRect.setShaderProgram(mirrorShader);



        //Render.bindVertexArray(Render.FILL_RECT, 7, mirrorShader, new int[] {0, 5, 30, 30, 255, 255, 255});

        Engine.joinSplash();

        Engine.addSubRoutine(new SubRoutine() {
            @Override
            public void routine() {
                try {
                   movingRect.setpos(game_window.getMousePosition().x, game_window.getMousePosition().y);
                   //testParticle.spawn(game_window.getMousePosition().x, game_window.getMousePosition().y, 15);
                } catch (Exception e) {
                    //pass;
                }
            }
        });

    }

}
