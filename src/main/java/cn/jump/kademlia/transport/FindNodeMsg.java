package cn.jump.kademlia.transport;

import cn.jump.kademlia.routing.Node;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author JumpTian
 */
@Getter
public class FindNodeMsg extends AbstractMsg {

    private Node originNode;
    private Node.Id lookupId;

    public FindNodeMsg(Node originNode, Node.Id lookupId) {
        this.originNode = originNode;
        this.lookupId = lookupId;
    }

    public FindNodeMsg(DataInputStream in) throws IOException {
        this.originNode = new Node(in);
        this.lookupId = new Node.Id(in);
    }

    @Override
    public byte getType() {
        return TYPE_FIND_NODE;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        originNode.writeToStream(out);

        out.write(lookupId.getKeySpace());
    }
}
