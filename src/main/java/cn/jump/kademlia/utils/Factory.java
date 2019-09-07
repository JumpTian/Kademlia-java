package cn.jump.kademlia.utils;

import cn.jump.kademlia.handler.Handler;
import cn.jump.kademlia.transport.Msg;
import cn.jump.kademlia.transport.TransportServer;

import java.io.DataInputStream;

/**
 * @author TianYiming
 */
public class Factory {
    public static Msg createMessage(byte msgType, DataInputStream in) {
        return null;
    }

    public static Handler createReceiver(byte msgType, TransportServer.Listener listener) {
        return null;
    }
}
