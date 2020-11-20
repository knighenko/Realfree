package com.knighenko.realfree.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectServer {
    private final Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public ConnectServer(String host, int port) throws IOException {
        socket = new Socket(host, port);


    }
/**Метод для соединения с сервером, возвращает строку в виде Json
 * @param request - адрес URL*/

    public String readJsonString(String request) throws IOException {
        outputStream = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client connected to server");
        while (!socket.isOutputShutdown()) {
            outputStream.writeUTF(request);
            outputStream.flush();

            inputStream = new DataInputStream(socket.getInputStream());

            String message = inputStream.readUTF();
            System.out.println("Response from server is: " + message);
            inputStream.close();
            outputStream.close();
            socket.close();
            System.out.println("Client disconected!");
            return message;
        }
        return null;
    }
    public String readJsonString() throws IOException {
        outputStream = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client connected to server");
        while (!socket.isOutputShutdown()) {
            outputStream.writeUTF("request Url from Android");
            outputStream.flush();

            inputStream = new DataInputStream(socket.getInputStream());
            String message = inputStream.readUTF();
            System.out.println("Response from server is: " + message);
            inputStream.close();
            outputStream.close();
            socket.close();
            System.out.println("Client disconected!");
            return message;
        }
        return null;
    }



}

