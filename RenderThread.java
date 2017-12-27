package slythr;

import com.aparapi.Kernel;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static slythr.Render.FRAGMENT_SHADER;
import static slythr.Render.GEOMETRY_SHADER;
import static slythr.Render.VERTEX_SHADER;


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


    public Integer call() throws Exception{
        claim();
        render(vertex_array);
        release();
        return 1;
    }
//            try {
//                notified = false;
//                synchronized (mon) {
//                    while (!notified) {
//                        mon.wait();
//                        notified = true;
//                        System.out.println("not notified");
//                    }
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            free = false;
//            try {
//                System.out.println("rendering");
//                render(vertex_array);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            free = true;
//            System.out.println("thread done rendering");
//        }

    public void setVertex_array(Vertex_Array Vertex_array) {
        vertex_array = Vertex_array;
    }


    public void quickRun(Vertex_Array vertex_array) {
        render(vertex_array);
    }

    public void render(Vertex_Array vertex_array) {
        activeProgram = vertex_array.program;
        if (activeProgram.pipeline[VERTEX_SHADER] == null || activeProgram.pipeline[GEOMETRY_SHADER] == null || activeProgram.pipeline[FRAGMENT_SHADER] == null) {
            Engine.throwFatalError(new SlythrError("ERROR: INCOMPLETE SHADER PROGRAM!"));
        }
        if (!vertex_array.enabled) {
            return;
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
                int pointArtifact[] = new int[vertex_array.stride];
                for (int y = startingBuffer[1]; y <= startingBuffer[1] + startingBuffer[4]; y++) {
                    for (int x = startingBuffer[0]; x <= startingBuffer[0] + startingBuffer[3]; x++) {
                        pointArtifact[0] = x;
                        pointArtifact[1] = y;
                        for (int pointArtifactFiller = 2; pointArtifactFiller < vertex_array.stride; pointArtifactFiller++) {
                            pointArtifact[pointArtifactFiller] = startingBuffer[pointArtifactFiller];
                        }

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
                            Render.putPixelToBuffer(pointArtifact[0], pointArtifact[1], 3, pointArtifact[4], pointArtifact[5], pointArtifact[6]);

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
                for (int pointArtifactFiller = 0; pointArtifactFiller < vertex_array.stride; pointArtifactFiller++) {
                    pointArtifact[pointArtifactFiller] = startingBuffer[pointArtifactFiller];
                }
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

    private void drawPoint(Vertex_Array vertex_array) {
        activeProgram = vertex_array.program;
        for (int cursor = 0; cursor < vertex_array.vertexArray.length - 1; cursor = cursor + vertex_array.stride) {
            startingBuffer = Render.makeArtifact(vertex_array, cursor);
            startingBuffer = activeProgram.pipeline[VERTEX_SHADER].shader(startingBuffer);
            int pointArtifact[] = new int[100];
            for (int pointArtifactFiller = 0; pointArtifactFiller < vertex_array.stride; pointArtifactFiller++) {
                pointArtifact[pointArtifactFiller] = startingBuffer[pointArtifactFiller];
            }
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
                    if (Render.shouldDrawPixel(pointArtifact[0], pointArtifact[1])) {
                        pointArtifact = activeProgram.pipeline[FRAGMENT_SHADER].shader(pointArtifact);
                        Render.putPixelToBuffer(pointArtifact[0], pointArtifact[1], 3, pointArtifact[4], pointArtifact[5], pointArtifact[6]);

                    }

                }
            }
        }
    }

    private void claim() {
        free = false;
    }

    private void release() {
        free = true;
    }

}
