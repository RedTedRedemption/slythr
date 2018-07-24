package slythr;



import java.util.ArrayList;
import java.util.concurrent.Callable;

import static slythr.Render.*;


public class RenderThread implements Callable<Integer> {

    public Vertex_Array vertex_array;
    Thread thread;
    private ShaderProgram activeProgram;
    private int[] startingBuffer;
    public boolean free = true;
    public RenderThread(Vertex_Array vertexArray) {
        vertex_array = vertexArray;
    }
    public static ArrayList<Integer> points = new ArrayList<>();
    private int[] horizLineStartingBuffer = new int[7];
    private int[] horizLinePixelBuffer = new int[7];


    private float fillBottomFlatTriangle_invslope0;
    private float fillBottomFlatTriangle_invslope1;
    private float fillBottomFlatTriangle_invslopez0;
    private float fillBottomFlatTriangle_invslopez1;
    private float fillBottomFlatTriangle_curx0;
    private float fillBottomFlatTriangle_curx1;

    private float fillTopFlatTriangle_invslope1;
    private float fillTopFlatTriangle_invslope0;
    private float fillTopFlatTriangle_invslopez1;
    private float fillTopFlatTriangle_curx0;
    private float fillTopFlatTriangle_curx1;




    public Integer call() throws Exception{
        claim();
        render(vertex_array);
        release();
        return 1;
    }

