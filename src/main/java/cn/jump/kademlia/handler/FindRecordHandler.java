package cn.jump.kademlia.handler;

import cn.jump.kademlia.cmd.FindRecordCmd;
import cn.jump.kademlia.transport.FindRecordMsg;
import cn.jump.kademlia.transport.Msg;

/**
 * @author JumpTian
 */
public class FindRecordHandler implements Handler {

    private final FindRecordCmd findRecordCmd;

    public FindRecordHandler(FindRecordCmd findRecordCmd) {
        this.findRecordCmd = findRecordCmd;
    }

    @Override
    public void handle(Msg inbound, int sessionId) {
        if (findRecordCmd.getRecord() != null) {
            return;
        }

        if (inbound instanceof FindRecordMsg) {
            FindRecordMsg findRecordMsg = (FindRecordMsg) inbound;
            findRecordCmd.getEndpoint().getRoutingTable().insert(findRecordMsg.getOriginNode());

            findRecordCmd.setRecord(findRecordMsg.getRecord());
        }
    }
}
