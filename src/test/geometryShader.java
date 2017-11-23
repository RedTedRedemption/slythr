package test;

import slythr.Shader;

import java.util.Random;

public class geometryShader implements Shader {


    private static int pos;

    private static final int xPos = 0;
    private static final int yPos = 1;
    private static final int width = 2;
    private static final int height = 3;

    Random random = new Random();


    public int[] shader(int[] artifact) {

        return artifact;
    }

}
