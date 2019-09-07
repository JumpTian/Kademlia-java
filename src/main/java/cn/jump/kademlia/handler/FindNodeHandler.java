package cn.jump.kademlia.handler;

import static cn.jump.kademlia.cmd.AbstractCmd.ASKED;
import cn.jump.kademlia.cmd.FindNodeCmd;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.FindNodeReply;
import cn.jump.kademlia.transport.AbstractMsg;

import java.io.IOException;
import java.util.List;

/**
 * 处理接收到的查找节点消息
 *
 * @author JumpTian
 */
public class FindNodeHandler extends AbstractHandler {

    private final FindNodeCmd findNodeCmd;

    public FindNodeHandler(FindNodeCmd findNodeCmd) {
        this.findNodeCmd = findNodeCmd;
    }

    @Override
    public void handle(AbstractMsg inbound, int sessionId) throws IOException {
        if (!(inbound instanceof FindNodeReply)) {
            return;
        }
        findNodeCmd.getMsgTransitingMap().remove(sessionId);

        FindNodeReply findNodeReply = (FindNodeReply) inbound;

        Node originNode = findNodeReply.getOriginNode();
        findNodeCmd.getEndpoint().getRoutingTable().insert(originNode);
        findNodeCmd.getNodeMap().put(originNode, ASKED);

        List<Node> nodeList = findNodeReply.getNodeList();
        findNodeCmd.addNodeList(nodeList);

        findNodeCmd.askNodeFinish();
    }
}
