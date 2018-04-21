package slythr;

import javax.rmi.CORBA.Util;

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
     * Audio master volume.
     */

    /**
     * Verbosity level
     */

    public static int verbosityLevel;

    public static final int VERBOSITY_SILENT = 0;
    public static final int VERBOSITY_QUIET = 1;
    public static final int VERBOSITY_NORMAL = 2;
    public static final int VERBOSITY_VERBOSE = 3;

    static double master_volume = 1;


    /**
     * Initialize variables and set up their initial values.
     */
    public static void init(){
        verbosityLevel = VERBOSITY_NORMAL;
        Utils.println_Normal("===========================");
        Utils.print_Normal("Initializing engine variables...");
        if (System.getProperty("os.name").contains("win")){
            os = "win";
        } else {
            os = "notwin";
        }
        Utils.println_Normal("done");
        Utils.println_Normal("===========================");
    }

    public static void init(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case "-silent":
                    verbosityLevel = VERBOSITY_SILENT;
                    break;

                case "-verbose":
                    verbosityLevel = VERBOSITY_VERBOSE;
                    Utils.println_Verbose("Running in verbose mode");
                    break;
            }
        }

        Utils.println_Normal("===========================");
        Utils.println_Normal("Initializing engine variables...");

        Utils.println_Verbose("Getting operating system...");
        if (System.getProperty("os.name").contains("win")){
            os = "win";
            Utils.println_Verbose("OS is windows");
        } else {
            os = "notwin";
            Utils.println_Verbose("OS is not windows");
        }
        Utils.println_Normal("Engine variables initialized");
        Utils.println_Normal("===========================");

    }



}
