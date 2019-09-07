package cn.jump.kademlia.transport;

import cn.jump.kademlia.routing.Node;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JumpTian
 */
@Getter
public class FindNodeReply extends AbstractMsg {

    private Node originNode;
    private List<Node> nodeList;

    public FindNodeReply(Node originNode, List<Node> nodeList) {
        this.originNode = originNode;
        this.nodeList = nodeList;
    }

    public FindNodeReply(DataInputStream in) throws IOException {
        this.originNode = new Node(in);

        int len = in.readInt();
        this.nodeList = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            this.nodeList.add(new Node(in));
        }
    }

    @Override
    public byte getType() {
        return TYPE_FIND_NODE_REPLY;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        originNode.writeToStream(out);

        if (nodeList.size() > 255) {
            throw new IndexOutOfBoundsException("Too many nodes in list to send");
        }
        out.writeInt(nodeList.size());

        for (Node node : nodeList) {
            node.writeToStream(out);
        }
    }
}
