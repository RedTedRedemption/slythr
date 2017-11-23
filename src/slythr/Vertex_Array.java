package slythr;

/**
 * Created by teddy on 10/31/17.
 */
public class Vertex_Array {
    int stride;
    int[] vertexArray;
    int drawAction;
    ShaderProgram program;

    public Vertex_Array(int DrawAction, int Stride, ShaderProgram shaderProgram, int[] array) {
        drawAction = DrawAction;
        stride = Stride;
        vertexArray = array;
        program = shaderProgram;
    }

    public void setData(int[] dataArray) {
        vertexArray = dataArray;
    }

    public void setProgram(ShaderProgram shaderProgram) {
        program = shaderProgram;
    }
}
