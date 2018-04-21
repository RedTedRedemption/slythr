package slythr; /**
 * Created by teddy on 4/28/17.
 */

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    public static JLabel watermark = new JLabel
            ("Powered by SLYTHR", JLabel.CENTER);
    public static JLabel status = new JLabel("", JLabel.LEFT);
    public static JLabel copyright = new JLabel("Slythr Engine created by Theodore Herzfeld", JLabel.RIGHT);
    private static boolean permitEnd = false;

    public SplashScreen(int d) {
        int duration = d;
    }


    public void showSplash() {
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.white);

        // Set the window's bounds, centering the window
        int width = 650;
        int height = 230;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        // Build the splash screen

        watermark.setFont(new Font("Sans-Serif", Font.BOLD, 36));
        status.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        copyright.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        content.add(watermark, BorderLayout.CENTER);
        content.add(status, BorderLayout.SOUTH);
        content.add(copyright, BorderLayout.NORTH);
        Color oraRed = Color.red;
        content.setBorder(BorderFactory.createLineBorder(oraRed, 10));

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display it
        setVisible(true);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (!permitEnd){
            //wait for signal to allow splash to exit and program to continue
        }
        System.out.println("splash done");
        setVisible(false);
    }

    public void showSplashAndExit() {
        showSplash();

    }

    public static void setStatus(String status){
        SplashScreen.status.setText(status);
    }

    public static void allowEnd(){
        permitEnd = true;
    }



    public void setInvisible(){
        setVisible(false);
    }
}