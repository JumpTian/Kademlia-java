package cn.jump.kademlia.handler;

import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.FindNodeReply;
import cn.jump.kademlia.transport.Msg;

import java.util.List;

/**
 * 处理接收到的查找节点消息
 *
 * @author JumpTian
 */
public class FindNodeHandler implements Handler {

    @Override
    public void handle(Msg inbound, int sessionId) {
        if (!(inbound instanceof FindNodeReply)) {
            return;
        }
        FindNodeReply findNodeReply = (FindNodeReply) inbound;

        Node originNode = findNodeReply.getOriginNode();
        List<Node> nodeList = findNodeReply.getNodeList();
    }
}
