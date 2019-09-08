package cn.jump.kademlia.handler;

import cn.jump.kademlia.cmd.ConnectCmd;
import cn.jump.kademlia.transport.AbstractMsg;
import cn.jump.kademlia.transport.ConnectMsg;

/**
 * @author JumpTian
 */
public class ConnectHandler extends AbstractHandler {

    private ConnectCmd connectCmd;

    public ConnectHandler(ConnectCmd connectCmd) {
        this.connectCmd = connectCmd;
    }

    @Override
    public void handle(AbstractMsg inbound, int sessionId) {
        if (!(inbound instanceof ConnectMsg)) {
            return;
        }

        connectCmd.getEndpoint().getRoutingTable().insert(connectCmd.getBootstrapNode());
        connectCmd.notify();
    }
}
