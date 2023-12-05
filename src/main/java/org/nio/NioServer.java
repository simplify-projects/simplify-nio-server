package org.nio;

import org.socket.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NioServer {

    private static final Boolean NON_BLOCKING = false;

    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(NON_BLOCKING);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 이벤트 발생 대기
                int readyChannels = selector.select();
                // 만약 이벤트가 발생하지 않았다면 계속 대기
                if (readyChannels == 0) {
                    continue;
                }
                isExistEvents(selector);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void isExistEvents(Selector selector) throws IOException {
        // 발생한 이벤트 처리
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keys = selectedKeys.iterator();

        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            if (key.isAcceptable()) {
                acceptable(selector, key);
            } else if (key.isReadable()) {
                readable(key);
            } else if (key.isWritable()) {
                writeable(key);
            }
            keys.remove();
        }
    }

    private static void acceptable(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private static void readable(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = channel.read(buffer);

        // 클라이언트가 연결을 종료했다면
        if (bytesRead == -1) {
            key.cancel();
            channel.close();
        } else if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            processReceivedData(data);
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private static void writeable(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        byte[] responseData = new byte[2048];
        ByteBuffer buffer = ByteBuffer.wrap(responseData);
        try {
            channel.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    private static void processReceivedData(byte[] data) {
        String inputMessage = new String(data, StandardCharsets.UTF_8);
        Logger logger = Logger.getLogger("NIO-Server");
        logger.log(Level.INFO, inputMessage);
    }

}