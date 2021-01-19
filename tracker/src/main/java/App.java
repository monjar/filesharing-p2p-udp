import gateway.Server;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.listen(8080);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
