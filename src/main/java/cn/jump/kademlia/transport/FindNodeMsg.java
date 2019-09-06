package cn.jump.kademlia.transport;

import cn.jump.kademlia.routing.Node;
import lombok.Getter;

/**
 * @author JumpTian
 */
@Getter
public class FindNodeMsg implements Msg {

    private Node originNode;
    private Node.Id lookupId;

    public FindNodeMsg(Node originNode, Node.Id lookupId) {
        this.originNode = originNode;
        this.lookupId = lookupId;
    }
}
