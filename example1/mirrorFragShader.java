package example1;

import slythr.Render;
import slythr.Shader;

public class mirrorFragShader extends Shader{

    private static final int xPos = 0;
    private static final int yPos = 1;
    private static final int r = 4;
    private static final int g = 5;
    private static final int b = 6;

    public int[] shader(int[] artifact) {
        try {
            artifact[r] = Render.getLastColorAtPixel(artifact[xPos], (Render.startingBuffer[yPos]))[0];
            artifact[r] += Render.getLastColorAtPixel(artifact[xPos], (Render.startingBuffer[yPos]) - (artifact[yPos] - Render.startingBuffer[yPos]))[0];
            artifact[r] = artifact[r] / 2;

            artifact[g] = Render.getLastColorAtPixel(artifact[xPos], (Render.startingBuffer[yPos]))[1];
            artifact[g] += Render.getLastColorAtPixel(artifact[xPos], (Render.startingBuffer[yPos]) - (artifact[yPos] - Render.startingBuffer[yPos]))[1];
            artifact[g] = artifact[g] / 2;

            artifact[b] = Render.getLastColorAtPixel(artifact[xPos], (Render.startingBuffer[yPos]))[2];
            artifact[b] += Render.getLastColorAtPixel(artifact[xPos], (Render.startingBuffer[yPos]) - (artifact[yPos] - Render.startingBuffer[yPos]))[2];
            artifact[b] = artifact[b] / 2;

        } catch (Exception e) {

        }
        return artifact;
    }

}
