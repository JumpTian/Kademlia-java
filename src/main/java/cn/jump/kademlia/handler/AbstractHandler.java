package cn.jump.kademlia.handler;

import cn.jump.kademlia.transport.AbstractMsg;
import cn.jump.kademlia.transport.TransportServer;

import java.io.IOException;

/**
 * 处理接收到的指令/请求
 *
 * @author JumpTian
 */
public abstract class AbstractHandler {

    /**
     * 处理接收到的消息
     *
     * @param inbound 接收消息
     * @param sessionId 会话id
     * @throws IOException
     */
    public abstract void handle(AbstractMsg inbound, int sessionId) throws IOException;

    /**
     * 根据不同消息类型创建不同的Handler实例
     *
     * @param type 消息类型
     * @param transportServer 服务类
     * @return Handler实例
     */
    public static AbstractHandler createHandler(byte type, TransportServer transportServer) {
        switch (type) {
            case AbstractMsg.TYPE_CONNECT:
                //return new ConnectHandler(this);
            case AbstractMsg.TYPE_STORE:
                //return new StoreHandler();
            default:
                throw new IllegalStateException("Found invalid handler type");
        }
    }
}
