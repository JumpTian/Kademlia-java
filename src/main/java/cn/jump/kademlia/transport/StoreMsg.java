package cn.jump.kademlia.transport;

import cn.jump.kademlia.dht.JsonSerializer;
import cn.jump.kademlia.dht.Record;
import cn.jump.kademlia.routing.Node;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author JumpTian
 */
@Getter
public class StoreMsg extends AbstractMsg {

    private Node originNode;
    private Record record;

    public StoreMsg(Node originNode, Record record) {
        this.originNode = originNode;
        this.record = record;
    }

    public StoreMsg(DataInputStream in) throws IOException {
        this.originNode = new Node(in);

        byte[] arr = new byte[in.readInt()];
        in.read(arr);
        this.record = new JsonSerializer<Record>().deserialize(arr);
    }

    @Override
    public byte getType() {
        return TYPE_STORE;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        originNode.writeToStream(out);

        byte[] arr = new JsonSerializer<Record>().serialize(record);
        out.write(arr.length);
        out.write(arr);
    }
}
