package slythr;

import test.plainFragShader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * The type Render.
 */
public class Render {

    /**
     * Pipeline containing shaders.
     */
    public static Shader[] pipeline = new Shader[3];
    /**
     * Data buffer containing data passed to the entrance of the pipeline.
     */
    public static int[] startingBuffer = new int[100];
    private static Color brush = new Color(255, 255, 255);
    /**
     * Surface drawn to the window.
     */
    public static BufferedImage GLRenderSurface;
    /**
     * An example blank surface used to clear other surfaces.
     */
    public static BufferedImage blankSurface;
    /**
     * Buffer surface that render processes draw to. When drawing is complete it is flashed to the GLRenderSurface. Implements double-buffering.
     */
    public static BufferedImage bufferSurface;


    private static Rect drawArea;
    /**
     * Contains window graphics info.
     */
    public static Graphics g;
    private static int rgbAtPoint;
    private static int intRegister;
    private static int[] intArrayRegister;
    private static ArrayList<Thread> renderThreads = new ArrayList<>();

    private static final Object synch = new Object();

    private static int max_z = 0;

    private static final int x = 0;
    private static final int y = 1;
    private static final int z = 2;
    private static final int r = 3;
    private static final int G = 4;
    private static final int b = 5;

    /**
     * The constant pixels.
     */
    public static int[][] pixels = new int[Engine.height * Engine.width][6];
    /**
     * The constant blankPixels.
     */
    public static int[][] blankPixels = new int[Engine.height * Engine.width][6];
    /**
     * The constant pixelCursor.
     */
    public static int pixelCursor = 0;

    /**
     * Boolean that can block the draw process.
     */
    public static boolean haltDraw = false;


    /**
     * ArrayList containing Vertex_Buffers that have been bound by user or library.
     */
    public static CopyOnWriteArrayList<Vertex_Array> vertexBuffers = new CopyOnWriteArrayList<>();

    private static ShaderProgram activeProgram;
    /**
     * A default ShaderProgram for use by user or library.
     */
    public static ShaderProgram defaultProgram;

    /**
     * Tag ID indicating draw action. Draws a point.
     */
    public static final int DRAW_POINT = 0;
    /**
     * Tag ID indicating draw action. Draws a line.
     */
    public static final int DRAW_LINE = 1;
    /**
     * Tag ID indicating draw action. Fills a rectangle.
     */
    public static final int FILL_RECT = 2;
    /**
     * Tag ID indicating draw action. Fills a triangle.
     */
    public static final int FILL_TRIANGLE = 3;

    /**
     * Tag ID indicating a shader's role and location in pipeline. Used for vertex shaders.
     */
    public static final int VERTEX_SHADER = 0;
    /**
     * Tag ID indicating a shader's role and location in pipeline. Used for geometry shaders.
     */
    public static final int GEOMETRY_SHADER = 1;
    /**
     * Tag ID indicating a shader's role and location in pipeline. Used for fragment shaders.
     */
    public static final int FRAGMENT_SHADER = 2;

    /**
     * Initialize the class and any necessary startup operation.
     */
    public static void init() {
        System.out.print("Initializing render system...");
        GLRenderSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
        blankSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < blankSurface.getHeight(); y++) {
            for (int x = 0; x < blankSurface.getWidth(); x++) {
                blankSurface.setRGB(x, y, WindowHint.windowHint_clear_color.getValue().getRGB());
            }
        }
        int[] blankPixel = {0, 0, 0, 0, 0, 0};
        for (int i = 0; i < blankPixels.length; i++) {
            System.arraycopy(blankPixel, 0, blankPixels[i], 0, 6);
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

        System.out.println("done");


    }

