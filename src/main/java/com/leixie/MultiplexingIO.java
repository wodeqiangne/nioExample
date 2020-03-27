package com.leixie;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xielei
 */
public class MultiplexingIO {

    private Selector selector;

    private ByteBuffer buf;

    @Test
    public void startServer() throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        try {
            socketChannel.configureBlocking(false);
            socketChannel.bind(new InetSocketAddress("localhost", 8080));
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                int readySocketCount = selector.select();
                if (readySocketCount < 1) {
                    continue;
                }
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = readyKeys.iterator();
                while(it.hasNext()) {
                    SelectionKey key = it.next();

                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable() && key.isWritable()) {
                        process(key);
                    }
                    it.remove();
                }
            }
        } finally {
            socketChannel.close();
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        System.out.println("connected to " + socketChannel.getRemoteAddress());
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

    }

    private void process(SelectionKey key) throws IOException{
        SocketChannel socketChannel = (SocketChannel) key.channel();
        buf = ByteBuffer.allocate(1024);
        int bytesRead;
        bytesRead = socketChannel.read(buf);
        if (bytesRead < 0) {
            socketChannel.close();
            key.cancel();
            return;
        } else {
            byte[] data = new byte[bytesRead];
            System.arraycopy(buf.array(), 0, data, 0, bytesRead);
            System.out.println(new String(data));
        }
        buf.flip();
        socketChannel.write(buf);

    }
}
