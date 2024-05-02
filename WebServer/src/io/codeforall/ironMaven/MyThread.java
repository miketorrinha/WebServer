package io.codeforall.ironMaven;

import java.io.*;
import java.net.Socket;

public class MyThread implements Runnable {

    Socket socket;

    public MyThread(Socket socket) {

        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            handlerRequest(socket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void handlerRequest(Socket socket) throws IOException {

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String requestLine = in.readLine();

        System.out.println(requestLine);

        if (requestLine != null && requestLine.startsWith("GET")) {
            String filePath = requestLine.split("\\s")[1];
            File file = null;
            switch (filePath) {
                case "/index.html":
                    file = new File("resource/index.html");
                    break;
                case "/jola.png":
                    file = new File("resource/jola.png");
                    break;
                default:
                    file = new File("resource/404.html");
            }

            FileInputStream fileInputStream = new FileInputStream(file);

            long fileLength = file.length();
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String header;

            switch (filePath) {
                case "/index.html":
                    header = ("HTTP/1.0 200 Document Follows\r\n" +
                            "Content-Type: text/html; charset=UTF-8\r\n" +
                            "Content-Length: <file_byte_size> \r\n" +
                            "\r\n");
                    dataOutputStream.write(header.getBytes());
                    break;
                case "/jola.png":
                    header = ("HTTP/1.0 200 Document Follows\r\n" +
                            "Content-Type: image/png \r\n" +
                            "Content-Length: " + fileLength + "\r\n" +
                            "\r\n");
                    dataOutputStream.write(header.getBytes());
                    break;
                default:
                    header = ("HTTP/1.0 404 Not Found\r\n" +
                            "Content-Type: text/html; charset=UTF-8\r\n" +
                            "Content-Length: <file_byte_size> \r\n" +
                            "\r\n");
                    dataOutputStream.write(header.getBytes());
            }


            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }
            out.flush();
            fileInputStream.close();

        }
        in.close();
        out.close();

    }
}
