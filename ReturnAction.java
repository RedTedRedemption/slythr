package slythr;


import java.lang.reflect.Type;

public class ReturnAction<T>{

    public Class typeClass = ((T) new Object()).getClass();

    public ReturnAction() {

    }

    public T execute() {
        Engine.throwFatalError(new SlythrError("ERROR: " + this + " does not override execute() method from parent class ReturnAction"));
        return null;
    }

    public Class getType() {
        return ((T) new Object()).getClass();
    }

}
