package protocol.dto;

import database.model.Node;
import protocol.Response;

import java.util.ArrayList;
import java.util.List;


public class NodeListDto extends Response {
    private List<Node> nodeList;
    public NodeListDto(){
        this.nodeList = new ArrayList<>();
    }
    public NodeListDto(List<Node> nodeList){
        this.nodeList = nodeList;
    }
    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }
}
