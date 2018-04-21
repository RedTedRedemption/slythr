package test;

import slythr.*;

public class testGame {

    static Primitive ship;
    static Game_Window game_window;
    public static Particle playerProjectile;

    public static final int PROPERTY_LIGHT = 0;
    public static final int PROPERTY_LIGHT_INTENSITY = 1;
    public static Property<Integer> light = new Property<Integer>(PROPERTY_LIGHT, new Integer[] {PROPERTY_LIGHT_INTENSITY});


    public static void main(String[] args) {
        Engine.launch(600, 600);
        game_window = Engine.addWindow();
        ship = new Rect();
        ship.setColor(0, 255, 0);
        ship.centerx(300);
        ship.centery(300);

        ShaderProgram recieveLight = new ShaderProgram();
        recieveLight.linkShader(Render.VERTEX_SHADER, new plainVertexShader());
        recieveLight.linkShader(Render.GEOMETRY_SHADER, new plainGeometryShader());
        recieveLight.linkShader(Render.FRAGMENT_SHADER, new recieveLightFragShader());



        playerProjectile = new Particle();

        playerProjectile.setSpawnAction(new ParticleAction() {
            @Override
            public void action(Primitive Particle) {
                Particle.setpos(ship.center_x, ship.center_y);
                Particle.applyForce(0, -3);
                Particle.setHeight(10);
                Particle.setWidth(5);
                Particle.setColor(255, 0, 0);
             //   Particle.addProperty(light);
            }
        });
        Engine.joinSplash();


        Engine.addSubRoutine(new SubRoutine() {
            @Override
            public void routine() {
                ship.setPhysics_velocity(0, 0);

                if (game_window.input_array.contains("a")) {
                    ship.applyForce(-1, 0);
                }

                if (game_window.input_array.contains("s")) {
                    ship.applyForce(0, 1);
                }

                if (game_window.input_array.contains("d")) {
                    ship.applyForce(1, 0);
                }

                if (game_window.input_array.contains("w")) {
                    ship.applyForce(0, -1);
                }

                if (game_window.input_pressed_array.contains(" ")) {
                    playerProjectile.spawn(0, 0, 50);
                }
            }
        });


    }

}
