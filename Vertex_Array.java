package slythr;

/**
 * Created by teddy on 10/31/17.
 */
public class Vertex_Array {
    int stride;
    int[] vertexArray;
    int drawAction;

    public Vertex_Array(int DrawAction, int Stride, int[] array) {
        drawAction = DrawAction;
        stride = Stride;
        vertexArray = array;
    }

}
