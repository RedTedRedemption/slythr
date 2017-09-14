package slythr;

import java.awt.*;

/**
 * Created by teddy on 6/19/17.
 */
public class Utils {

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




}
