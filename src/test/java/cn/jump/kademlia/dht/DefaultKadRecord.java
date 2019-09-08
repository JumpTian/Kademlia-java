package cn.jump.kademlia.dht;

import cn.jump.kademlia.routing.Node;
import lombok.Getter;

/**
 * 用于测试的存储记录
 *
 * @author JumpTian
 */
@Getter
public class DefaultKadRecord implements KadRecord {

    private final Node.Id nodeId;
    private final String ownerId;
    private String data;
    private final long createTime;
    private long lastUpdateTime;
    private long lastRepublishTime;

    public DefaultKadRecord(Node.Id nodeId, String ownerId, String data) {
        this.nodeId = nodeId;
        this.ownerId = ownerId;
        this.data = data;
        this.createTime = System.currentTimeMillis();
        this.lastUpdateTime = createTime;
        this.lastRepublishTime = createTime;
    }

    @Override
    public Node.Id getNodeId() {
        return nodeId;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
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
    public long lastRepublishTime() {
        return lastRepublishTime;
    }

    @Override
    public void updateRepublishTime() {
        lastRepublishTime = System.currentTimeMillis();
    }

    @Override
    public boolean satisfy(String ownerId, Node.Id key) {
        if (ownerId == null || !this.ownerId.equals(ownerId)) {
            return false;
        }
        if (key == null || !nodeId.equals(key)) {
            return false;
        }
        return true;
    }
}
