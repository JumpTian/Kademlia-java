package cn.jump.kademlia.routing;

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
public class RoutingTable {

}
