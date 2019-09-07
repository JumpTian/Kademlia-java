package cn.jump.kademlia.dht;

import cn.jump.kademlia.routing.Node;

/**
 * 用于测试的存储记录
 *
 * @author JumpTian
 */
public class RecordImpl implements Record {

    private Node.Id nodeId;
    private String data;
    private long createTime;
    private long lastUpdateTime;

    public RecordImpl(Node.Id nodeId, String data) {
        this.nodeId = nodeId;
        this.data = data;
        this.createTime = System.currentTimeMillis();
        this.lastUpdateTime = createTime;
    }

    @Override
    public Node.Id getNodeId() {
        return nodeId;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public Record deserialize(byte[] data) {
        return null;
    }
}
