package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.handler.ConnectHandler;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.ConnectMsg;
import cn.jump.kademlia.transport.TransportServer;
import lombok.Getter;

import java.io.IOException;

/**
 * 新节点加入kad网络指令。
 *
 * 新节点上线，需要指定一个节点作为启动节点以加入Kad网络，
 * 具体来说，大致分为两步：
 *   1.首先将节点放入本地路由表，成为启动节点；
 *   2.接着向启动节点发起FIND_NODE请求，目的在于是一告诉启
 *     动节点新增了新节点，二是通过启动发现网络中更多距离
 *     更近的节点；
 *
 * 同时启动节点受到当前节点的FIND_NODE请求，首先将新节点
 * 加入自身的路由表中，然后返回最多K个距离新节点最接近的节
 * 点列表。
 *
 * 随后本地将返回的节点加入本地路由表，然后分别对这些节点发
 * 起与上类似的FIND_NODE请求。
 *
 * 最终新增节点可以逐步建立对网络的理解。
 *
 * @author JumpTian
 */
@Getter
public class ConnectCmd implements Cmd {

    private final Endpoint endpoint;
    private final Node bootstrapNode;
    private final TransportServer transportServer;

    private ConnectCmd(Endpoint endpoint, Node bootstrapNode, TransportServer transportServer) {
        this.endpoint = endpoint;
        this.bootstrapNode = bootstrapNode;
        this.transportServer = transportServer;
    }

    /**
     * 发起和kad网络建立联系的请求
     *
     * @param endpoint 当前节点
     * @param bootstrapNode 启动节点
     * @param transportServer Kad服务
     */
    public static ConnectCmd fire(Endpoint endpoint, Node bootstrapNode,
                            TransportServer transportServer) throws IOException {
        ConnectCmd cmd = new ConnectCmd(endpoint, bootstrapNode, transportServer);
        cmd.execute();
        return cmd;
    }

    @Override
    public void execute() throws IOException {
        ConnectMsg connectMsg = new ConnectMsg(endpoint.getLocalNode());
        transportServer.sendMsg(bootstrapNode, connectMsg, new ConnectHandler(this));

        FindNodeCmd.fire(endpoint, bootstrapNode.getId(), transportServer);
    }
}
