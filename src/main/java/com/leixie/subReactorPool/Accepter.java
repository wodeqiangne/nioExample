package com.leixie.subReactorPool;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.stream.IntStream;

/**
 * @author xielei
 */
public class Accepter implements Runnable{
    ServerSocketChannel serverSocketChannel;
    Selector selector;
    SubReactor[] subReactors;
    int current = 0;

    private static final int round = 10;

    public Accepter(Selector selector, ServerSocketChannel serverSocketChannel, SubReactor[] subReactors) throws IOException {
        this.subReactors = subReactors;
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
        SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        key.attach(this);
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("Accepter 收到连接请求" + socketChannel.getRemoteAddress());
            SubReactor subReactor = subReactors[current];
            subReactor.register(new Handler(socketChannel, Handler.read));
            current = (current + 1) % round;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}