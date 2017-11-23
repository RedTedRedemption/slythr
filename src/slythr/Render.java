package slythr;

import com.aparapi.Kernel;
import test.plainFragShader;

import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;


public class Render {

    public static Shader[] pipeline = new Shader[3];
    public static int[] startingBuffer;
    private static Color brush = new Color(255, 255, 255);
    public static BufferedImage GLRenderSurface;
    public static BufferedImage blankSurface;
    public static BufferedImage bufferSurface;
    public static Rect drawArea;
    public static Graphics g;
    private static int rgbAtPoint;
    private static int intRegister;
    private static int[] intArrayRegister;

    public static ArrayList<Vertex_Array> vertexBuffers = new ArrayList();

    private static ShaderProgram activeProgram;

    public static final int DRAW_POINT = 0;
    public static final int DRAW_LINE = 1;
    public static final int FILL_RECT = 2;

    public static final int VERTEX_SHADER = 0;
    public static final int GEOMETRY_SHADER = 1;
    public static final int FRAGMENT_SHADER = 2;

    public static void init() {
        GLRenderSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
        blankSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
        bufferSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
        drawArea = new Rect(true);
        drawArea.setHeight(Engine.height);
        drawArea.setWidth(Engine.width);

        Primitive.plainShaderProgram = new ShaderProgram();
        Primitive.plainShaderProgram.linkShader(VERTEX_SHADER, new plainVertexShader());
        Primitive.plainShaderProgram.linkShader(GEOMETRY_SHADER, new plainGeometryShader());
        Primitive.plainShaderProgram.linkShader(FRAGMENT_SHADER, new plainFragShader());

        activeProgram = Primitive.plainShaderProgram;
    }

    public static void render() {
        if (activeProgram.pipeline[VERTEX_SHADER] == null || activeProgram.pipeline[GEOMETRY_SHADER] == null || activeProgram.pipeline[FRAGMENT_SHADER] == null) {
            Engine.throwFatalError(new SlythrError("ERROR: INCOMPLETE SHADER PROGRAM!"));
        }
        bufferSurface.setData(blankSurface.getRaster());
        for (Vertex_Array vertex_array : vertexBuffers) {
            if (vertex_array.drawAction == FILL_RECT) {
                Kernel kernel = new Kernel() {
                    @Override
                    public void run() {
                        drawRect(vertex_array);
                    }
                };
            }
        }
        GLRenderSurface.setData(bufferSurface.getRaster());
    }

    public static void addShader(int shaderType, Shader shader) {
        pipeline[shaderType] = shader;
    }

    private static int[] makeArtifact(Vertex_Array vertex_array, int cursor) {
        int[] tout = new int[vertex_array.stride];
        for (int pointer = 0; pointer <= tout.length - 1; pointer++) {
            tout[pointer] = vertex_array.vertexArray[cursor + pointer];
        }
        return tout;
    }

    private static int getRGB(int r, int g, int b) {
        return 0xFF000000 | (r << 16) & 0x00FF0000 | (g << 8) & 0x0000FF00 | b & 0x000000FF;
    }

    private static void drawRect(Vertex_Array vertex_array) {
        activeProgram = vertex_array.program;

        for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
//            System.out.println(cursor);
//            for (int i : makeArtifact(vertex_array, cursor)) {
//                System.out.print(i);
//                System.out.print(", ");
//            }

            startingBuffer = makeArtifact(vertex_array, cursor);
            startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
            int pointArtifact[] = new int[vertex_array.stride];
            for (int y = startingBuffer[1]; y <= startingBuffer[1] + startingBuffer[3]; y++) {
                for (int x = startingBuffer[0]; x <= startingBuffer[0] + startingBuffer[2]; x++) {
                    pointArtifact[0] = x;
                    pointArtifact[1] = y;
                    for (int pointArtifactFiller = 2; pointArtifactFiller < vertex_array.stride; pointArtifactFiller++) {
                        pointArtifact[pointArtifactFiller] = startingBuffer[pointArtifactFiller];
                    }
//                    pointArtifact[2] = startingBuffer[2];
//                    pointArtifact[3] = startingBuffer[3];
//                    pointArtifact[4] = startingBuffer[4];
//                    pointArtifact[5] = startingBuffer[5];
//                    pointArtifact[6] = startingBuffer[6];
                    pointArtifact = activeProgram.pipeline[GEOMETRY_SHADER].shader(pointArtifact);

                    //pixel position based culling
                    if (shouldDrawPixel(pointArtifact[0], pointArtifact[1])) {
                        pointArtifact = activeProgram.pipeline[FRAGMENT_SHADER].shader(pointArtifact);
                        try {
                            if (pointArtifact[0] >= 0 && pointArtifact[0] < Engine.width && pointArtifact[1] >= 0 && pointArtifact[1] < Engine.height) {
                                bufferSurface.setRGB(pointArtifact[0], pointArtifact[1], getRGB(pointArtifact[4], pointArtifact[5], pointArtifact[6]));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
        }
    }

    private boolean cullRect(int x, int y, int height, int width) {
        return false;
    }

    private static boolean shouldDrawPixel(int x, int y) {
        return x > 0 && x < Engine.width && y > 0 && y < Engine.height;
    }

    public static void use(ShaderProgram shaderProgram) {
        activeProgram = shaderProgram;
    }

    public static Vertex_Array bindVertexArray(int drawAction, int stride, ShaderProgram shaderProgram, int[] array) {
        Vertex_Array new_vertexArray = new Vertex_Array(drawAction, stride, shaderProgram, array);
        vertexBuffers.add(new_vertexArray);
        return new_vertexArray;

    }

    public static int[] getColorAtPixel(int x, int y) {
        try {
            intRegister = bufferSurface.getRGB(x, y);
        } catch (Exception e) {
            intRegister = WindowHint.windowHint_clear_color.value.getRGB();
        }
        return new int[]{
                (intRegister >> 16) & 0xFF,
                (intRegister >> 8) & 0xFF,
                intRegister & 0xFF
        };
    }

    public static int[] getLastColorAtPixel(int x, int y) {
        try {
            intRegister = GLRenderSurface.getRGB(x, y);
        } catch (Exception e) {
            intRegister = WindowHint.windowHint_clear_color.value.getRGB();
        }
        return new int[]{
                (intRegister >> 16) & 0xFF,
                (intRegister >> 8) & 0xFF,
                intRegister & 0xFF
        };
    }
}