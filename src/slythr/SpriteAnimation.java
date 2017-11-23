package slythr;

import java.util.ArrayList;

/**
 * Created by teddy on 4/21/17.
 */
public class SpriteAnimation {

    public ArrayList<int[]> bread = new ArrayList<>();
    public String name = "default";
    public String mode;

    public SpriteAnimation(String Name, String Mode){
        name = Name;
        mode = Mode;
    }

    public int[] step(int frame){
        if (frame >= bread.size()){
            System.out.println("WARNING! tried to animate a frame outside the range of " + name + "'s animation points");
            return new int[] {0, 0};
        } else {
            return bread.get(frame);
        }
    }

    public void add(int[] point){
        bread.add(point);
    }


}
