package gateway;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private boolean isServerUp = true;

    public Server() {

    }

    private ServerSocket serverSocket;

    public void listen(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        try {
            while (isServerUp) {
                Socket clientSocket = this.serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(clientSocket);
                requestHandler.start();
            }
        } catch (SocketException socketException) {
            System.out.println("Server closed.");
        }
    }

    public synchronized void stop() {
        try {
            this.isServerUp = false;
            this.serverSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
