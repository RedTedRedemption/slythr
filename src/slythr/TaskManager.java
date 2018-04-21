package slythr;

import javax.security.auth.callback.Callback;
import java.lang.reflect.Type;
import java.util.concurrent.*;

public class TaskManager {

    private static ThreadPoolExecutor threadPool;
    
    public static void init() {
        System.out.println("===========================");
        System.out.print("Initializing task manager...");
        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(WindowHint.windowHint_TaskManager_thread_count.getValue());
        System.out.println("done");
        Engine.addSubRoutine(new SubRoutine() {
            @Override
            public void routine() {
                if (threadPool.getActiveCount() == 0) {
                    threadPool.purge();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("===========================");
    }
    
    public static void dispatch(SlythrAction action) {
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                action.execute();
            }
        });
    }
    
    public static Object dispatch(Callable action) throws ExecutionException, InterruptedException {
        Future future = threadPool.submit(action);

        return future.get();
    }
}
