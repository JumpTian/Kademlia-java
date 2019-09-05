package cn.jump.kademlia.routing;

import lombok.Getter;

/**
 * 记录peer节点的通信信息
 *
 * @author JumpTian
 */
@Getter
public class Contact implements Comparable<Contact> {

    /**
     * 节点
     */
    private final Node node;
    /**
     * 最后访问节点时间戳
     */
    private long lastAccess;

    public Contact(Node node) {
        this.node = node;
    }

    public void setLastAccess() {
        this.lastAccess = System.currentTimeMillis();
    }

    @Override
    public int compareTo(Contact contact) {
        if (getNode().getId().equals(contact.getNode().getId())) {
            return 0;
        } else {
            return (int) (getLastAccess() - contact.getLastAccess());
        }
    }
}
