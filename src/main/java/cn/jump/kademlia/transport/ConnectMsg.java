package cn.jump.kademlia.transport;

import java.io.DataOutputStream;

/**
 * @author JumpTian
 */
public class ConnectMsg implements Msg {
    @Override
    public byte getType() {
        return 0;
    }

    @Override
    public void writeToStream(DataOutputStream out) {

    }
}
