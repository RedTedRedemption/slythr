package test;

import slythr.SMath;
import slythr.Shader;
import slythr.Uniform;

public class colorBasedFragShader extends Shader {

    private static final int xPos = 0;
    private static final int yPos = 1;
    private static final int zPos = 2;
    private static final int r = 3;
    private static final int g = 4;
    private static final int b = 5;

    public static Uniform<Integer> blue;
    public static Uniform<Integer[]> light;

    public colorBasedFragShader() {
        blue = new Uniform<>();
    }

    public int[] shader(int[] artifact) {
        artifact[3] = artifact[0];
        artifact[4] = artifact[1];
        artifact[5] = artifact[5];
     //   artifact[5] = blue.get();
        return artifact;
    }

}
