package gateway;

import com.google.gson.Gson;
import protocol.Request;
import protocol.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestHandler extends Thread{

    private Socket socket;
    public RequestHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(this.socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            String requestString = inputStream.readUTF();
            Gson gson =new Gson();
            Request request = gson.fromJson(requestString, Request.class);
            RequestRouter router = RequestRouter.getInstance();
            Response response = router.route(request);
            String responseString = gson.toJson(response);
            dataOutputStream.writeUTF(responseString);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
