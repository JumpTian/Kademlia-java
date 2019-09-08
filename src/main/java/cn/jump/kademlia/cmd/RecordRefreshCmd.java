package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.KadConfig;
import cn.jump.kademlia.dht.KadRecord;
import cn.jump.kademlia.dht.Table;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.StoreMsg;
import cn.jump.kademlia.transport.TransportServer;

import java.io.IOException;
import java.util.List;

/**
 * 记录刷新指令，对于每个存储在DHT的记录，每隔指定时间检查当前
 * 节点是否是记录的k-closest，如果不是则删除。
 *
 * @author JumpTian
 */
public class RecordRefreshCmd implements Cmd {

    private final Endpoint endpoint;
    private final Table table;
    private final TransportServer transportServer;

    private RecordRefreshCmd(Endpoint endpoint, Table table, TransportServer transportServer) {
        this.endpoint = endpoint;
        this.table = table;
        this.transportServer = transportServer;
    }

    public static RecordRefreshCmd fire(Endpoint endpoint, Table table,
                                        TransportServer transportServer) throws IOException {
        RecordRefreshCmd cmd = new RecordRefreshCmd(endpoint, table, transportServer);
        cmd.execute();
        return cmd;
    }

    @Override
    public void execute() throws IOException {
        List<KadRecord> recordList = table.getAllRecord();

        // 如果记录的last republish是在此之前，我们需要Re-publish
        long minRepublishTime = System.currentTimeMillis() - KadConfig.refreshInterval();

        // 依次对本地存储的记录进行distribute
        for (KadRecord record : recordList) {
            if (record.lastRepublishTime() > minRepublishTime) {
                continue;
            }

            record.updateRepublishTime();

            List<Node> closestNodeList = endpoint.getRoutingTable().findClosestNode(record.getNodeId(), KadConfig.k());
            StoreMsg storeMsg = new StoreMsg(endpoint.getLocalNode(), table.get(record));
            for (Node node : closestNodeList) {
                if (node.getId() != endpoint.getLocalNode().getId()) {
                    transportServer.sendMsg(node, storeMsg, null);
                }
            }

            if (!closestNodeList.contains(endpoint.getLocalNode())) {
                table.remove(record);
            }
        }
    }
}
