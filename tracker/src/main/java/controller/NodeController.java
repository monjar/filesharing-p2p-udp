package controller;

import annotations.GetRequest;
import annotations.PostRequest;
import protocol.RequestBody;
import protocol.dto.NodeInputDto;
import protocol.dto.NodeListDto;
import protocol.Request;
import protocol.dto.StringDto;
import service.NodeService;

public class NodeController extends Controller {
    private static volatile NodeController instance;
    private final NodeService service;
    private NodeController() {
        this.service = NodeService.getInstance();
    }

    synchronized public static NodeController getInstance() {
        if (instance == null)
            instance = new NodeController();
        return instance;
    }

    @GetRequest("/")
    public NodeListDto getAllNodesData(Request request){
        return this.service.getAllNodesData();
    }
    @PostRequest("/")
    public StringDto addNode(Request request){
        String id = this.service.addNode(RequestBody.parse(NodeInputDto.class, request.getBody()));
        return new StringDto(id);
    }
}
