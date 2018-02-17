//package slythr;
//
//import java.util.Iterator;
//import java.util.Spliterator;
//import java.util.function.Consumer;
//
//public class SDynamicArray<T> implements Iterable{
//
//    private int size;
//    private T[] list;
//    private T[] tempList;
//
//    public SDynamicArray() {
//        size = 0;
//    }
//
//    public void add(T o) {
//        tempList = (T[]) new Object[size + 1];
//        size = size + 1;
//        System.arraycopy(list, 0, tempList, 0, list.length);
//        tempList[size - 1] = o;
//        list = new (T[]) Object[size];
//        System.arraycopy();
//
//    }
//
//
//    @Override
//    public Iterator iterator() {
//        return null;
//    }
//
//    @Override
//    public void forEach(Consumer action) {
//
//    }
//
//    @Override
//    public Spliterator spliterator() {
//        return null;
//    }
//}
