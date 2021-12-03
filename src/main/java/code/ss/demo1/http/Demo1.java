package code.ss.demo1.http;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Demo1 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket accept = serverSocket.accept();
            LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(accept.getInputStream()));
            String line = null;
            while ((line = lineNumberReader.readLine()) != null) {
                System.out.println(line);
                if (lineNumberReader.getLineNumber() == 1) {

                }else{
                    if (line.isEmpty()) {
                        doResponse(accept);
                    }
                }
            }
        }
    }

    public static void doResponse(Socket socket) throws IOException {
        String msg = "I can't find file\r\n";
        String response = "HTTP/1.1 200 OK\r\n";
        response += "Server: SS server/1.0\r\n";
        response += "Content-length: " + (msg.length() - 4) + "\r\n";
        response += "\r\n";
        response += msg;
        socket.getOutputStream().write(response.getBytes());
        socket.getOutputStream().flush();

    }

}
