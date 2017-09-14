package slythr;

import com.sun.scenario.Settings;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Audio class is responsible for playing audio files. It contains internal code to maintain its own instances. Each
 * Audio instance runs in its own thread.
 */
public class Audio implements Runnable {

    static ArrayList<Thread> threads = new ArrayList<>();
    static ArrayList<Audio> audios = new ArrayList<>();



    public boolean playing = false;
    Thread audiothread;
    String self_path;
    MediaPlayer mediaPlayer;

    /**
     *
     * @param path The path to the source file for the audio instance.
     * @throws IOException
     * @throws LineUnavailableException
     */
    public Audio(String path) throws IOException, LineUnavailableException {
        SplashScreen.status.setText("loading audio file " + path);
        String bip = path;
        self_path = path;
        Media hit = new Media(new File(bip).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        audios.add(this);
    }
    public void run(){
        playing = true;
        mediaPlayer.play();
    }

    /**
     * Play the audio instance.
     */
    public void play(){
        audiothread = new Thread(this, "slythr audio thread playing " + Utils.localizePath(self_path));
        threads.add(audiothread);
        audiothread.start();
        playing = true;
        this.mediaPlayer.setVolume(Evar.master_volume);
    }

    /**
     * Resume an audio instance.
     */
    public void resume(){
        if (playing){
            mediaPlayer.play();
        }
    }

    /**
     * Pause the audio instance.
     */
    public void stop(){
       mediaPlayer.stop();
       playing = false;
    }

    /**
     * Pause all audio instances.
     */
    public static void pauseAll(){
        for (Audio a : audios){
            a.mediaPlayer.pause();
        }
    }

    /**
     * Resume all audio instances.
     */
    public static void resumeAll(){
        for (Audio a : audios){
            if (a.playing) {
                a.resume();
            }
        }
    }

    /**
     * Set the volume of all audio instances.
     * @param vol {@code double} value between 0 and 1 to set all instances' volume to.
     */
    public static void set_volume(double vol) {
        Evar.master_volume = vol;
        Settings.set("volume", Double.toString(vol));
        for (Audio a : audios){
            a.mediaPlayer.setVolume(Evar.master_volume);
        }
    }


    /**
     * Will create an audio object with the given path and play it. This object can only be manipulated by the {@code pauseAll()},
     * {@code resumeAll}, and {@code set_volume()} methods. Instances created by this method cannot be controlled individually.
     * @param path Path to the file to play
     * @throws IOException
     * @throws LineUnavailableException
     */
    public static void quick_play(String path) throws IOException, LineUnavailableException {
        Audio quick_audio = new Audio(path);
        quick_audio.play();
    }

}
