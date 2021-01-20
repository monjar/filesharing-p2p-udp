package service;

import database.DatabaseConnector;
import database.model.Node;
import exceptions.DuplicateEntryException;
import protocol.dto.NodeInputDto;
import protocol.dto.NodeListDto;
import protocol.Request;
import protocol.dto.ServedFileDto;

import java.util.Collections;
import java.util.List;

public class NodeService {
    private static volatile NodeService instance;
    private DatabaseConnector repo;

    private NodeService() {
        repo = DatabaseConnector.getInstance();
    }

    synchronized public static NodeService getInstance() {
        if (instance == null)
            instance = new NodeService();
        return instance;
    }

    public NodeListDto getAllNodesData() {
        List<Node> list = repo.loadAll(Node.class);
        NodeListDto nodeListDto = new NodeListDto(list);
        return nodeListDto;
    }

    public String addNode(NodeInputDto dto) {
        Node node = new Node(dto.getAddress(), 0, 0);
        if (repo.exists(Node.class, node.getAddress())) {
            repo.update(node, node.getAddress());
            return node.getAddress();
        }
        return repo.save(node);
    }

    public void addDownloaded(String id) {
        Node node = repo.loadOneById(Node.class, id);
        node.setDownloadedFilesCount(node.getDownloadedFilesCount() + 1);
        repo.update(node, id);
    }

    public void addUploaded(String id) {
        Node node = repo.loadOneById(Node.class, id);
        node.setUploadedFilesCount(node.getUploadedFilesCount() + 1);
        repo.update(node, id);
    }

    public void addServedFile(ServedFileDto dto, String id) {
        Node node = repo.loadOneById(Node.class, id);
        node.addServedFile(dto.getFileName());
        repo.update(node, id);
//        repo.addFile(node, dto.getFileName());
    }
}
