package cn.jump.kademlia.dht;

import cn.jump.kademlia.routing.Node;

/**
 * 继承自Record，额外增加了诸如计算记录哈希以及Re-publish
 * 等元信息
 *
 * @author JumpTian
 */
public interface KadRecord extends Record {

    /**
     * 返回记录最后Re-publish时间
     *
     * @return 时间戳
     */
    long lastRepublishTime();

    /**
     * 更新Re-publish时间戳
     */
    void updateRepublishTime();

    /**
     * 根据指定参数判断当前记录是否满足
     *
     * @param ownerId 所属id
     * @param key 源节点id
     * @return 是否满足
     */
    boolean satisfy(String ownerId, Node.Id key);
}
