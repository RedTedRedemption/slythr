package slythr;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class SKernel {

   // private static RenderThread[] threadPool = new RenderThread[EngineSettings.RENDER_THREADS];
    private static ArrayList<Future<Integer>> futures = new ArrayList<>();
    private static RenderThread[] workerThreadPool = new RenderThread[EngineSettings.RENDER_THREADS];
    private static boolean dispatchComplete = false;
    private static boolean initiated = false;
    private static int taskedThreads = 0;
    private static Object mon = new Object();
    private static ThreadPoolExecutor threadPool;

    public static void init() {

        System.out.print("Initializing Kernel...");
//        for (int i = 0; i < threadPool.length; i++) {
//           // threadPool[i] = new RenderThread(null);
//          //  threadPool[i].start();
//        }

        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(EngineSettings.RENDER_THREADS);

        for (int i = 0; i < EngineSettings.RENDER_THREADS; i++) {
            workerThreadPool[i] = new RenderThread(null);
        }
        initiated = true;
        System.out.println("done");
    }

    public static void dispatch(Vertex_Array vertex_array) {
        if (initiated) {
            if (vertex_array.enabled) {
//            while (true) {
//                for (RenderThread worker : workerThreadPool) {
//                    if (worker.free) {
//                        worker.setVertex_array(vertex_array);
//                        Future<Integer> future = threadPool.submit(worker);
//                        System.out.println(futures.size());
//                        futures.add(future);
//                        return;
//                    }
//                }
//            }

                Future<Integer> future = threadPool.submit(new RenderThread(vertex_array));
                futures.add(future);
                //System.out.println(futures.size());
            }
        } else {
            Engine.throwFatalError(new SlythrError("Could not dispatch vertex render job - kernel not initialized"));
        }
    }

    public static void joinAll() {
        for (Future<Integer> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        futures.clear();
    }

}
