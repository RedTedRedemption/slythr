package slythr;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Particle {

    private ArrayList<Primitive> subStructure = new ArrayList<>();
    private ArrayList<Primitive> elements = new ArrayList<>();
    public static ArrayList<Particle> instances = new ArrayList<>();
    public Primitive element;
    private ParticleAction spawnAction;
    private ParticleAction behavior;
    private Primitive infant;
    private int lifetime;
    private boolean alive = false;

    public void spawn(int x, int y, int Lifetime) {
//        for (Primitive source : subStructure) {
//            infant = source.dupe();
//            source.enable();
//            spawnAction.action(source);
//        }
        alive = true;
        Primitive newPart = new Rect();
        newPart.setpos(x, y);
        spawnAction.action(newPart);
        elements.add(newPart);
        element = newPart;

        this.setBehavior(new ParticleAction() {
            @Override
            public void action(Primitive Particle) {
                //do nothing;
            }
        });

        Engine.addSubRoutine(new SubRoutine() {
            @Override
            public void routine() {
                if (alive) {
                    tick();
                }
            }
        });

        lifetime = Lifetime;

        instances.add(this);
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
        if (lifetime <= 0) {
            kill();
        }
        lifetime = lifetime - 1;
        behavior.action(this.element);
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
