package slythr;

/**
 * Created by teddy on 8/13/17.
 */
public class Evar {

    static String os;
    static double master_volume = 1;

    public static void init(){
        if (System.getProperty("os.name").contains("win")){
            os = "win";
        } else {
            os = "notwin";
        }

    }

}
