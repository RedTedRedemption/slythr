package slythr;

import java.util.ArrayList;

/**
 * This class is responsible for creation and management of animations for primitives.
 * Animations have 4 modes: Keyframe, offset, enabled, and action.
 * Keyframe animations move the target to a point.
 * Offset animations move the target by a delta.
 * Enabled enables or disables the object's rendering: 1 will cause the object to be rendered, 0 will cause it to not be rendered.
 * Action animations execute a slythr action. A bread[0] value of 1 will cause execute() to be called, a bread[1] value of 1 will cause execute2() to be called.
 * Opacity will set the opacity of an object to the bread[0] value.
 * Color will set the object's color where bread is {red, blue, green}.
 */
public class Animation {

    /**
     * Array list containing a set of int[2] objects that indicate what action the animation should act upon the target at a given frame.
     */
    public ArrayList<int[]> bread = new ArrayList<>();
    public static final int KEYFRAME = 0;
    public static final int OFFSET = 1;
    public static final int ENABLED = 2;
    public static final int ACTION = 3;
    public static final int BLACKTOWHITE = 4;
    public static final int OPACITY = 5;
    public static final int COLOR = 6;

    public static Animation_Buffer default_buffer;
    /**
     * A primitive that the animation will act on.
     */
    public Primitive target;
    public int step = 0;
    public int mode;
    public boolean enabled = false;
    public SlythrAction action;
    public boolean loop = false;
    public boolean generic = false;


    /**
     * Create a new animation. Target can be null.
     * @param Target the target of the animation; is nullable
     * @param Mode What the animation should do to the object.
     * @param point_array
     */
    public Animation(Primitive Target, int Mode, ArrayList<int[]> point_array){
        for (int[] a : point_array){
            bread.add(a);
        }
        target = Target;
        mode = Mode;
        Engine.animation_buffer.add(this);
    }

    /**
     * Create a new Animation. This method does not require an initial set of animation points.
     * @param Target
     * @param Mode
     */
    public Animation(Primitive Target, int Mode){
        target = Target;
        mode = Mode;
        Engine.animation_buffer.add(this);
    }

    public Animation(int Mode){
        mode = Mode;
        Engine.animation_buffer.add(this);
    }


    /**
     * Create a new Animation object with no initial properties
     *
     */
    public Animation(){
        Engine.animation_buffer.add(this);
    }

    public static void bind_default_animation_buffer(Animation_Buffer buffer){
        default_buffer = buffer;
    }

    /**
     * sets the value of the {@code loop} parameter, which indicates if the animation should play in a loop and restart once it is complete
     * @param status
     */
    public void loopme(boolean status){
        loop = status;
    }

    public void bind_Action(SlythrAction slythrAction){
        action = slythrAction;
    }

    /**
     * Steps the animation and executes appropriate code based on mode and the value of bread.
     */
    public boolean Step() {
        if (enabled) {
            try {
                if (step >= bread.size()) {
                    if (loop) {
                        step = 0;
                    } else {
                        enabled = false;
                    }
                }
                if (mode == KEYFRAME) {
                    target.setpos(bread.get(step)[0], bread.get(step)[1]);
                    return true;
                }
                if (mode == OFFSET) {
                    target.move(bread.get(step)[0], bread.get(step)[1]);
                    return true;
                }
                if (mode == ENABLED) {
                    if (bread.get(step)[0] == 1) {
                        target.enable();
                    }
                    if (bread.get(step)[0] == 0) {
                        target.disable();
                    }
                }
                if (mode == ACTION) {
                    if (bread.get(step)[0] == 1) {
                        action.execute();
                    }
                    if (bread.get(step)[1] == 1) {
                        action.execute2();
                    }
                }
                if (mode == OPACITY) {
                    target.setOpacity(bread.get(step)[0]);
                }
                if (mode == COLOR) {
                    target.setColor(bread.get(step)[0], bread.get(step)[1], bread.get(step)[2]);
                }



                if (step >= bread.size() - 1) {
                    enabled = false;
                }
                step = step + 1;
            } catch (NullPointerException e) {
                System.out.println("WARNING! animation target has not been initialized yet");
                return false;
            }
        }
        return false;
    }

    public void setStep(int frame){
        step = frame;
    }

    public void bake(ArrayList<int[]> dough){
        bread.clear();
        for (int[] a : dough){
            bread.add(a);
        }
    }

    /**
     * add a point to
     * @param point int[2] object to add to bread
     */
    public void add(int[] point){
        bread.add(point);
    }

    public void setTarget(Primitive targ){
        target = targ;
    }

    public void start(){
        step = 0;
        enabled = true;
    }
    public void pause(){
        enabled = false;
    }
    public void resume(){
        enabled = true;
    }

    public void quick_animate(Primitive target) {
        if (default_buffer == null) {
            try {
                throw new SlythrError("Generic Animation buffer has not been initialized yet");
            } catch (SlythrError slythrError) {
                slythrError.printStackTrace();
            }
        } else {
            Animation temp = new Animation(this.target, this.mode, this.bread);
            default_buffer.add(temp);
            temp.setTarget(target);
            temp.generic = true;
            temp.start();
        }
    }

    public Animation duplicate_onto(){
        Animation tout = new Animation(null, this.mode);
        for (int[] point : this.bread){
            tout.add(point);
        }
        return tout;
    }

    public void setMode(int Mode){
        mode = Mode;
    }
}

