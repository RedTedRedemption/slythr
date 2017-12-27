package slythr;

import test.plainFragShader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Render {

    public static Shader[] pipeline = new Shader[3];
    public static int[] startingBuffer = new int[100];
    private static Color brush = new Color(255, 255, 255);
    public static BufferedImage GLRenderSurface;
    public static BufferedImage blankSurface;
    public static BufferedImage bufferSurface;
    public static Rect drawArea;
    public static Graphics g;
    private static int rgbAtPoint;
    private static int intRegister;
    private static int[] intArrayRegister;
    private static ArrayList<Thread> renderThreads = new ArrayList<>();

    private static int max_z = 0;

    private static final int x = 0;
    private static final int y = 1;
    private static final int z = 2;
    private static final int r = 3;
    private static final int G = 4;
    private static final int b = 5;

    public static int[][] pixels = new int[600 * 600][6];
    public static int pixelCursor = 0;



    public static final int PIXEL_ARTIFACT_LENGTH = 100;

    public static boolean haltDraw = false;


    public static ArrayList<Vertex_Array> vertexBuffers = new ArrayList();

    private static ShaderProgram activeProgram;
    public static ShaderProgram defaultProgram;

    public static final int DRAW_POINT = 0;
    public static final int DRAW_LINE = 1;
    public static final int FILL_RECT = 2;

    public static final int VERTEX_SHADER = 0;
    public static final int GEOMETRY_SHADER = 1;
    public static final int FRAGMENT_SHADER = 2;

    public static void init() {
        GLRenderSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
        blankSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < blankSurface.getHeight(); y++) {
            for (int x = 0; x < blankSurface.getWidth(); x++) {
                blankSurface.setRGB(x, y, WindowHint.windowHint_clear_color.getValue().getRGB());
            }
        }
        bufferSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
        drawArea = new Rect(true);
        drawArea.setHeight(Engine.height);
        drawArea.setWidth(Engine.width);

        Primitive.plainShaderProgram = new ShaderProgram();
        Primitive.plainShaderProgram.linkShader(VERTEX_SHADER, new plainVertexShader());
        Primitive.plainShaderProgram.linkShader(GEOMETRY_SHADER, new plainGeometryShader());
        Primitive.plainShaderProgram.linkShader(FRAGMENT_SHADER, new plainFragShader());

        defaultProgram = new ShaderProgram();
        defaultProgram.linkShader(VERTEX_SHADER, new plainVertexShader());
        defaultProgram.linkShader(GEOMETRY_SHADER, new plainGeometryShader());
        defaultProgram.linkShader(FRAGMENT_SHADER, new plainFragShader());

        activeProgram = Primitive.plainShaderProgram;


    }

    public static void render() {
        if (activeProgram.pipeline[VERTEX_SHADER] == null || activeProgram.pipeline[GEOMETRY_SHADER] == null || activeProgram.pipeline[FRAGMENT_SHADER] == null) {
            Engine.throwFatalError(new SlythrError("ERROR: INCOMPLETE SHADER PROGRAM!"));
        }
        bufferSurface.setData(blankSurface.getRaster());
        for (Vertex_Array vertex_array : vertexBuffers) {
            if (vertex_array.enabled) {
                if (vertex_array.drawAction == Render.FILL_RECT) {
                    drawRect(vertex_array);
                }
                if (vertex_array.drawAction == Render.DRAW_POINT) {
                    drawPoint(vertex_array);
                }
            }
        }
        synchronized (GLRenderSurface) {
            GLRenderSurface.setData(bufferSurface.getRaster());
        }
    }

    public static void hardwareAcceleratedRend() {
//        bufferSurface.setData(blankSurface.getRaster());
//        for (Vertex_Array vertex_array : vertexBuffers) {
//            if (vertex_array.enabled) {
//                SKernel.dispatch(vertex_array);
//            }
//        }
    }

    public static void threadRend() {
        haltDraw = true;
        bufferSurface.setData(blankSurface.getRaster());
        for (Vertex_Array vertex_array : vertexBuffers) {
            if (vertex_array.enabled) {
                SKernel.dispatch(vertex_array);
            }
        }

        SKernel.joinAll();

        blit();

        pixelCursor = 0;

        GLRenderSurface.setData(bufferSurface.getRaster());
        haltDraw = false;

        for (int[] pixel : pixels) {
            pixel[x] = 0;
            pixel[y] = 0;
            pixel[z] = 0;
            pixel[r] = 0;
            pixel[G] = 0;
            pixel[b] = 0;
        }

//        bufferSurface.setData(blankSurface.getRaster());
//        try {
//            for (Vertex_Array vertex_array : vertexBuffers) {
//                if (vertex_array.program.pipeline[VERTEX_SHADER] == null || vertex_array.program.pipeline[GEOMETRY_SHADER] == null || vertex_array.program.pipeline[FRAGMENT_SHADER] == null) {
//                    Engine.throwFatalError(new SlythrError("ERROR: INCOMPLETE SHADER PROGRAM!"));
//                }
//                SKernel.dispatch(vertex_array);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        SKernel.joinAll();
//        GLRenderSurface.setData(bufferSurface.getRaster());

    }

    public static void render(Vertex_Array vertex_array) {
        if (activeProgram.pipeline[VERTEX_SHADER] == null || activeProgram.pipeline[GEOMETRY_SHADER] == null || activeProgram.pipeline[FRAGMENT_SHADER] == null) {
            Engine.throwFatalError(new SlythrError("ERROR: INCOMPLETE SHADER PROGRAM!"));
        }
        //bufferSurface.setData(blankSurface.getRaster());
        if (vertex_array.enabled) {
            if (vertex_array.drawAction == Render.FILL_RECT) {
                drawRect(vertex_array);
            }
            if (vertex_array.drawAction == Render.DRAW_POINT) {
                drawPoint(vertex_array);
            }
            // GLRenderSurface.setData(bufferSurface.getRaster());
        }
    }

    public static void addShader(int shaderType, Shader shader) {
        pipeline[shaderType] = shader;
    }

    public static int[] makeArtifact(Vertex_Array vertex_array, int cursor) {
        int[] tout = new int[100];
        for (int pointer = 0; pointer <= vertex_array.stride - 1; pointer++) {
            tout[pointer] = vertex_array.vertexArray[cursor + pointer];
        }
        return tout;
    }

    private static int convertToRBG_INT(int r, int g, int b) {
        return 0xFF000000 | (r << 16) & 0x00FF0000 | (g << 8) & 0x0000FF00 | b & 0x000000FF;
    }

    private static void drawPoint(Vertex_Array vertex_array) {
        activeProgram = vertex_array.program;
        for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
            startingBuffer = makeArtifact(vertex_array, cursor);
            startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
            int pointArtifact[] = new int[100];
            for (int pointArtifactFiller = 0; pointArtifactFiller < vertex_array.stride; pointArtifactFiller++) {
                pointArtifact[pointArtifactFiller] = startingBuffer[pointArtifactFiller];
            }
            pointArtifact = activeProgram.pipeline[GEOMETRY_SHADER].shader(pointArtifact);
            if (shouldDrawPixel(pointArtifact[0], pointArtifact[1])) {
                try {
                    pointArtifact = activeProgram.pipeline[FRAGMENT_SHADER].shader(pointArtifact);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("failed to render");
                }
                putPixelToBuffer(pointArtifact[0], pointArtifact[1], 3, pointArtifact[2], pointArtifact[3], pointArtifact[4]);
//                System.out.println("drawing point");
//                try {
//                    bufferSurface.setRGB(pointArtifact[0], pointArtifact[1], convertToRBG_INT(pointArtifact[2], pointArtifact[3], pointArtifact[4]));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println("point rendered");
            }
        }
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
            int pointArtifact[] = new int[100];
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
                        putPixelToBuffer(pointArtifact[0], pointArtifact[1], 3, pointArtifact[4], pointArtifact[5], pointArtifact[6]);

                    }

                }
            }
        }
    }

    public synchronized static void putPixelToBuffer(int x, int y, int z, int r, int g, int b) {
        try {
            //bufferSurface.setRGB(x, y, convertToRBG_INT(r, g, b));
            if (pixelCursor < pixels.length) {
                pixels[pixelCursor][0] = x;
                pixels[pixelCursor][1] = y;
                pixels[pixelCursor][2] = z;
                pixels[pixelCursor][3] = r;
                pixels[pixelCursor][4] = g;
                pixels[pixelCursor][5] = b;
                pixelCursor = pixelCursor + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void legacy_putPixelToBuffer(int x, int y, int z, int r, int g, int b) {}

    private boolean cullRect(int x, int y, int height, int width) {
        return false;
    }

    public static boolean shouldDrawPixel(int x, int y) {
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
            intRegister = getGLRenderSurface().getRGB(x, y);
        } catch (Exception e) {
            intRegister = WindowHint.windowHint_clear_color.value.getRGB();
        }
        return new int[]{
                (intRegister >> 16) & 0xFF,
                (intRegister >> 8) & 0xFF,
                intRegister & 0xFF
        };
    }

    private static void blit() {
        max_z = 0;
        for (int[] pixel : pixels) {
            if (pixel[z] > max_z) {
                max_z = pixel[z];
            }
        }
        while (max_z >= 0) {
            for (int[] pixel : pixels) {
                if (pixel[z] == max_z) {
                    bufferSurface.setRGB(pixel[x], pixel[y],convertToRBG_INT(pixel[r], pixel[G], pixel[b]));
                }
            }
            max_z = max_z - 1;
        }
    }

    public static synchronized BufferedImage getGLRenderSurface() {
        return GLRenderSurface;
    }
}