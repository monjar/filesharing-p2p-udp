package controller;

import annotations.GetRequest;
import annotations.PostRequest;
import protocol.RequestBody;
import protocol.dto.NodeInputDto;
import protocol.dto.NodeListDto;
import protocol.Request;
import protocol.dto.ServedFileDto;
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
    public NodeListDto getAllNodesData(Request request) {
        return this.service.getAllNodesData();
    }

    @PostRequest("/")
    public StringDto addNode(Request request) {
        NodeInputDto dto = RequestBody.parse(NodeInputDto.class, request.getBody());
        dto.setAddress(request.getAddress());
        String id = this.service.addNode(dto);
        return new StringDto(id);
    }

    @PostRequest("/addDownloadedFile")
    public void addDownloaded(Request request) {
        this.service.addDownloaded(request.getAddress());
    }

    @PostRequest("/addUploadedFile")
    public void addUploaded(Request request) {
        this.service.addUploaded(request.getAddress());
    }


    @PostRequest("/addServedFile")
    public void addServedFile(Request request) {
        ServedFileDto dto = RequestBody.parse(ServedFileDto.class, request.getBody());
        this.service.addServedFile(dto, request.getAddress());
    }
}
