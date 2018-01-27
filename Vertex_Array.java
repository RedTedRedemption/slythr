package slythr;

/**
 * Created by teddy on 10/31/17.
 */
public class Vertex_Array {
    int stride;
    int[] vertexArray;
    int drawAction;
    ShaderProgram program;
    public int[] pointArtifact;
    public boolean enabled = true;

    int[] intRegister;

    public Vertex_Array(int DrawAction, int Stride, ShaderProgram shaderProgram, int[] array) {
        drawAction = DrawAction;
        stride = Stride;
        vertexArray = array;
        program = shaderProgram;
        pointArtifact = new int[stride];
    }

    public int[] pumpPixel(int[] pixelArray) {
        intRegister = program.pipeline[Render.GEOMETRY_SHADER].shader(pixelArray);
        intRegister = program.pipeline[Render.FRAGMENT_SHADER].shader(intRegister);
        return intRegister;
    }

    public void setData(int[] dataArray) {
        vertexArray = dataArray;
    }

    public void setProgram(ShaderProgram shaderProgram) {
        program = shaderProgram;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public void disable() {
        enabled = false;
    }

    public void enable() {
        enabled = true;
    }
}
