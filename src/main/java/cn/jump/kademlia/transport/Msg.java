package cn.jump.kademlia.transport;

import java.io.DataOutputStream;

/**
 * @author JumpTian
 */
public interface Msg {

    /**
     * 获取消息类型
     *
     * @return 消息类型
     */
    byte getType();

    /**
     * 序列化消息到指定输出流中
     *
     * @param out 输出流
     */
    void writeToStream(DataOutputStream out);

}
