package com.leixie.subReactorPool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Future;

/**
 * @author xielei
 */
class Handler implements Runnable {
    public SocketChannel socketChannel;
    public SelectionKey selectionKey;

    private final static int batchSize = 1024;
    ByteBuffer buffer = ByteBuffer.allocate(batchSize);

    public int status;

    public final static int read = 0;
    public final static int write = 1;

    public Handler(SocketChannel socketChannel, int type) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        status = type;
    }


    @Override
    public void run() {
        try {
            if (status == read) {
                read();
            } else if (status == write) {
                write();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void read() throws IOException, InterruptedException {
        if (selectionKey.isValid() && selectionKey.isReadable()) {
            System.out.println("开始读取数据");
            buffer.clear();
            int byteRead = socketChannel.read(buffer);
            byte[] b = new byte[byteRead];
            if (0 < byteRead) {
                System.arraycopy(buffer.array(), 0, b, 0, byteRead);
                System.out.println("收到消息11" + new String(b));
                status = write;
                selectionKey.interestOps(SelectionKey.OP_WRITE);
//                WorkThread workThread = new WorkThread(this, b);
//                WorkThreadPool.executorService.submit(workThread);
            } else {
                socketChannel.close();
                selectionKey.cancel();
            }
        }
    }

    public void write() throws IOException{
        if (selectionKey.isValid() && selectionKey.isWritable()) {
            buffer.flip();
            int byteWrite = socketChannel.write(buffer);
            if (byteWrite > 0) {
                status = read;
                System.out.println("echo完成");
                selectionKey.interestOps(SelectionKey.OP_READ);
            } else {
                socketChannel.close();
                selectionKey.cancel();
            }
        }
    }

}