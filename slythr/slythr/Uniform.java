package slythr;

import java.util.ArrayList;

public class Uniform<T> {

    public static ArrayList<Uniform> uniforms = new ArrayList<>();
    private T value;

    public Uniform() {
        uniforms.add(this);
    }

    public Uniform(T initialValue) {
        value = initialValue;
        uniforms.add(this);
    }

    public void update(T val) {
        value = val;
    }

    public T get() {
        return value;
    }

}
