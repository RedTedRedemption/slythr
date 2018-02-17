package slythr;

public class SMath {

    public static int getDistance_int(int x0, int y0, int x1, int y1) {
        return (int) (Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2)));
    }

    public static double getDistance_double(int x0, int y0, int x1, int y1) {
        return (int) (Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2)));
    }

    public static int clampUpper_int(int input, int ceiling) {
        if (input >= ceiling) {
            return ceiling;
        }
        return input;
    }



}
