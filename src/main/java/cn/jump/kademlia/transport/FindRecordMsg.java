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
public class FindRecordMsg extends AbstractMsg {

    private final Node originNode;
    private final FindParam findParam;
    private Record record;

    public FindRecordMsg(Node originNode, FindParam findParam) {
        this.originNode = originNode;
        this.findParam = findParam;
    }

    public FindRecordMsg(DataInputStream in) throws IOException {
        this.originNode = new Node(in);

        byte[] arr = new byte[in.readInt()];
        in.read(arr);
        this.findParam = new JsonSerializer<FindParam>().deserialize(arr);
    }

    @Override
    public byte getType() {
        return TYPE_FIND_RECORD;
    }

    @Override
    public void writeToStream(DataOutputStream out) throws IOException {
        originNode.writeToStream(out);

        byte[] data = new JsonSerializer<FindParam>().serialize(findParam);
        out.write(data.length);
        out.write(data);
    }
}
