package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.KadConfig;
import cn.jump.kademlia.handler.FindNodeHandler;
import cn.jump.kademlia.handler.AbstractHandler;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.FindNodeMsg;
import cn.jump.kademlia.transport.AbstractMsg;
import cn.jump.kademlia.transport.TransportServer;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

/**
 * 根据节点id查找k个距离最接近节点
 *
 * @author JumpTian
 */
@Getter
public class FindNodeCmd extends AbstractCmd {

    private final AbstractMsg findNodeMsg;

    private FindNodeCmd(Endpoint endpoint, Node.Id lookupId, TransportServer transportServer) {
       super(endpoint, lookupId, transportServer);
        this.findNodeMsg = new FindNodeMsg(endpoint.getLocalNode(), lookupId);
    }

    public static FindNodeCmd fire(Endpoint endpoint, Node.Id lookupId,
                                   TransportServer transportServer) throws IOException {
        FindNodeCmd cmd = new FindNodeCmd(endpoint, lookupId, transportServer);
        cmd.execute();
        return cmd;
    }

    private void execute() throws IOException {
        nodeMap.put(endpoint.getLocalNode(), ASKED);

        addNodeList(endpoint.getRoutingTable().findAllNode());

        try {
            int totalWaitTime = 0;
            int waitInterval = 10;
            while (totalWaitTime < KadConfig.socketTimeout()) {
                if (!askNodeFinish()) {
                    wait(waitInterval);
                    totalWaitTime += waitInterval;
                } else {
                    break;
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().isInterrupted();
            throw new RuntimeException(ex);
        }

        endpoint.getRoutingTable().clearFailedContact(getNodeListByStatus(FAILED));
    }

    @Override
    protected AbstractMsg getMsg() {
        return findNodeMsg;
    }

    @Override
    protected AbstractHandler getHandler() {
        return new FindNodeHandler(this);
    }

    /**
     * 获取k-closest节点
     *
     * @return 节点列表
     */
    public List<Node> getClosestNodeList() {
        return getNodeListByStatus(ASKED, KadConfig.k());
    }

    /**
     * 获取全部指定状态的节点
     *
     * @param status 节点状态
     * @return 节点列表
     */
    public List<Node> getNodeListByStatus(byte status) {
        return getNodeListByStatus(status, -1);
    }
}
