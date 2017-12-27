package slythr;//package slythr;
//
//import stardust.GameLoop;
//import stardust.GlobalGamestate;
//import stardust.Main;
//import stardust.MainPane;
//
//import java.awt.Graphics;
//
///**
// * Thread used to count how many frames are rendered in a second.
// */
//public class FPScounter implements Runnable {
//
//    Thread fpsthread;
//
//    public static Primitive fps;
//    public static Primitive cps;
//    public static Primitive fps_background = new Rect();
//    public static Primitive cps_background = new Rect();
//
//    //static Instant drawcount_start = Instant.now();
//
//    public FPScounter() {
//        boolean retry = true;
//        while (retry) {
//            try {
//                System.out.print("creating fps counter thread...");
//                fpsthread = new Thread(this, "FPS counter thread");
//                //fps = new Text("", 24, MainPane.global_g);
//                //cps = new Text("", 24, MainPane.global_g);
//                cps_background.setColor(0, 0, 0);
//                fps_background.setColor(0, 0, 0);
//
//
//
//
//
//
//
//
//                //fps.setpos(Utils.getFrameWidth(MainPane.host_frame) - fps.getBounding_box().getWidth() - 10, 24);
//               // cps.setpos(Utils.getFrameWidth(MainPane.host_frame) - cps.getBounding_box().getWidth() - 10, fps.getpos()[1] + cps.getBounding_box().getHeight());
//
//
//
//
//
//
//                System.out.println("done");
//                retry = false;
//            } catch (NullPointerException e) {
//                System.out.print("retrying...");
//            }
//        }
//
//    }
//
//    public void run() {
//        System.out.print("starting fps counter thread...");
////        System.out.println("waiting for Graphics source to not be null");
////        while (MainPane.global_g == null){
////           //pass;
////        }
//        System.out.println("fps counter setup");
//        while (true) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            fps.setText("FPS: " + Integer.toString(MainPane.drawcount));
//            fps.update(MainPane.global_g);
//            fps.setpos(Utils.getFrameWidth(MainPane.host_frame) - fps.getBounding_box().getWidth(), 24);
//            cps.setText("CPS: " + Integer.toString(GameLoop.game_loop_count));
//            cps.update(MainPane.global_g);
//            cps.setpos(Utils.getFrameWidth(MainPane.host_frame) - cps.getBounding_box().getWidth(), fps.getpos()[1] + cps.getBounding_box().getHeight());
//
//
//            if (GlobalGamestate.evar_os.equals("win") || Main.evar_force_windows){
//                fps.setpos(Utils.getFrameWidth(MainPane.host_frame) - fps.getBounding_box().getWidth() - 7, 24);
//                cps.setpos(Utils.getFrameWidth(MainPane.host_frame) - cps.getBounding_box().getWidth() - 7, fps.getpos()[1] + cps.getBounding_box().getHeight());
//            }
//            else {
//                fps.setpos(Utils.getFrameWidth(MainPane.host_frame) - fps.getBounding_box().getWidth(), 24);
//                cps.setpos(Utils.getFrameWidth(MainPane.host_frame) - cps.getBounding_box().getWidth(), fps.getpos()[1] + cps.getBounding_box().getHeight());
//
//            }
//
//            cps_background.setWidth(cps.getWidth());
//            cps_background.setHeight(cps.getHeight());
//            cps_background.setpos(cps.getpos()[0], cps.getpos()[1] - fps.getHeight());
//
//
//
//            fps_background.setWidth(fps.getWidth());
//            fps_background.setHeight(fps.getHeight());
//            fps_background.setpos(fps.getpos()[0], fps.getpos()[1] - fps.getHeight());
//
//
//
//            MainPane.drawcount = 0;
//            GameLoop.game_loop_count = 0;
//
//
//        }
//    }
//
//    public static void draw(Graphics g){
//        fps_background.draw(g);
//        cps_background.draw(g);
//        fps.draw(g);
//        cps.draw(g);
//    }
//
//}