    /**
     * Render each Vertex_Array. Does not use multithreading or hardware acceleration.
     */
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
        synchronized (synch) {
            GLRenderSurface.setData(bufferSurface.getRaster());
        }
    }

    /**
     * Render using hardware acceleration.
     */
    public static void hardwareAcceleratedRend() {
//        bufferSurface.setData(blankSurface.getRaster());
//        for (Vertex_Array vertex_array : vertexBuffers) {
//            if (vertex_array.enabled) {
//                SKernel.dispatch(vertex_array);
//            }
//        }
        Engine.throwFatalError(new SlythrError("Hardware accelerated rendering has been disabled -- set EngineSettings hardware accelerating setting to false"));
    }

    /**
     * Render using multithreading.
     */
    public static void threadRend() {
        haltDraw = true;
        bufferSurface.setData(blankSurface.getRaster());
        for (Vertex_Array vertex_array : vertexBuffers) {
            if (vertex_array.enabled) {
                SKernel.dispatch(vertex_array);
            }
        }

        SKernel.joinAll();

        fastBlit();

        pixelCursor = 0;

        GLRenderSurface.setData(bufferSurface.getRaster());
        haltDraw = false;

        //System.arraycopy(pixels, 0, pixels, 0, pixels.length);

        System.arraycopy(blankPixels, 0, pixels, 0, blankPixels.length);

//       for (int[] pixel : pixels) {
//
//        }

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

    /**
     * Render a specific Vertex_Array
     *
     * @param vertex_array vertex array to be drawn
     */
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

//    /**
//     * Add shader.
//     *
//     * @param shaderType the shader type
//     * @param shader     the shader
//     */
//    public static void addShader(int shaderType, Shader shader) {
//        pipeline[shaderType] = shader;
//    }

    /**
     * Extract a single artifact from a vertex_array
     *
     * @param vertex_array the vertex array
     * @param cursor       cursor indicating start of the artifact
     * @return an int[] containing the artifact
     */
    public static int[] makeArtifact(Vertex_Array vertex_array, int cursor) {
        int[] tout = new int[vertex_array.stride];
        System.arraycopy(vertex_array.vertexArray, cursor, tout, 0, vertex_array.stride);
//        for (int pointer = 0; pointer <= vertex_array.stride - 1; pointer++) {
//            tout[pointer] = vertex_array.vertexArray[cursor + pointer];
//        }
        return tout;
    }

    /**
     * Make artifact int [ ].
     *
     * @param startingBuffer the starting buffer
     * @return the int [ ]
     */
    public static int[] makeArtifact(int[] startingBuffer) {
        int len = startingBuffer.length;
        return new int[] {startingBuffer[0], startingBuffer[1], startingBuffer[2], startingBuffer[len -1], startingBuffer[len - 2], startingBuffer[len - 3]};
    }

    /**
     * Convert 3 integer rgb data to single int rgb data
     *
     * @param r red value
     * @param g green value
     * @param b blue value
     * @return single integer rgb data
     */
    public static int convertToRBG_INT(int r, int g, int b) {
        return 0xFF000000 | (r << 16) & 0x00FF0000 | (g << 8) & 0x0000FF00 | b & 0x000000FF;
    }

    private static void drawTriangle(Vertex_Array vertex_array) {

    }

    private static void fillBottomFlatTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int r, int g, int b) {

        //      0
        //     / \
        //    /   \
        //   /     \
        //  1-------2



    }

    private static void drawPoint(Vertex_Array vertex_array) {
        activeProgram = vertex_array.program;
        for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
            startingBuffer = makeArtifact(vertex_array, cursor);
            startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
            int pointArtifact[] = new int[100];
            System.arraycopy(startingBuffer, 0, pointArtifact, 0, vertex_array.stride);
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
                        putPixelToBuffer(pointArtifact[0], pointArtifact[1], 3, pointArtifact[4], pointArtifact[5], pointArtifact[6]);

                    }
                }
            }
        }
    }

    /**
     * Put a pixel to the buffer surface.
     *
     * @param x x position
     * @param y y position
     * @param z z position
     * @param r red color value
     * @param g green color value
     * @param b blue color value
     */
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

    /**
     * Legacy put pixel to buffer.
     * @deprecated
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @param r the r
     * @param g the g
     * @param b the b
     */
    public static void legacy_putPixelToBuffer(int x, int y, int z, int r, int g, int b) {}

    private boolean cullRect(int x, int y, int height, int width) {
        return false;
    }

    /**
     * Determine if a pixel will appear on screen. Used for culling.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    public static boolean shouldDrawPixel(int x, int y) {
        return x > 0 && x < Engine.width && y > 0 && y < Engine.height;
    }


    /**
     * Indicate which shader program to use
     *
     * @param shaderProgram the shader program
     */
    public static void use(ShaderProgram shaderProgram) {
        activeProgram = shaderProgram;
    }

    /**
     * Create a vertex array and pass it to the render system.
     *
     * @param drawAction    draw action
     * @param stride        length of a single artifact
     * @param shaderProgram shader program to pass the artifact through
     * @param array         array containing the data to be drawn
     * @return the new vertex array to be used by user
     */
    public static synchronized Vertex_Array bindVertexArray(int drawAction, int stride, ShaderProgram shaderProgram, int[] array) {
        Vertex_Array new_vertexArray = new Vertex_Array(drawAction, stride, shaderProgram, array);
        vertexBuffers.add(new_vertexArray);
        return new_vertexArray;

    }

    /**
     * Get the color of a pixel at a location
     *
     * @param x x position
     * @param y y position
     * @return 3 integer rgb data
     */
    public static int[] getColorAtPixel(int x, int y) {
        try {
            intRegister = bufferSurface.getRGB(x, y);
        } catch (Exception e) {
            intRegister = WindowHint.windowHint_clear_color.getValue().getRGB();
        }
        return new int[]{
                (intRegister >> 16) & 0xFF,
                (intRegister >> 8) & 0xFF,
                intRegister & 0xFF
        };
    }

    /**
     * Get the color at a pixel from last successful render.
     *
     * @param x x position
     * @param y y position
     * @return 3 integers containing rgb data
     */
    public static int[] getLastColorAtPixel(int x, int y) {
        try {
            intRegister = getGLRenderSurface().getRGB(x, y);
        } catch (Exception e) {
            intRegister = WindowHint.windowHint_clear_color.getValue().getRGB();
        }
        return new int[]{
                (intRegister >> 16) & 0xFF,
                (intRegister >> 8) & 0xFF,
                intRegister & 0xFF
        };
    }

    private static void fastBlit() {
        for (int[] pixel : pixels) {
            bufferSurface.setRGB(pixel[x], pixel[y], convertToRBG_INT(pixel[r], pixel[G], pixel[b]));

        }
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
                    bufferSurface.setRGB(pixel[x], pixel[y], convertToRBG_INT(pixel[r], pixel[G], pixel[b]));
                }
            }
            max_z = max_z - 1;
        }
    }

    /**
     * Gets gl render surface.
     *
     * @return the gl render surface
     */
    public static synchronized BufferedImage getGLRenderSurface() {
        return GLRenderSurface;
    }

    /**
     * unimplemented, no effective action.
     * Will transform a vertex using information about the camera, 3d point projection.
     *
     * @param point3d the point 3 d
     * @return the int [ ]
     */
    public static synchronized int[] cameraTransform(int[] point3d) {
        return point3d;
    }

    /**
     * Project a 3d point onto 2d plane using camera information.
     *
     * @param inputArray the input array
     * @param xPos       position of x value on array
     * @param yPos       position of y value on array
     * @param zPos       position of z value on array
     * @return the int [ ]
     */
    public static synchronized int[] cameraTransformArray(int[] inputArray, int xPos, int yPos, int zPos) {
        return inputArray;
    }

}