package cn.jump.kademlia.transport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author JumpTian
 */
public abstract class AbstractMsg {

    public static final byte TYPE_CONNECT = 1;
    public static final byte TYPE_FIND_NODE = 2;
    public static final byte TYPE_FIND_RECORD = 3;
    public static final byte TYPE_FIND_NODE_REPLY = 4;
    public static final byte TYPE_STORE = 5;

    /**
     * 获取消息类型
     *
     * @return 消息类型
     */
    abstract byte getType();

    /**
     * 序列化消息到指定输出流中
     *
     * @param out 输出流
     * @throws IOException
     */
    abstract void writeToStream(DataOutputStream out) throws IOException;

    /**
     * 根据不同消息类型从输入流实例化消息
     *
     * @param type 消息类型
     * @param in 输入流
     * @return 消息
     */
    public static AbstractMsg createMsg(byte type, DataInputStream in) throws IOException {
        switch (type) {
            case TYPE_CONNECT:
                return new ConnectMsg(in);
            case TYPE_FIND_NODE:
                return new FindNodeMsg(in);
            case TYPE_FIND_RECORD:
                return new FindRecordMsg(in);
            case TYPE_FIND_NODE_REPLY:
                return new FindNodeReply(in);
            case TYPE_STORE:
                return new StoreMsg(in);
            default:
               throw new IllegalStateException("Found invalid msg type");
        }
    }
}
