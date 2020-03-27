package com.leixie;

import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author xielei
 */
public class NonBlockingIO {


    @Test
    public void startServer() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress("localhost", 8080));
        System.out.println("listening on port  " + 8080);
        try {
            while(true) {
                //不会阻塞
                SocketChannel socketChannel = serverChannel.accept();
                if (socketChannel == null) {
                    continue;
                }
                ByteBuffer buf = ByteBuffer.allocate(1024);
                int bytesRead;
                while( (bytesRead = socketChannel.read(buf)) != -1) {
                    buf.flip();
                    byte[] data = new byte[bytesRead];
                    buf.get(data);
                    System.out.println(new String(data));
                    PrintWriter out = new PrintWriter(socketChannel.socket().getOutputStream(), true);
                    out.println(new String(data));
                    buf.flip();
                }
            }
        } finally {
            serverChannel.close();
        }
    }
}
