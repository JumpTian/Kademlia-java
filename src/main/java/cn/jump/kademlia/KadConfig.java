package cn.jump.kademlia;

/**
 * @author JumpTian
 */
public class KadConfig {

    public static int maxMsgTransiting() {
        return 3;
    }

    public static int k() {
        return 5;
    }

    public static int socketTimeout() {
        return 1000;
    }
}
