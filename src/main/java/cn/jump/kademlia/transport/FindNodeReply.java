package cn.jump.kademlia.transport;

import cn.jump.kademlia.routing.Node;
import lombok.Getter;

import java.util.List;

/**
 * @author JumpTian
 */
@Getter
public class FindNodeReply implements Msg {

    private Node originNode;
    private List<Node> nodeList;
}
