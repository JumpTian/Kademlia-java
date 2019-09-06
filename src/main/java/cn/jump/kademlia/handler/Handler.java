package cn.jump.kademlia.handler;

import cn.jump.kademlia.transport.Msg;

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
     */
    void handle(Msg inbound, int sessionId);
}
