package dto;

import model.Node;
import protocol.Response;

import java.io.Serializable;
import java.util.List;


public class NodeListDto extends Response {
    List<Node> nodeList;
}
