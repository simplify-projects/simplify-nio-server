package org.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 8080);
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream()
        ) {
            String request = "Request";
            output.write(request.getBytes());
            output.flush();

            byte[] inputData = new byte[2048];
            int length = input.read(inputData);
            String receivedMessage = new String(inputData, 0, length);
            System.out.println("Received message from server: " + receivedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}