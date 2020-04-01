package com.leixie.subReactorPool;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author xielei
 */
public class Starter {

    private static final int ROUND = 10;
    private static final ExecutorService executorService = new ThreadPoolExecutor(10, 10, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory());

    public static void main(String[] args) throws IOException {
        SubReactor[] subReactors = new SubReactor[ROUND];
        for(int i = 0; i < ROUND; i++) {
            subReactors[i] = new SubReactor();
            executorService.submit(subReactors[i]);
        }
        Reactor reactor = new Reactor(8080, subReactors);
        reactor.run();
    }
}
