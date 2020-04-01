package com.leixie.subReactorPool;

import java.util.concurrent.*;

/**
 * @author xielei
 */
public class WorkThreadPool {

    public static ExecutorService executorService = new ThreadPoolExecutor(10, 10, 1000, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(1000), Executors.defaultThreadFactory());

}
