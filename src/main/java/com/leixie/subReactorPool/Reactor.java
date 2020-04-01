package com.leixie.subReactorPool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xielei
 */
public class Reactor implements Runnable{
    private Selector selector;
    ServerSocketChannel socketChannel;

    public Reactor(int port, SubReactor[] subReactors) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        new Accepter(selector, serverSocketChannel, subReactors);
        this.selector = selector;
    }


    @Override
    public void run() {
        try {
            while (true) {
                int count = selector.select();
                if (count < 1) {
                    continue;
                }
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = readyKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    Runnable r = (Runnable) key.attachment();
                    r.run();
                    it.remove();
                }
            }
        } catch (Exception e) {
            try {
                selector.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
