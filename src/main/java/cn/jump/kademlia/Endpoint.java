package cn.jump.kademlia;

import cn.jump.kademlia.cmd.BucketRefreshCmd;
import cn.jump.kademlia.cmd.ConnectCmd;
import cn.jump.kademlia.cmd.FindRecordCmd;
import cn.jump.kademlia.cmd.RecordRefreshCmd;
import cn.jump.kademlia.cmd.StoreCmd;
import cn.jump.kademlia.dht.KadRecord;
import cn.jump.kademlia.dht.Table;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.routing.RoutingTable;
import cn.jump.kademlia.transport.FindParam;
import cn.jump.kademlia.transport.TransportServer;
import lombok.Getter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 作为Kad网络中作为本地节点抽象
 *
 * @author JumpTian
 */
@Getter
public class Endpoint {

    private final Node localNode;
    private final RoutingTable routingTable;
    private final TransportServer transportServer;
    private final Table table;

    private ScheduledExecutorService refreshPool;
    private Runnable refreshTask;

    private Endpoint(Node.Id nodeId, String ownerId, int port) throws UnknownHostException, SocketException {
        this.localNode = new Node(nodeId, new InetSocketAddress(InetAddress.getLocalHost(), port));
        this.routingTable = new RoutingTable(localNode);
        this.transportServer = TransportServer.newInstance(port);
        this.table = new Table(ownerId);
    }

    public static Endpoint newInstance(Node.Id nodeId, String ownerId,
                                       int port) throws SocketException, UnknownHostException {
        Endpoint endpoint = new Endpoint(nodeId, ownerId, port);
        endpoint.startRefresh();
        return endpoint;
    }

    public void startRefresh() {
        refreshPool = new ScheduledThreadPoolExecutor(1);
        refreshTask = () -> {
            try {
                BucketRefreshCmd.fire(this, transportServer);
                RecordRefreshCmd.fire(this, table, transportServer);
            } catch (IOException ignore) {
            }
        };
        refreshPool.schedule(refreshTask, KadConfig.refreshInterval(), TimeUnit.SECONDS);
    }

    public void stopRefresh() {
        refreshPool.shutdown();
    }

    /**
     * 启动本地节点加入Kad网络
     *
     * @param bootstrapNode 启动节点
     */
    public void bootstrap(Node bootstrapNode) throws IOException {
        ConnectCmd.fire(this, bootstrapNode, transportServer);
    }

    /**
     * 存储记录到Kad网络中，实际会被存储到K个节点
     *
     * @param record 记录
     * @return 成功存储节点数量
     */
    public int put(KadRecord record) throws IOException {
        StoreCmd storeCmd = StoreCmd.fire(this, record, table, transportServer);
        return storeCmd.getStoredNodeCount();
    }

    /**
     * 从Kad网络中根据指定参数查找记录
     *
     * @param findParam 查询条件
     * @return 记录
     */
    public KadRecord get(FindParam findParam) throws IOException {
        if (table.contains(findParam)) {
            return table.get(findParam);
        } else {
            FindRecordCmd findRecordCmd = FindRecordCmd.fire(this, findParam, transportServer);
            return findRecordCmd.getRecord();
        }
    }
}
