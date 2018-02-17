package slythr;

public class Pixel {

    public int x;
    public int y;
    public int z;
    public int r;
    public int g;
    public int b;
    public int[] color;

    public Pixel() {
        x = 0;
        y = 0;
        z = 0;
        r = 255;
        g = 255;
        b = 255;
        color = new int[] {r, g, b};

    }

}
