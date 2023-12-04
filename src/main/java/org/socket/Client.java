package org.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080)) {
            // 클라이언트가 비동기적으로 데이터를 전송하기 위해 별도의 쓰레드 생성
            Thread senderThread = new Thread(() -> {
                try (OutputStream output = socket.getOutputStream()) {
                    String request = "Request";
                    output.write(request.getBytes());
                    output.flush();
                    // 서버에 데이터를 보내고 나서 바로 종료
                    socket.shutdownOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            senderThread.start();

            // 응답을 받기 위해 블록킹으로 데이터를 읽음
            try (InputStream input = socket.getInputStream()) {
                byte[] inputData = new byte[2048];
                int length = input.read(inputData);
                if (length > 0) {
                    String receivedMessage = new String(inputData, 0, length);
                    System.out.println("Received message from server: " + receivedMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 쓰레드 종료 대기
            try {
                senderThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
