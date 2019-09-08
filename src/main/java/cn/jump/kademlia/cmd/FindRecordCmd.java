package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.KadConfig;
import cn.jump.kademlia.dht.KadRecord;
import cn.jump.kademlia.handler.AbstractHandler;
import cn.jump.kademlia.handler.FindRecordHandler;
import cn.jump.kademlia.transport.AbstractMsg;
import cn.jump.kademlia.transport.FindParam;
import cn.jump.kademlia.transport.FindRecordMsg;
import cn.jump.kademlia.transport.TransportServer;
import lombok.Data;

import java.io.IOException;

/**
 * 根据key查找指定数据，和FIND_NODE类似
 *
 * @author JumpTian
 */
@Data
public class FindRecordCmd extends AbstractFindCmd {

    private final AbstractMsg findRecordMsg;

    private KadRecord record;

    private FindRecordCmd(Endpoint endpoint, FindParam findParam, TransportServer transportServer) {
        super(endpoint, findParam.getKey(), transportServer);
        this.findRecordMsg = new FindRecordMsg(endpoint.getLocalNode(), findParam);
    }

    public static FindRecordCmd fire(Endpoint endpoint, FindParam findParam,
                                     TransportServer transportServer) throws IOException {
        FindRecordCmd cmd = new FindRecordCmd(endpoint, findParam, transportServer);
        cmd.execute();
        return cmd;
    }

    @Override
    public void execute() throws IOException {
        nodeMap.put(endpoint.getLocalNode(), ASKED);

        addNodeList(endpoint.getRoutingTable().findAllNode());

        try {
            int totalWaitTime = 0;
            int waitInterval = 10;
            while (totalWaitTime < KadConfig.socketTimeout()) {
                if (!askNodeFinish() && getRecord() != null) {
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
    }

    @Override
    protected AbstractMsg getMsg() {
        return findRecordMsg;
    }

    @Override
    protected AbstractHandler getHandler() {
        return new FindRecordHandler(this);
    }
}