    private void fillBottomFlatTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int r, int g, int b, Vertex_Array vertex_array) {

        //      0
        //     / \
        //    /   \
        //   /     \
        //  1-------2

        fillBottomFlatTriangle_invslope0 = (float) (x1 - x0) / (float) (y1 - y0);
        fillBottomFlatTriangle_invslope1 = (float) (x2 - x0) / (float) (y2 - y0);

        fillBottomFlatTriangle_curx0 = x0;
        fillBottomFlatTriangle_curx1 = x0;



        for (int scanlineY = x0; scanlineY <= y1; scanlineY++) {
            drawHorizLine((int) fillBottomFlatTriangle_curx0, scanlineY, (int) fillBottomFlatTriangle_curx1, scanlineY, r, g, b, vertex_array);
            //drawHorizLine((int) fillBottomFlatTriangle_curx0, (int) fillBottomFlatTriangle_curx1, scanlineY, 255, 255, 255);
            //Render.bufferSurface.setRGB((int) fillBottomFlatTriangle_curx0, scanlineY, Render.convertToRBG_INT(255, 255, 255));
            fillBottomFlatTriangle_curx0 = fillBottomFlatTriangle_curx0 + fillBottomFlatTriangle_invslope0;
            fillBottomFlatTriangle_curx1 = fillBottomFlatTriangle_curx1 + fillBottomFlatTriangle_invslope1;
        }

    }

    private void fillTopFlatTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int r, int g, int b, Vertex_Array vertex_array) {
        //  0-------1
        //   \     /
        //    \   /
        //     \ /
        //      2

        fillTopFlatTriangle_invslope0 = (float) (x2 - x0) / (float) (y2 - y0);
        fillTopFlatTriangle_invslope1 = (float) (x2 - x1) / (float) (y2 - y1);

        fillTopFlatTriangle_curx0 = x2;
        fillTopFlatTriangle_curx1 = x2;

        for (int scanlineY = y2; scanlineY > y0; scanlineY--) {
            drawHorizLine((int) fillTopFlatTriangle_curx0, scanlineY, (int) fillTopFlatTriangle_curx1, scanlineY, r, g, b, vertex_array);
            fillTopFlatTriangle_curx0 = fillTopFlatTriangle_curx0 + fillTopFlatTriangle_invslope0;
            fillTopFlatTriangle_curx1 = fillTopFlatTriangle_curx1 + fillTopFlatTriangle_invslope1;
        }


    }

    public void setVertex_array(Vertex_Array Vertex_array) {
        vertex_array = Vertex_array;
    }


    public void quickRun(Vertex_Array vertex_array) {
        render(vertex_array);
    }

    public void render(Vertex_Array vertex_array) {
        //Vertex_Array blankVertexArray = new Vertex_Array(1, 7, defaultProgram, new int[] {0, 0, 0, 0, 0, 0, 0});
        activeProgram = vertex_array.program;
        if (activeProgram.pipeline[VERTEX_SHADER] == null || activeProgram.pipeline[GEOMETRY_SHADER] == null || activeProgram.pipeline[FRAGMENT_SHADER] == null) {
            Engine.throwFatalError(new SlythrError("ERROR: INCOMPLETE SHADER PROGRAM!"));
        }
        if (!vertex_array.enabled) {
            return;
        }
        if (vertex_array.drawAction == Render.DRAW_LINE) {
            for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
                startingBuffer = Render.makeArtifact(vertex_array, cursor);
                startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
                //camera transform here
                drawLine(vertex_array.vertexArray[0], vertex_array.vertexArray[1], vertex_array.vertexArray[2], vertex_array.vertexArray[3], vertex_array.vertexArray[4], vertex_array.vertexArray[5], vertex_array.vertexArray[6], vertex_array);
            }
        }
        if (vertex_array.drawAction == Render.FILL_TRIANGLE) {
            activeProgram = vertex_array.program;
            for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
                startingBuffer = Render.makeArtifact(vertex_array, cursor);
                startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
                //camera transform goes here
                drawTriangle(startingBuffer, vertex_array);
            }
        }
        if (vertex_array.drawAction == Render.FILL_RECT) {
            //drawRect(vertex_array);
            activeProgram = vertex_array.program;

            for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
//            System.out.println(cursor);
//            for (int i : makeArtifact(vertex_array, cursor)) {
//                System.out.print(i);
//                System.out.print(", ");
//            }
                startingBuffer = Render.makeArtifact(vertex_array, cursor);
                startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
                startingBuffer = Render.cameraTransformArray(startingBuffer, 0, 1, 2);
                int pointArtifact[] = new int[vertex_array.stride];
                for (int y = startingBuffer[1]; y <= startingBuffer[1] + startingBuffer[4]; y++) {
                    for (int x = startingBuffer[0]; x <= startingBuffer[0] + startingBuffer[3]; x++) {
                        pointArtifact[0] = x;
                        pointArtifact[1] = y;
                        pointArtifact[3] = startingBuffer[5];
                        pointArtifact[4] = startingBuffer[6];
                        pointArtifact[5] = startingBuffer[7];
//                        for (int pointArtifactFiller = 2; pointArtifactFiller < vertex_array.stride; pointArtifactFiller++) {
//                            pointArtifact[pointArtifactFiller] = startingBuffer[pointArtifactFiller];
//                        }

                        if (!EngineSettings.HARDWARE_ACCELERATION) {
                            pointArtifact = activeProgram.pipeline[GEOMETRY_SHADER].shader(pointArtifact);
                        } else {
                            for (int point : pointArtifact) {
                                points.add(point);
                            }

                        }

                        //pixel position based culling
                        if (Render.shouldDrawPixel(pointArtifact[0], pointArtifact[1])) {
                            pointArtifact = activeProgram.pipeline[FRAGMENT_SHADER].shader(pointArtifact);
                            Render.putPixelToBuffer(pointArtifact[0], pointArtifact[1], 3, pointArtifact[3], pointArtifact[4], pointArtifact[5]);

                        }
                    }
                }
            }

