package gateway;

import controller.Controller;
import controller.NodeController;
import protocol.Request;
import protocol.Response;

import java.net.ServerSocket;
import java.net.Socket;

public class RequestRouter {
    private static volatile RequestRouter instance;

    private RequestRouter() {

    }

    synchronized public static RequestRouter getInstance() {
        if (instance == null)
            instance = new RequestRouter();

        return instance;
    }

    public Response route(Request request) {
        Controller controller = NodeController.getInstance();
        return controller.handleRequest(request);

    }
}
