package cn.jump.kademlia.dht;

import cn.jump.kademlia.routing.Node;

/**
 * 存储在本地的记录
 *
 * @author JumpTian
 */
public interface Record {

    /**
     * 获取DHT中的key，查找通过这个key在kad网络中
     * 进行路由
     *
     * @return 节点id
     */
    Node.Id getNodeId();

    /**
     * 记录的所有者id
     *
     * @return owner id
     */
    String getOwnerId();

    /**
     * 记录类型
     *
     * @return 类型
     */
    String getType();

    /**
     * 记录的创建时间
     *
     * @return 创建时间
     */
    long getCreateTime();

    /**
     * 记录最后的更新时间
     *
     * @return 更新时间
     */
    long getLastUpdateTime();

    /**
     * 对当前记录进行序列化以便于存储和网络传输
     *
     * @return 记录的字节数组
     */
    byte[] serialize();

    /**
     * 对指定字节数组进行反序列化
     *
     * @param data 字节数组
     * @return 记录对象
     */
    Record deserialize(byte[] data);
}
