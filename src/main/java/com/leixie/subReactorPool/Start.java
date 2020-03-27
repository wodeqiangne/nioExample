package com.leixie.subReactorPool;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author xielei
 */
public class Start {

    private static final int round = 10;
    private static final ExecutorService es = new ThreadPoolExecutor(10, 10, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory());

    public static void main(String[] args) throws IOException {
        SubReactor[] subReactors = new SubReactor[round];
        for(int i = 0; i < round; i++) {
            subReactors[i] = new SubReactor();
            es.submit(subReactors[i]);
        }
        Reactor reactor = new Reactor(8080, subReactors);
        reactor.run();
    }
}
