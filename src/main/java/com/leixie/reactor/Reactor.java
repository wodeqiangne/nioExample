package com.leixie.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xielei
 */
public class Reactor implements Runnable{

//    ExecutorService boss = Executors.newFixedThreadPool(1);
//    ExecutorService worker = Executors.newFixedThreadPool(10);
    Selector selector;
    ServerSocketChannel serverSocket;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey key = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        key.attach(new Accepter(serverSocket, selector));
    }
    public static void main(String[] args) throws IOException {
        new Reactor(8080).run();
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selections = selector.selectedKeys();
                Iterator it = selections.iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    if (!key.isValid()) {
                        continue;
                    }
                    it.remove();
                    dispatch(key);

                }
                selections.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                selector.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    public void dispatch(SelectionKey key){
        Runnable r = (Runnable) key.attachment();
        r.run();
    }

}
