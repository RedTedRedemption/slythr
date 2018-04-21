package slythr;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {

    private static File logFile;
    private static PrintWriter out;
    private static boolean enabled = false;

    public static void init() {
        enabled = true;
        final String dir = System.getProperty("user.dir") + File.separator +"logs" + File.separator;
        String date = new SimpleDateFormat("yyy_MM_dd_HH_mm_ss").format(new Date());
        System.out.println("logfile is " + dir + date + ".log");
        logFile = new File(dir + date + ".log");
        try {
            out = new PrintWriter(new FileWriter(logFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (logFile.createNewFile()) {
                Utils.println_Verbose("Log file created");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void log(Object o) {
        if (enabled) {
            out.println(o);
        }
    }

    public static void log_inline(Object o) {
        if (enabled) {
            out.print(o);
        }
    }

    public static void close() {
        System.out.print("Closing log file...");
        out.println("Closing log file");
        out.close();
        System.out.println("Done \nBye!");
    }

}
