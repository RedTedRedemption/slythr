package test;

import slythr.Render;
import slythr.Shader;

import java.awt.*;

public class fragmentShader implements Shader{


    private static final int xPos = 0;
    private static final int yPos = 1;
    private static final int height = 2;
    private static final int r = 4;
    private static final int g = 5;
    private static final int b = 6;



    public int[] shader(int[] artifact) {

        try {
            artifact[r] = Render.getColorAtPixel(artifact[xPos], (Render.startingBuffer[yPos]) - (artifact[yPos] - Render.startingBuffer[yPos]))[0];
        } catch (Exception e) {

        }
        return artifact;
    }
}
