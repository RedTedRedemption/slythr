package example1;

import slythr.*;
import test.mirrorFragmentShader;

public class Main {

    public static void main(String[] args) {
        Engine.launch(600, 600); //launch the engine and create a window with height 600 and width 600
        Game_Window game_window = Engine.addWindow(); //create a game window on which to draw and get input and store it for later

        //initial setup
        ShaderProgram mirrorProgram = new ShaderProgram(); //create a shader program for mirrors. Shader programs contain a pipeline of shaders
                                                           //that are used to calculate position and color of an object
        mirrorProgram.linkShader(Render.VERTEX_SHADER, new plainVertexShader()); //link a new shader to the shader program and declare it as
                                                                                 //a vertex shader. The default planeVertexShader is used since
                                                                                 //no changes to the objects position will be made
        mirrorProgram.linkShader(Render.GEOMETRY_SHADER, new plainGeometryShader());
        mirrorProgram.linkShader(Render.FRAGMENT_SHADER, new mirrorFragShader()); //link our custom mirror shader

        ShaderProgram positionalColorProgram = new ShaderProgram();
        positionalColorProgram.linkShader(Render.VERTEX_SHADER, new plainVertexShader());
        positionalColorProgram.linkShader(Render.GEOMETRY_SHADER, new plainGeometryShader());
        positionalColorProgram.linkShader(Render.FRAGMENT_SHADER, new positionalFragShader());

        Primitive mirrorRect = new Rect(); //create a new rectangle
        mirrorRect.setHeight(75); //set its height and width
        mirrorRect.setWidth(75);
        mirrorRect.setColor(255, 0, 0); //set its color
        mirrorRect.setpos(60, 60); //set the object's position (specifically the upper left corner of it)
        mirrorRect.setShaderProgram(mirrorProgram); //set the shader program to be run when it's rendered to the mirrorProgram

        Primitive mirrorText = new Text("Mirror material", 12, game_window); //create a new text object
        mirrorText.setpos(mirrorRect.getpos()[0], mirrorRect.getpos()[1]); //set it to the position of the mirror object

        Primitive cursorRect = new Rect(); //create a rectangle to follow the cursor
        cursorRect.setShaderProgram(positionalColorProgram); //give it a shader program
        Primitive cursorText = new Text("positional color example", 12, game_window);


        Particle particle = new Particle();
        Rect samplerect = new Rect();
        particle.setPrimitive(samplerect.getClass());
        particle.setSpawnAction(new ParticleAction() {
            @Override
            public void action(Primitive Particle) {
                Particle.setPhysics_velocity(1, -4);
                Particle.setShaderProgram(positionalColorProgram);
            }
        });

        particle.setBehavior(new ParticleAction() {
            @Override
            public void action(Primitive Particle) {
                Particle.applyForce(0, .1);
            }
        });

        Engine.addSubRoutine(new SubRoutine() { //will run 60 times a second in its own thread
            @Override
            public void routine() {
                try {
                    cursorRect.setpos(game_window.getMousePosition().x, game_window.getMousePosition().y); //set the rectangle to the position of the mouse
                    cursorText.setpos(cursorRect.getpos()[0], cursorRect.getpos()[1]); //set the text to the rectangle's position
                    particle.spawn(game_window.getMousePosition().x, game_window.getMousePosition().y, 50);
                } catch (java.lang.NullPointerException e) {
                    //if the mouse is outside of the window the getMousePosition method throws an exception
                }
            }
        });


        //end setup

        Engine.joinSplash(); //tell the engine we are ready for it to close the splash window once enough time has passed
    }

}
