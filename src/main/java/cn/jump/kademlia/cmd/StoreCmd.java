package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.dht.Record;
import cn.jump.kademlia.dht.Table;
import cn.jump.kademlia.handler.AbstractHandler;
import cn.jump.kademlia.handler.StoreHandler;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.AbstractMsg;
import cn.jump.kademlia.transport.StoreMsg;
import cn.jump.kademlia.transport.TransportServer;

import java.io.IOException;
import java.util.List;

/**
 * 要求节点存储一份数据
 * 即存储记录到k个最接近发起节点的节点集
 *
 * @author JumpTian
 */
public class StoreCmd extends AbstractCmd {

    private final AbstractMsg storeMsg;
    private final Record record;
    private final Table table;

    private StoreCmd(Endpoint endpoint, Record record, Table table, TransportServer transportServer) {
        super(endpoint, record.getNodeId(), transportServer);
        this.storeMsg = new StoreMsg(endpoint.getLocalNode(), record);
        this.record = record;
        this.table = table;
    }

    public static StoreCmd fire(Endpoint endpoint, Record record,
                            Table table, TransportServer transportServer) throws IOException {
        StoreCmd cmd = new StoreCmd(endpoint, record, table, transportServer);
        cmd.execute();
        return cmd;
    }

    private void execute() throws IOException {
        FindNodeCmd findNodeCmd = FindNodeCmd.fire(endpoint, record.getNodeId(), transportServer);
        List<Node> nodeList = findNodeCmd.getClosestNodeList();

        for (Node node : nodeList) {
            if (node.getId().equals(endpoint.getLocalNode().getId())) {
                table.store(record);
            } else {
                transportServer.sendMsg(node, storeMsg, new StoreHandler());
            }
        }
    }

    @Override
    protected AbstractMsg getMsg() {
        return storeMsg;
    }

    @Override
    protected AbstractHandler getHandler() {
        return new StoreHandler();
    }

    /**
     * 返回实际成功存储指定记录的节点数量
     *
     * @return 节点数量
     */
    public int getStoredNodeCount() {
        //todo
        return 1;
    }
}
