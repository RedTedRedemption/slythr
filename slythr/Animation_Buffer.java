package slythr;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by teddy on 4/20/17.
 *
 * Animation_buffers are sets of animations. Animations should be added to a buffer, and the buffer should be stepped
 * once for each cycle of the main game loop.
 */
public class Animation_Buffer {

    public CopyOnWriteArrayList<Animation> buffer = new CopyOnWriteArrayList<>();


    public Animation_Buffer(){

    }

    /**
     * Adds an animation to the buffer.
     * @param append Animation to be appended to the buffer
     */
    public void add(Animation append){
        buffer.add(append);
    }

    /**
     * Steps every animation in the buffer.
     */
    public void step(){
        for (Animation a : buffer){
            a.Step();
        }
    }

    /**
     * Remove an animation from the buffer.
     * @param animation to remove
     */
    public void remove(Animation animation){
        buffer.remove(buffer.indexOf(animation));
    }

    /**
     * Pause all animations in the buffer.
     */
    public void pause_all(){
        for (Animation animation : buffer) {
            animation.pause();
        }
    }

}
