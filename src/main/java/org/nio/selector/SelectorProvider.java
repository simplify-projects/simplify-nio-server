package org.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class SelectorProvider {

    private static final Boolean NON_BLOCKING = false;

    public static Selector generate(final int serverSocketPort) {
        Selector selector = null;
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(serverSocketPort));
            serverSocketChannel.configureBlocking(NON_BLOCKING);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return selector;
    }
}
