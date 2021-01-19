package service;

import database.DatabaseConnector;
import database.model.Node;
import exceptions.DuplicateEntryException;
import protocol.dto.NodeInputDto;
import protocol.dto.NodeListDto;
import protocol.Request;

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

    public NodeListDto getAllNodesData(){
        List<Node> list= repo.loadAll(Node.class);
        NodeListDto nodeListDto = new NodeListDto(list);
        return nodeListDto;
    }

    public String addNode(NodeInputDto dto){
        Node node = new Node(dto.getAddress(), 0,0);
        if(repo.exists(Node.class, node.getAddress()))
            throw new DuplicateEntryException(dto.getAddress());
        return repo.save(node);
    }
}
