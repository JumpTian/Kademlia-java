package cn.jump.kademlia.transport;

import cn.jump.kademlia.routing.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author JumpTian
 */
public class ConnectMsg extends AbstractMsg {

    private Node originNode;

    public ConnectMsg(Node originNode) {
        this.originNode = originNode;
    }

    public ConnectMsg(DataInputStream in) throws IOException {
        this.originNode = new Node(in);
    }

    @Override
    public byte getType() {
        return TYPE_CONNECT;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        originNode.writeToStream(out);
    }
}
