package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.handler.FindNodeHandler;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.FindNodeMsg;
import cn.jump.kademlia.transport.Msg;
import cn.jump.kademlia.transport.TransportServer;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 根据节点id查找k个距离最接近节点
 *
 * @author JumpTian
 */
public class FindNodeCmd {

    private Endpoint endpoint;
    private TransportServer transportServer;

    private FindNodeCmd(Endpoint endpoint, TransportServer transportServer) {
        this.endpoint = endpoint;
        this.transportServer = transportServer;
    }

    public static void fire(Endpoint endpoint, TransportServer transportServer) throws IOException {
        FindNodeCmd cmd = new FindNodeCmd(endpoint, transportServer);
        cmd.execute();
    }

    private void execute() throws IOException {
        List<Node> nodeList = endpoint.getRoutingTable().findAllNode();
        if (nodeList.isEmpty()) {
            return;
        }

        //todo 并发查询
        int randomIdx = new Random().nextInt(nodeList.size()-1);
        Node node = nodeList.get(randomIdx > 0 ? randomIdx : 0);

        Msg findNodeMsg = new FindNodeMsg(endpoint.getLocalNode(), endpoint.getLocalNode().getId());
        long sessionId = transportServer.sendMsg(node, findNodeMsg, new FindNodeHandler());

        //todo 清理本地路由表本次ping不通的节点
    }
}
