package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.KadConfig;
import cn.jump.kademlia.dht.Record;
import cn.jump.kademlia.handler.FindRecordHandler;
import cn.jump.kademlia.handler.Handler;
import cn.jump.kademlia.transport.FindRecordMsg;
import cn.jump.kademlia.transport.Msg;
import cn.jump.kademlia.transport.TransportServer;
import lombok.Data;

import java.io.IOException;

/**
 * 根据key查找指定数据，和FIND_NODE类似
 *
 * @author JumpTian
 */
@Data
public class FindRecordCmd extends AbstractCmd {

    private final Msg findRecordMsg;

    private Record record;

    private FindRecordCmd(Endpoint endpoint, TransportServer transportServer) {
        super(endpoint, null, transportServer);
        this.findRecordMsg = new FindRecordMsg(endpoint.getLocalNode());
    }

    public static FindRecordCmd fire(Endpoint endpoint, TransportServer transportServer) throws IOException {
        FindRecordCmd cmd = new FindRecordCmd(endpoint, transportServer);
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
    protected Msg getMsg() {
        return findRecordMsg;
    }

    @Override
    protected Handler getHandler() {
        return new FindRecordHandler(this);
    }
}
