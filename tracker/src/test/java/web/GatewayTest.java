package web;

import controller.Controller;
import controller.NodeController;
import dto.NodeListDto;
import exceptions.InvalidRequestMethodException;
import gateway.Server;
import org.junit.Test;
import protocol.Request;
import protocol.RequestMethod;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;

public class GatewayTest {
    @Test
    public void nodeControllerGetSuccess(){
        Controller controller = NodeController.getInstance();
        Request request = new Request();
        request.setBody(null);
        request.setMethod(RequestMethod.GET);
        request.setTargetRoute("all");
        controller.handleRequest(request);
    }

    @Test(expected = InvalidRequestMethodException.class)
    public void nodeControllerGetBadMethod(){

        Controller controller = NodeController.getInstance();
        Request request = new Request();
        request.setBody(null);
        request.setMethod(RequestMethod.UPDATE);
        request.setTargetRoute("all");
        controller.handleRequest(request);
    }

    @Test
    public void serverNodeGetSuccessTestSuccess(){
        try {Server s = new Server();
            Thread t = new Thread(()->{
                try {
                    Thread.sleep(100);
                    Socket socket = new Socket("localhost", 2021);
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    dos.writeUTF("{\"method\":\"GET\", \"targetRoute\":\"all\", \"body\":null}");
                    String response = dis.readUTF();
                    assertEquals(response, "{}");
                    s.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
            s.listen(2021);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
