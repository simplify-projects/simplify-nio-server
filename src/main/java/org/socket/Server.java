package org.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (
                final ServerSocket serverSocket = new ServerSocket(8080);
                final Socket socket = serverSocket.accept();
                final InputStream input = socket.getInputStream();
                final OutputStream output = socket.getOutputStream();
        ) {
            byte[] inputDataLength = new byte[2048];
            int length = input.read(inputDataLength);
            String inputMessage = new String(inputDataLength, 0, length);
            System.out.println("inputMessage from client = " + inputMessage);

            String outputMessage = "Response";
            output.write(outputMessage.getBytes());
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
