package com.leixie.subReactorPool;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * @author xielei
 */
public class SubReactor implements Runnable {

    private ArrayBlockingQueue<Handler> events = new ArrayBlockingQueue<>(1000);
    private Selector selector;

    public SubReactor() throws IOException {
        this.selector = Selector.open();
    }


    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                Handler handler;
                while ((handler = events.poll()) != null) {
                    //从队列中把所有Handler拿出来，注册到当前selector上
                    handler.selectionKey = handler.socketChannel.register(selector, SelectionKey.OP_READ);
                    handler.selectionKey.attach(handler);
                }
                int count = selector.select();
                if (count < 1) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator it = selectionKeys.iterator();
                while(it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    it.remove();
                    dispatch(key);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(Handler handler) {
        events.offer(handler);
        selector.wakeup();
    }

    public void dispatch(SelectionKey key) {
        Runnable r =(Runnable) key.attachment();
        r.run();
    }
}
