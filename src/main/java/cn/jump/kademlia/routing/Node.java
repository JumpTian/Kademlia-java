package cn.jump.kademlia.routing;

import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Random;

/**
 * Kad网络中的节点抽象，包括比如节点端口、IP等路由
 * 信息。
 *
 * @author JumpTian
 */
@Getter
public class Node implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点id信息
     */
    private final Id id;
    /**
     * Socket连接地址
     */
    private final InetSocketAddress address;

    public Node(Id nodeId, InetSocketAddress address) {
        this.id = nodeId;
        this.address = address;
    }

    public Node(DataInputStream in) throws IOException {
        this.id = new Id(in);

        byte[] ip = new byte[4];
        in.readFully(ip);
        this.address = new InetSocketAddress(InetAddress.getByAddress(ip), in.readInt());
    }

    /**
     * 计算当前节点和指定节点的距离，该距离就是一个逻辑
     * 距离，它实际上就是在节点间Id的异或XOR值基础上计
     * 算Longest common prefix得到
     *
     * @param cmpNode 需要计算的peer节点
     * @return 逻辑距离值
     */
    public int calculateDistance(Node cmpNode) {
        byte[] xorArr = id.xor(cmpNode.getId());
        int distance = 0;
        for (byte xor : xorArr) {
            if (xor != 0) {
                int c = 0;
                for (int i = 7; i >= 0; i--) {
                    if ((xor & (1 << i)) == 0) {
                        c++;
                    } else {
                        break;
                    }
                }
                distance += c;
            } else {
                distance += 8;
            }
        }
        return distance;
    }

    /**
     * 将当前节点序列化到指定输出流中
     *
     * @param out 输出流
     * @throws IOException
     */
    public void writeToStream(DataOutputStream out) throws IOException {
        out.write(getId().getKeySpace());
        // ^_^
        byte[] addressArr = address.getAddress().getAddress();
        out.write(addressArr);
        out.write(address.getPort());
    }

    /**
     * 节点id
     */
    @Getter
    public static class Id implements Serializable  {
        private static final long serialVersionUID = 1L;

        public static final int SPACE = 160;
        private static final int SPACE_BYTE = SPACE/8;

        private byte[] keySpace;

        public Id() {
            keySpace = new byte[SPACE_BYTE];
            new Random().nextBytes(keySpace);
        }

        public Id(DataInputStream in) throws IOException {
            byte[] data = new byte[SPACE_BYTE];
            in.readFully(data);
            this.keySpace = data;
        }

        /**
         * 计算两节点id间的异或值
         *
         * @param cmpId 需要对比的节点id
         * @return 异或值
         */
        public byte[] xor(Id cmpId) {
            byte[] arr = new byte[SPACE_BYTE];
            for (int i = 0; i < SPACE_BYTE; i++) {
                arr[i] = (byte) (keySpace[i] ^ cmpId.getKeySpace()[i]);
            }
            return arr;
        }

        /**
         * 以十六进制格式返回节点id
         *
         * @return 节点id的十六进制表示
         */
        public String hexValue() {
            BigInteger bigInteger = new BigInteger(1, keySpace);
            return String.format("%0".concat(String.valueOf(keySpace.length << 1)).concat("X"), bigInteger);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final Id id = (Id) o;
            return Arrays.equals(keySpace, id.keySpace);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(keySpace);
        }
    }
}
