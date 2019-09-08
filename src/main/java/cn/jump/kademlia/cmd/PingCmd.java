package cn.jump.kademlia.cmd;

import cn.jump.kademlia.routing.Node;
import lombok.Getter;

import java.io.IOException;

/**
 * 测试一个节点是否在线
 *
 * @author JumpTian
 */
@Getter
public class PingCmd implements Cmd {

    private Node peerNode;
    private boolean isAck;

    private PingCmd(Node peerNode) {
        this.peerNode = peerNode;
    }

    public static PingCmd fire(Node peerNode) throws IOException {
        PingCmd cmd = new PingCmd(peerNode);
        cmd.execute();
        return cmd;
    }

    @Override
    public void execute() throws IOException {
        //todo
    }


}
