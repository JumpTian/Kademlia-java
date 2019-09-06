package cn.jump.kademlia.routing;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.TreeSet;

/**
 * 在Kad中通过使用一个Bucket来保存和当前节点距离在某个范围内
 * 的所有节点列表，比如Bucket#0记录来[1,2)范围内的节点。
 *
 * 根据节点id的不同，计算得到LCP，相同的LCP的peer节点在当前
 * 节点存放到同一Bucket（对应节点位树中的一个子树），同时通
 * 过指定K参数来限定每个Bucket中的节点数量。
 *
 * 由于节点可能是频繁加入和退出网络的，而k-bucket保存的是相
 * 对静态的信息，因此需要随着一些条件的变化来进行相应的更新，
 * 典型的包括：
 *   1.连上一个新的peer节点
 *   2.查询原本不在k-bucket中的节点
 *
 * 通过记录k-bucket中的每个节点的最近访问时间戳来判断节点
 * 的活跃度，如果Bucket已经满了，直接添加节点；否则判断是否
 * 需要剔除失效节点，如果存在则使用新节点替换，否则丢弃新
 * 节点。
 *
 * @author JumpTian
 */
@Getter
public class Bucket {

    private final TreeSet<Contact> contactSet = Sets.newTreeSet();

    public Bucket() {

    }

    public void insert(Node node) {

    }

    /**
     * 添加一条路由记录到bucket中
     *
     * @param contact 路由记录
     */
    public void insert(Contact contact) {
        if (contactSet.contains(contact)) {
            // 如果contact中节点已经在本地k-bucket中，则重新设置最近访问时间，
            // 并按照最近访问时间时间排序
            contact = remove(contact.getNode().getId());
            contact.setLastAccess();
            contactSet.add(contact);
        } else {
            // todo
            // 否则，ping一下列表最上（旧）的一个节点
            // r如果ping通了，将旧节点放到列表最底，并丢弃新节点
            // 如果ping不同，删除旧节点，并将新节点放到列表
            // 从而保证了任意节点的加入和离开都不影响整体网络
            // 如果当前bucket已满
        }
    }

    /**
     * 根据节点id从路由表中删除Contact，并返回
     *
     * @param nodeId 节点id
     * @return 被删除Contact
     */
    public Contact remove(Node.Id nodeId) {
        for (Contact contact : contactSet) {
            if (contact.getNode().getId().equals(nodeId)) {
                contactSet.remove(contact);
                return contact;
            }
        }
        throw new IllegalStateException("Node not exist");
    }
}
