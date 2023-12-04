package org.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) {
        try {
            // Selector 생성
            Selector selector = Selector.open();

            // ServerSocketChannel 생성 및 설정
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8080));
            // Non-blocking 모드로 설정
            serverSocketChannel.configureBlocking(false);

            // Selector에 ServerSocketChannel 등록
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 이벤트 발생을 대기
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                // 발생한 이벤트 처리
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        // Accept 이벤트 처리
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        // Read 이벤트 처리
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesRead = channel.read(buffer);
                        
                        if (bytesRead == -1) {
                            // 클라이언트가 연결을 종료한 경우
                            key.cancel();
                            channel.close();
                        } else if (bytesRead > 0) {
                            // 데이터를 받아온 경우
                            buffer.flip();
                            byte[] data = new byte[buffer.limit()];
                            buffer.get(data);
                            processReceivedData(data);

                            // 다시 Selector에 쓰기 이벤트 등록
                            key.interestOps(SelectionKey.OP_WRITE);
                        }
                    } else if (key.isWritable()) {
                        // Write 이벤트 처리
                        SocketChannel channel = (SocketChannel) key.channel();

                        // TODO: 응답 데이터 작성
                        byte[] responseData = generateResponseData();

                        ByteBuffer buffer = ByteBuffer.wrap(responseData);
                        channel.write(buffer);

                        // 다시 Selector에 읽기 이벤트 등록
                        key.interestOps(SelectionKey.OP_READ);
                    }

                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processReceivedData(byte[] data) {
        // TODO: 받은 데이터에 대한 비즈니스 로직 수행
        // ...
    }

    private static byte[] generateResponseData() {
        // TODO: 응답 데이터 생성
        // ...

        return new byte[0];
    }
}