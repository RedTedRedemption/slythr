package slythr;

import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class Render {

    public static Shader[] pipeline = new Shader[3];
    private static int[] startingBuffer;
    private static Color brush = new Color(255, 255, 255);
    public static BufferedImage GLRenderSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
    public static BufferedImage blankSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
    public static BufferedImage bufferSurface = new BufferedImage(Engine.width, Engine.height, BufferedImage.TYPE_INT_ARGB);
    public static Graphics g;

    public static final int VERTEX_SHADER = 0;
    public static final int GEOMETRY_SHADER = 1;
    public static final int FRAGMENT_SHADER = 2;

	public static void render(Vertex_Array vertex_array) {
	    bufferSurface.setData(blankSurface.getRaster());
	    if (vertex_array.drawAction == Game_Window.FILL_RECT) {
	        for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
//            System.out.println(cursor);
//            for (int i : makeArtifact(vertex_array, cursor)) {
//                System.out.print(i);
//                System.out.print(", ");
//            }

                startingBuffer = makeArtifact(vertex_array, cursor);
                startingBuffer = pipeline[VERTEX_SHADER].shader(startingBuffer);
                int pointArtifact[] = new int[vertex_array.stride];
                for (int y = startingBuffer[1]; y <= startingBuffer[1] + startingBuffer[3]; y++) {
                    for (int x = startingBuffer[0]; x <= startingBuffer[0] + startingBuffer[2]; x++) {
                        pointArtifact[0] = x;
                        pointArtifact[1] = y;
                        pointArtifact[2] = startingBuffer[2];
                        pointArtifact[3] = startingBuffer[3];
                        pointArtifact[4] = startingBuffer[4];
                        pointArtifact[5] = startingBuffer[5];
                        pointArtifact[6] = startingBuffer[6];
                       // pointArtifact = {x, y, startingBuffer[2], startingBuffer[3], startingBuffer[4], startingBuffer[5], startingBuffer[6]};
                        pointArtifact = pipeline[GEOMETRY_SHADER].shader(pointArtifact);
                        pointArtifact = pipeline[FRAGMENT_SHADER].shader(pointArtifact);
                        //brush = new Color(pointArtifact[4], pointArtifact[5], pointArtifact[6]);
                        try {
                            bufferSurface.setRGB(pointArtifact[0], pointArtifact[1], getRGB(pointArtifact[4], pointArtifact[5], pointArtifact[6]));
                        } catch (Exception e) {
                            //pass;
                        }
//  g.setColor(new Color(pointArtifact[4], pointArtifact[5], pointArtifact[6]));
//                        g.fillRect(pointArtifact[0], pointArtifact[1], pointArtifact[2], pointArtifact[3]);
                    }
                }
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

}