//            final int[] dispatchPoints = new int[points.size()];
//            for (int i = 0; i < points.size(); i++) {
//                dispatchPoints[i] = points.get(i);
//            }
//            Kernel kernel = new Kernel() {
//                @Override
//                public void run() {
//                    int id = getGlobalId();
//                    int[] pnt = {dispatchPoints[id * 100], dispatchPoints[id * 100 + 1], dispatchPoints[id * 100 + 2], dispatchPoints[id * 100 + 3], dispatchPoints[id * 100 + 4]};
//                    pnt = vertex_array.program.pipeline[Render.GEOMETRY_SHADER].shader(pnt);
//                    pnt = vertex_array.program.pipeline[Render.FRAGMENT_SHADER].shader(pnt);
//                    dispatchPoints[id * 100] = pnt[0];
//                    dispatchPoints[id * 100 + 1] = pnt[1];
//                    dispatchPoints[id * 100 + 2] = pnt[2];
//                    dispatchPoints[id * 100 + 3] = pnt[3];
//                    dispatchPoints[id * 100 + 4] = pnt[4];
//
//
//                }
//            };
//
//            System.out.println(dispatchPoints[0]);


        }
        if (vertex_array.drawAction == Render.DRAW_POINT) {
            //drawPoint(vertex_array);
            for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
                startingBuffer = Render.makeArtifact(vertex_array, cursor);
                startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
                int pointArtifact[] = new int[100];
                System.arraycopy(startingBuffer, 0, pointArtifact, 0, vertex_array.stride);
                pointArtifact = activeProgram.pipeline[GEOMETRY_SHADER].shader(pointArtifact);
                if (Render.shouldDrawPixel(pointArtifact[0], pointArtifact[1])) {
                    try {
                        pointArtifact = activeProgram.pipeline[FRAGMENT_SHADER].shader(pointArtifact);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("failed to render");
                    }
                    Render.putPixelToBuffer(pointArtifact[0], pointArtifact[1], 3, pointArtifact[2], pointArtifact[3], pointArtifact[4]);
//                System.out.println("drawing point");
//                try {
//                    bufferSurface.setRGB(pointArtifact[0], pointArtifact[1], getRGB(pointArtifact[2], pointArtifact[3], pointArtifact[4]));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println("point rendered");
                }
            }
        }
    }

    private void drawPoint(int x, int y, int r, int g, int b, Vertex_Array vertex_array) {


    }

    //x,y,z, x,y,z, x,y,z, r, g, b
    //0,1,2, 3,4,5, 6,7,8, 9,10,11

    private void drawTriangle(int[] vertex, Vertex_Array vertex_array) {

//        if (vertex[4] < vertex[1]) {
//            Utils.printArray(vertex);
//            Utils.swap(2, 5, vertex_array.vertexArray);
//            Utils.printArray(vertex);
//
//
//        }
        if (vertex[4] == vertex[7]) {
            fillBottomFlatTriangle(vertex[0], vertex[1], vertex[3], vertex[4], vertex[6], vertex[7], vertex[9], vertex[10], vertex[11], vertex_array);
            return;
        }
        if (vertex[1] == vertex[4]) {
            fillTopFlatTriangle(vertex[0], vertex[1], vertex[3], vertex[4], vertex[6], vertex[7], vertex[9], vertex[10], vertex[11], vertex_array);
            return;
        }

        int v4x = (int) (vertex[0] + ((float) (vertex[4] - vertex[1]) / (float) (vertex[7] - vertex[1])) * (vertex[6] - vertex[0]));
        int v4y = vertex[4];

        fillBottomFlatTriangle(vertex[0], vertex[1], vertex[3], vertex[4], v4x, v4y, vertex[9], vertex[10], vertex[11], vertex_array);
        fillBottomFlatTriangle(vertex[3], vertex[4], v4x, v4y, vertex[6], vertex[7], vertex[9], vertex[10], vertex[11], vertex_array);




    }

    public void drawLine(int x,int y,int x2, int y2, int r, int g, int b, Vertex_Array vertex_array) {
        int w = x2 - x ;
        int h = y2 - y ;
        int[] pixelBuffer = new int[6];
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            pixelBuffer[0] = x;
            pixelBuffer[1] = y;
            pixelBuffer[2] = 0;
            pixelBuffer[3] = r;
            pixelBuffer[4] = g;
            pixelBuffer[5] = b;
            pixelBuffer = vertex_array.pumpPixel(pixelBuffer);
            putPixelToBuffer(pixelBuffer[0], pixelBuffer[1], pixelBuffer[2], pixelBuffer[2], pixelBuffer[3], pixelBuffer[4]);
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }
    }

    private void drawPoint(Vertex_Array vertex_array) {
        activeProgram = vertex_array.program;
        for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
            startingBuffer = Render.makeArtifact(vertex_array, cursor);
            startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
            int pointArtifact[] = new int[100];
            System.arraycopy(startingBuffer, 0, pointArtifact, 0, vertex_array.stride);
            pointArtifact = activeProgram.pipeline[GEOMETRY_SHADER].shader(pointArtifact);
            if (Render.shouldDrawPixel(pointArtifact[0], pointArtifact[1])) {
                try {
                    pointArtifact = activeProgram.pipeline[FRAGMENT_SHADER].shader(pointArtifact);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("failed to render");
                }
                Render.putPixelToBuffer(pointArtifact[0], pointArtifact[1], 3, pointArtifact[2], pointArtifact[3], pointArtifact[4]);
//                System.out.println("drawing point");
//                try {
//                    bufferSurface.setRGB(pointArtifact[0], pointArtifact[1], getRGB(pointArtifact[2], pointArtifact[3], pointArtifact[4]));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println("point rendered");
            }
        }
    }

    private void drawRect(Vertex_Array vertex_array) {
        activeProgram = vertex_array.program;

        for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
//            System.out.println(cursor);
//            for (int i : makeArtifact(vertex_array, cursor)) {
//                System.out.print(i);
//                System.out.print(", ");
//            }
            startingBuffer = Render.makeArtifact(vertex_array, cursor);
            startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
            int pointArtifact[] = new int[100];
            for (int y = startingBuffer[1]; y <= startingBuffer[1] + startingBuffer[3]; y++) {
                for (int x = startingBuffer[0]; x <= startingBuffer[0] + startingBuffer[2]; x++) {
                    pointArtifact[0] = x;
                    pointArtifact[1] = y;
                    System.arraycopy(startingBuffer, 2, pointArtifact, 2, vertex_array.stride - 2);
//                    pointArtifact[2] = startingBuffer[2];
//                    pointArtifact[3] = startingBuffer[3];
//                    pointArtifact[4] = startingBuffer[4];
//                    pointArtifact[5] = startingBuffer[5];
//                    pointArtifact[6] = startingBuffer[6];
                    pointArtifact = activeProgram.pipeline[GEOMETRY_SHADER].shader(pointArtifact);

                    //pixel position based culling
                    if (Render.shouldDrawPixel(pointArtifact[0], pointArtifact[1])) {
                        pointArtifact = activeProgram.pipeline[FRAGMENT_SHADER].shader(pointArtifact);
                        Render.putPixelToBuffer(pointArtifact[0], pointArtifact[1], 3, pointArtifact[4], pointArtifact[5], pointArtifact[6]);

                    }

                }
            }
        }
    }

