package slythr;//package slythr;
//
//import javax.swing.*;
//import java.awt.*;
//
///**
// * Created by teddy on 4/26/17.
// */
//public class MouseLocation extends Thread{
//
//    private Thread t;
//    private String threadName;
//    private JRootPane rootPane;
//    public int[] mousePos = {0, 0};
//
//    public MouseLocation(JRootPane rootpane){
//        super("Slythr mouse locator");
//        System.out.print("Creating mouse location thread...");
//        threadName = "Slythr mouse location listener";
//        System.out.println("done");
//        start();
//
//    }
//
//
//    public void run() {
//        while (true) {
//            mousePos[0] = MouseInfo.getPointerInfo().getLocation().x;
//            mousePos[1] = MouseInfo.getPointerInfo().getLocation().y;
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
////    public void start(){
////        if (t == null){
////            t = new Thread(this, threadName);
////            t.start();
////        }
////    }
//
//
//}
