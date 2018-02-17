package slythr;

/**
 * Created by teddy on 4/25/17.
 *
 *
 * Used to create actions that slythr tools such as animations can execute
 *
 * when an animation's [0] value is 1, execute() will be called, when its [1] value is 1, otherwise() will be called
 */
public interface SlythrAction {

    void execute();

    void execute2();
}
