package io.codeforall.ironMaven;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int portNumber;
    private Socket clientSocket;
    private String message;

    private byte[] receivedRequest;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();

    }

    public void startServer() throws IOException {
        setPortNumber();
        ServerSocket serverSocket = new ServerSocket(portNumber);

        ExecutorService pool = Executors.newFixedThreadPool(10);

        while (true) {

            Socket clientSocket = setClientSocket(serverSocket);

            //Thread thread = new Thread(new MyThread(clientSocket));

            //thread.start();

            pool.submit(new MyThread(clientSocket));

        }
    }

    public void setPortNumber() throws IOException {
        System.out.println("Port number?");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        this.portNumber = 8080; /*Integer.parseInt(bReader.readLine());*/
        System.out.println(portNumber);
    }

    public String appendLineToString(String originalString, String lineToAppend) {
        return originalString + "\n" + lineToAppend;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Socket setClientSocket(ServerSocket serverSocket) throws IOException {

        return serverSocket.accept();

    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }
}
