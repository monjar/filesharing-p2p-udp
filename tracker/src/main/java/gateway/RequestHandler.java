package gateway;

import com.google.gson.Gson;
import protocol.Request;
import protocol.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestHandler extends Thread {

    private final Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream dataOutputStream;
    private final Gson gson = new Gson();

    public RequestHandler(Socket socket) {
        this.socket = socket;
        try {
            this.inputStream = new DataInputStream(this.socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Request request = getRequest();
            Response response = getResponse(request);
            sendResponse(response);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void sendResponse(Response response) throws IOException {
        String responseString = gson.toJson(response);
        this.dataOutputStream.writeUTF(responseString);
    }

    private Response getResponse(Request request) {
        RequestRouter router = RequestRouter.getInstance();
        request.setAddress(this.socket.getInetAddress().getHostAddress() + ":" + request.getPort());
        return router.route(request);
    }

    private Request getRequest() throws IOException {
        String requestString = this.inputStream.readUTF();
        System.out.println(requestString);
        return this.gson.fromJson(requestString, Request.class);
    }
}
