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

    public Vertex_Array(int DrawAction, int Stride, ShaderProgram shaderProgram, int[] array) {
        drawAction = DrawAction;
        stride = Stride;
        vertexArray = array;
        program = shaderProgram;
        pointArtifact = new int[stride];
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
