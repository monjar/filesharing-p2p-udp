package controller;

import annotations.GetRequest;
import dto.NodeListDto;
import gateway.RequestRouter;
import protocol.Request;
import protocol.Response;

public class NodeController extends Controller {
    private static volatile NodeController instance;
    private NodeController() {
    }
    synchronized public static NodeController getInstance() {
        if (instance == null)
            instance = new NodeController();

        return instance;
    }


    @GetRequest("all")
    public NodeListDto getAllNodesData(){
        return new NodeListDto();
    }
}
