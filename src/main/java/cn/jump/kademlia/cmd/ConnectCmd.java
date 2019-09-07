package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.handler.ConnectHandler;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.ConnectMsg;
import cn.jump.kademlia.transport.Msg;
import cn.jump.kademlia.transport.TransportServer;

import java.io.IOException;

/**
 * 用于节点连接到kad网络的指令
 *
 * @author JumpTian
 */
public class ConnectCmd {

    private Endpoint endpoint;
    private TransportServer transportServer;

    private ConnectCmd(Endpoint endpoint, TransportServer transportServer) {
        this.endpoint = endpoint;
        this.transportServer = transportServer;
    }

    /**
     * 发起和kad网络建立连接请求
     *
     * @param endpoint 当前节点
     * @param bootstrapNode 启动节点
     * @param transportServer Kad服务
     */
    public static void fire(Endpoint endpoint, Node bootstrapNode,
                            TransportServer transportServer) throws IOException {
        ConnectCmd cmd = new ConnectCmd(endpoint, transportServer);
        cmd.execute(bootstrapNode);
    }

    /**
     * 新节点上线，需要指定一个节点作为proxy加入Kad网
     * 络，具体来说：
     *   1.将节点放入本地路由表，成为proxy节点
     *   2.向proxy节点发起FIND_NODE请求，目的在于是一
     *     告诉proxy节点新增了新节点，二是通过proxy发现
     *     网络中更多距离更近的节点
     * 同时proxy节点受到当前节点的FIND_NODE请求，首先
     * 将新节点加入自身的路由表中，然后返回最多K个距离
     * 新节点最接近的节点列表。
     *
     * 随后本地将返回的节点加入本地路由表，然后分别对这些
     * 节点发起与上类似的FIND_NODE请求。
     *
     * 最终新增节点可以逐步建立对网络的理解。
     *
     * @param bootstrapNode proxy启动节点
     * @throws IOException
     */
    private void execute(Node bootstrapNode) throws IOException {
        Msg connectMsg = new ConnectMsg();
        transportServer.sendMsg(bootstrapNode, connectMsg, new ConnectHandler());

        FindNodeCmd.fire(endpoint, bootstrapNode.getId(), transportServer);
    }
}
