package test;

import slythr.Shader;

public class colorBasedFragShader implements Shader {

    private static final int xPos = 0;
    private static final int yPos = 1;
    private static final int r = 4;
    private static final int g = 5;
    private static final int b = 6;

    public int[] shader(int[] artifact) {
        artifact[r] = artifact[xPos];
        artifact[g] = artifact[yPos];
        return artifact;
    }

}
