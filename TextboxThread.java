package slythr;

/**
 * Created by teddy on 5/23/17.
 */
public class TextboxThread implements Runnable{

    int wait_time;
    Complex target_text_box;
    Thread waitThread;
    int start_time;

    public TextboxThread(int milis, Complex Text_box){
        waitThread = new Thread(this, "textbox wait thread");
        wait_time = milis;
        start_time = milis;
        target_text_box = Text_box;


    }

    public void run(){
        while (wait_time > 0) {
            wait_time = wait_time - 1;
            target_text_box.getContinue_guage().setWidth((target_text_box.getInner_rect().getWidth() * wait_time) / start_time);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
