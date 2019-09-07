package cn.jump.kademlia.handler;

import cn.jump.kademlia.transport.Msg;

import java.io.IOException;

/**
 * 处理接收到的指令/请求
 *
 * @author JumpTian
 */
public interface Handler {

    /**
     * 处理接收到的消息
     *
     * @param inbound 接收消息
     * @param sessionId 会话id
     * @throws IOException
     */
    void handle(Msg inbound, int sessionId) throws IOException;
}
