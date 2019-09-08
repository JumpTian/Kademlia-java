package cn.jump.kademlia.cmd;

import java.io.IOException;

/**
 * Kad网络指令
 *
 * @author JumpTian
 */
public interface Cmd {

    /**
     * 执行具体执行
     *
     * @throws IOException
     */
    void execute() throws IOException;
}
