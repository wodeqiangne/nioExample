package com.leixie.reactor;


import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author xielei
 */
public class Accepter implements Runnable{
    ServerSocketChannel serverSocketChannel;
    Selector key;

//    Selector[] subReactorPool;
//
//    private static int subReactorSize = 10;

    public Accepter(ServerSocketChannel serverSocketChannel, Selector key) {
        this.serverSocketChannel = serverSocketChannel;
        this.key = key;

    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("收到连接请求" + socketChannel.getRemoteAddress());
            new Handler(key, socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}