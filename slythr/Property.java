package slythr;

public class Property<T> {

    public int id;
    public T[] data;

    public Property(int ID, T[] Data) {
        id = ID;
        data = Data;
    }

}
