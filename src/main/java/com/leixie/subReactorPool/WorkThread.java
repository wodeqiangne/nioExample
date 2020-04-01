package com.leixie.subReactorPool;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author xielei
 */
public class WorkThread implements Runnable{

    Handler handler;
    byte[] data;

    public WorkThread(Handler handler, byte[] data) {
        this.handler = handler;
        this.data = data;
    }

    @Override
    public void run() {
        //do something with data
        this.handler.status = Handler.write;
        this.handler.selectionKey.interestOps(SelectionKey.OP_WRITE);

    }
}