//    private void drawHorizLine(int x0, int x1, int y, int z, int r, int g, int b, Vertex_Array vertex_array) {
//        horizLineStartingBuffer[0] = x0;
//        horizLineStartingBuffer[1] = x1;
//        horizLineStartingBuffer[2] = y;
//        horizLineStartingBuffer[3] = z;
//        horizLineStartingBuffer[4] = r;
//        horizLineStartingBuffer[5] = g;
//        horizLineStartingBuffer[6] = b;
//
//        for (int x = x0; x <= x1; x++) {
//            horizLinePixelBuffer = vertex_array.program.pipeline[Render.GEOMETRY_SHADER].shader(horizLineStartingBuffer);
//            horizLinePixelBuffer = vertex_array.program.pipeline[Render.FRAGMENT_SHADER].shader(horizLinePixelBuffer);
//            //Utils.printArray(horizLinePixelBuffer);
//            Render.putPixelToBuffer(horizLinePixelBuffer[0], horizLinePixelBuffer[2], horizLinePixelBuffer[3], horizLinePixelBuffer[4], horizLinePixelBuffer[5], horizLinePixelBuffer[6]);
//        }
//    }

    private void drawHorizLine(int x0, int x1, int y, int z, int r, int g, int b, Vertex_Array vertex_array) {
        int[] intRegister = {x0, y, r, g, b};
        for (int x = x0; x <= x1; x++) {
            intRegister[0] = x;
            intRegister = vertex_array.pumpPixel(intRegister);
            Render.putPixelToBuffer(intRegister[0], intRegister[1], 0, intRegister[2], intRegister[3], intRegister[4]);
        }
    }


    private void claim() {
        free = false;
    }

    private void release() {
        free = true;
    }

}
