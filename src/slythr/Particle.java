package slythr;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Particle {

    private ArrayList<Primitive> subStructure = new ArrayList<>();
    private ArrayList<Primitive> elements = new ArrayList<>();
    public static ArrayList<Particle> instances = new ArrayList<>();
    public Primitive element;
    private boolean GLEnabled = true;
    private ParticleAction spawnAction = new ParticleAction() {
        @Override
        public void action(Primitive Particle) {

        }
    };
    private ParticleAction behavior = new ParticleAction() {
        @Override
        public void action(Primitive Particle) {

        }
    };
    private Primitive infant;
    private int lifetime;
    private boolean alive = false;
    private Primitive templateClass;

    public void setPrimitive(Primitive template) {
        templateClass = template;
        templateClass.disable();
    }

    public void setGLEnabled(boolean b) {
        GLEnabled = b;
    }


    public void spawn(int x, int y, int Lifetime) {
//        for (Primitive source : subStructure) {
//            infant = source.dupe();
//            source.enable();
//            spawnAction.action(source);
//        }
        alive = true;
        Primitive newPart = null;
        if (!GLEnabled) {
            newPart =templateClass.newMe(false);
        } else {
            newPart = templateClass.newMe();
        }
        newPart.setpos(x, y);
        spawnAction.action(newPart);
        elements.add(newPart);
        element = newPart;

        lifetime = Lifetime;

        instances.add(this);

        while(element == null) {
            //wait
        }

        Engine.addSubRoutine(new SubRoutine() {
            @Override
            public void routine() {
                if (alive) {
                    tick();
                }
            }
        });


    }

    public void setSpawnAction(ParticleAction action) {
        spawnAction = action;
    }

    public void setBehavior(ParticleAction action) {
        behavior = action;
    }

    public void addSubStructure(Primitive primitive) {
        subStructure.add(primitive);
        primitive.disable();
    }

    public void tick() {
        if (lifetime <= 0 || this.element == null) {
            kill();
            return;
        }
        lifetime = lifetime - 1;
        behavior.action(this.element);
//        catch (java.lang.NullPointerException e) {
//            e.printStackTrace();
//            //pass
//        }
    }

    public void kill() {
//        for (Primitive element : elements) {
//            element.disable();
//            element = null;
//            Engine.rendStack.remove(element);
//        }
        //element.disable();
        //element.vertex_array.disable();
        //System.out.println("killing");
        alive = false;
    }

}
