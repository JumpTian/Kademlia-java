package cn.jump.kademlia.dht;

import cn.jump.kademlia.routing.Node;

/**
 * 存储在kad网络中内容记录
 *
 * @author JumpTian
 */
public interface Record {

    /**
     * 记录的源节点id，在Kad网络以该节点id为key进行路由，
     * 来决定记录存储的范围
     *
     * @return 源节点id
     */
    Node.Id getNodeId();

    /**
     * 记录的所有者id
     *
     * @return 所有者id
     */
    String getOwnerId();

    /**
     * 记录的创建时间
     *
     * @return 创建时间
     */
    long getCreateTime();

    /**
     * 记录的最后更新时间
     *
     * @return 更新时间
     */
    long getLastUpdateTime();
}
