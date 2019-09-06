package cn.jump.kademlia.transport;

import cn.jump.kademlia.handler.Handler;
import cn.jump.kademlia.routing.Node;
import com.google.common.collect.Maps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author JumpTian
 */
public class TransportServer {

    private static final int BUFFER_SIZE = 64*1024;

    private final DatagramSocket datagramSocket;

    private static volatile boolean isRunning;
    private static Map<Long, Handler> handlerMap = Maps.newConcurrentMap();
    private static ExecutorService QUEUE = Executors.newFixedThreadPool(1);

    private TransportServer(int port) throws SocketException {
        this.datagramSocket = new DatagramSocket(port);
    }

    public static TransportServer newInstance(int port) throws SocketException {
        TransportServer transportServer = new TransportServer(port);
        QUEUE.submit(new Listener(transportServer.datagramSocket));
        isRunning = true;
        return transportServer;
    }

    public long sendMsg(Node target, Msg msg, Handler handler) throws IOException {
        if (!isRunning) {
            throw new IllegalStateException("TransportServer is down");
        }
        long sessionId = new Random().nextLong();

        if (handler != null) {
            handlerMap.put(sessionId, handler);
        }

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
        try {
            out.writeLong(sessionId);
            out.writeByte(msg.getType());
            msg.WriteToStream(out);

            byte[] data = bout.toByteArray();
            if (data.length > BUFFER_SIZE) {
                throw new IllegalArgumentException("Msg is too large");
            }

            DatagramPacket datagramPacket = new DatagramPacket(data, 0, data.length);
            datagramPacket.setSocketAddress(target.getAddress());
            datagramSocket.send(datagramPacket);
        } finally {
            bout.close();
            out.close();
        }

        return sessionId;
    }

    private static class Listener implements Runnable {

        private final DatagramSocket datagramSocket;

        public Listener(DatagramSocket datagramSocket) {
            this.datagramSocket = datagramSocket;
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    try {
                        byte[] buffer = new byte[BUFFER_SIZE];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        datagramSocket.receive(packet);

                        ByteArrayInputStream bin = new ByteArrayInputStream(
                                packet.getData(), packet.getOffset(), packet.getLength());
                        DataInputStream in = new DataInputStream(bin);

                        int sessionId = in.readInt();
                        byte msgType = in.readByte();

                        Msg msg = Factory.createMessage(msgType, in);

                        Handler handler;
                        if (handlerMap.containsKey(sessionId)) {
                            synchronized (this) {
                                handler = handlerMap.remove(sessionId);
                            }
                        } else {
                            handler = Factory.createReceiver(msgType, this);
                        }

                        if (handler != null) {
                            handler.handle(msg, sessionId);
                        }
                    } catch (IOException ignore) {
                    }
                }
            } finally {
                if (!datagramSocket.isClosed()) {
                    datagramSocket.close();
                }
                isRunning = false;
            }
        }
    }
}
