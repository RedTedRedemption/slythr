package slythr;

import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Scanner;

/**
 * Created by teddy on 6/19/17.
 */
public class Utils {

    private static Scanner scanner = new Scanner(System.in);
    private static int intRegister;
    private static Object objRegister;

    public static int getFrameHeight(Frame frame){
        if (Evar.os.equals("win")){
            return frame.getHeight() - 29;
        }
        return frame.getHeight() - 23;
    }

    public static int getFrameWidth(Frame frame){
        if (Evar.os.equals("win")){
            return frame.getWidth();
        }
        return frame.getWidth();
    }


    public static String localizePath(String unix_style){
        if (Evar.os.equals("win")){
            return unix_style.replace("/", "\\");
        }
        return unix_style;
    }

//    public synchronized static void printArray(int[] array) {
//        for (int o : array) {
//            System.out.print(o);
//            System.out.print(", ");
//        }
//        System.out.println();
//    }

    public synchronized static void printArray(Object[] array) {
        for (Object o : array) {
            System.out.print(o);
            System.out.print(", ");
        }
        System.out.println();
    }

    public synchronized static void swap(int index0, int index1, int[] arr) {
        intRegister = arr[index0];
        arr[index0] = arr[index1];
        arr[index1] = intRegister;
    }

    public static void pause() {
        scanner.next();
    }




}
