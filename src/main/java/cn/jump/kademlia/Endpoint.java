package cn.jump.kademlia;

import cn.jump.kademlia.cmd.ConnectCmd;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.routing.RoutingTable;
import cn.jump.kademlia.transport.TransportServer;
import lombok.Getter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 作为Kad网络中作为本地节点抽象
 *
 * @author JumpTian
 */
@Getter
public class Endpoint {

    private Node localNode;
    private RoutingTable routingTable;
    private TransportServer transportServer;

    public Endpoint(Node.Id nodeId, int port) throws UnknownHostException, SocketException {
        this.localNode = new Node(nodeId, new InetSocketAddress(InetAddress.getLocalHost(), port));
        this.routingTable = new RoutingTable(localNode);
        this.transportServer = TransportServer.newInstance(port);
    }

    /**
     * 启动本地节点进入Kad网络
     *
     * @param bootstrapNode proxy启动节点
     */
    public void bootstrap(Node bootstrapNode) throws IOException {
        ConnectCmd.fire(this, bootstrapNode, transportServer);
    }
}
