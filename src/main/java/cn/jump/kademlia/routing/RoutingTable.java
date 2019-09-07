package cn.jump.kademlia.routing;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.List;
import java.util.TreeSet;

/**
 * Kad网络路由表
 *
 * 每个节点都会维护一个路由表，用来记录网络中其他节点
 * 的路由通信信息。
 *
 * kad要求每个节点至少知道其各LCP位子树至少一个节点，
 * 这样每个节点都可以通过XOR值找到任何一个节点。
 *
 * Bucket的分配方式一般有几种：
 *   1.每个子树对应一个Bucket，kad动态分配Bucket，如果节
 *     点少的时候，就只有一个Bucket，当节点不断增加之后，
 *     原Bucket不断分裂。
 *   2.预先按照节点位空间一次性分配所有Bucket。
 *
 * @author JumpTian
 */
@Getter
public class RoutingTable {

    private Node localNode;
    private Bucket[] bucketArr = new Bucket[Node.Id.SPACE];

    {
        for (int i = 0; i < Node.Id.SPACE; i++) {
            bucketArr[i] = new Bucket();
        }
    }

    public RoutingTable(Node localNode) {
        this.localNode = localNode;
        this.insert(localNode);
    }

    /**
     * 向路由表插入一条节点
     *
     * @param node 节点
     */
    public void insert(Node node) {
        int idx = getBucketIdx(node);
        bucketArr[idx].insert(node);
    }

    /**
     * 向路由表插入一条contact
     *
     * @param contact 路由记录
     */
    public void insert(Contact contact) {
        int idx = getBucketIdx(contact.getNode());
        bucketArr[idx].insert(contact);
    }

    /**
     * 获取指定节点应该存放在当前节点哪层Bucket
     *
     * @param node peer节点
     * @return Bucket索引
     */
    private int getBucketIdx(Node node) {
        int idx = Node.Id.SPACE - node.calculateDistance(node);
        return idx > 0 ? idx : 0;
    }

    /**
     * 查找当前路由表中所有节点
     *
     * @return 节点列表
     */
    public List<Node> findAllNode() {
        List<Node> nodeList = Lists.newArrayList();
        for (Bucket bucket : bucketArr) {
            for (Contact contact : bucket.getContactSet()) {
                nodeList.add(contact.getNode());
            }
        }
        return nodeList;
    }

    /**
     * 查找距离指定节点距离最近的节点列表
     *
     * @param nodeId 指定节点id
     * @param count 返回的节点数量
     * @return 节点列表
     */
    public List<Node> findClosestNode(Node.Id nodeId, int count) {
        TreeSet<Node> nodeSet = Sets.newTreeSet(new NodeComparator(nodeId));
        nodeSet.addAll(findAllNode());

        List<Node> closest = Lists.newArrayList();
        for (Node node : nodeSet) {
            if (count-- <= 0) {
                break;
            }
            closest.add(node);
        }
        return closest;
    }

    /**
     * 从路由表中删除无效节点列表
     *
     * @param failedNodeList 无效节点列表
     */
    public void clearFailedContact(List<Node> failedNodeList) {
        if (failedNodeList == null || failedNodeList.isEmpty()) {
            return;
        }
        for (Node failNode : failedNodeList) {
            clearFailedContact(failNode);
        }
    }

    /**
     * 从路由表中删除无效节点
     *
     * @param failNode 无效节点
     */
    public void clearFailedContact(Node failNode) {
        int idx = getBucketIdx(failNode);
        bucketArr[idx].remove(failNode.getId());
    }
}
