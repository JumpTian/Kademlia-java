package cn.jump.kademlia.transport;

import cn.jump.kademlia.routing.Node;
import lombok.Getter;

import java.io.DataOutputStream;
import java.util.List;

/**
 * @author JumpTian
 */
@Getter
public class FindNodeReply implements Msg {

    private Node originNode;
    private List<Node> nodeList;

    @Override
    public byte getType() {
        return 0;
    }

    @Override
    public void writeToStream(DataOutputStream out) {

    }
}
