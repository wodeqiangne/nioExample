package com.leixie.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author xielei
 */
class Handler implements Runnable {
    SocketChannel sc;
    SelectionKey key;

    private final static int batchSize = 1024;
    ByteBuffer buffer = ByteBuffer.allocate(batchSize);

    private int status = read;

    private final static int read = 0;
    private final static int write = 1;

    public Handler(Selector selector, SocketChannel socketChannel) throws IOException {
        sc = socketChannel;
        socketChannel.configureBlocking(false);
        System.out.println("注册请求");
        key = socketChannel.register(selector, SelectionKey.OP_READ);
        key.attach(this);
        selector.wakeup();
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
            key.cancel();
            try {
                sc.close();
            } catch (IOException e1) {
                System.out.println("close exception" + e1.getMessage());
            }
            e.printStackTrace();
        }

    }

    public void read() throws IOException, InterruptedException {
        if (key.isValid() && key.isReadable()) {
            System.out.println("开始读取数据");
            buffer.clear();
            Thread.sleep(10000);
            int byteRead = sc.read(buffer);
            byte[] b = new byte[byteRead];
            if (byteRead > 0) {
                System.arraycopy(buffer.array(), 0, b, 0, byteRead);
                System.out.println("收到消息" + new String(b));
                status = write;
                key.interestOps(SelectionKey.OP_WRITE);
            } else {
                sc.close();
                key.cancel();
            }
        }
    }

    public void write() throws IOException{
        if (key.isValid() && key.isWritable()) {
            buffer.flip();
            int byteWrite = sc.write(buffer);
            if (byteWrite > 0) {
                status = read;
                key.interestOps(SelectionKey.OP_READ);
            } else {
                sc.close();
                key.cancel();
            }

        }
    }

}