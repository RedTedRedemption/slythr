package slythr;

/**
 * Created by teddy on 8/13/17.
 *
 * Class containing various engine variables. These are initialized during engine launch.
 */
public class Evar {

    /**
     * Host operating system
     */
    static String os;
    /**
     * Audio master volu,e.
     */
    static double master_volume = 1;


    /**
     * Initialize variables and set up their initial values.
     */
    public static void init(){
        if (System.getProperty("os.name").contains("win")){
            os = "win";
        } else {
            os = "notwin";
        }

    }

}
