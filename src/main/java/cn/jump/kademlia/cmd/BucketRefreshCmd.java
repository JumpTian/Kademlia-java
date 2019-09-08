package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.TransportServer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bucket更新指令，定时更新
 *
 * @author JumpTian
 */
public class BucketRefreshCmd {

    private final Endpoint endpoint;
    private final TransportServer transportServer;

    private final ExecutorService POOL = Executors.newFixedThreadPool(1);

    private BucketRefreshCmd(Endpoint endpoint, TransportServer transportServer) {
        this.endpoint = endpoint;
        this.transportServer = transportServer;
    }

    public static BucketRefreshCmd fire(Endpoint endpoint, TransportServer transportServer) {
        BucketRefreshCmd cmd = new BucketRefreshCmd(endpoint, transportServer);
        cmd.execute();
        return cmd;
    }

    /**
     * 因为在分布式网络中，节点up/down是常态，所以需要每间隔指定时间都
     * 刷新Bucket以避免contact失效
     */
    private void execute() {
        for (int i = 1; i < Node.Id.SPACE; i++) {
            Node.Id nodeId = endpoint.getLocalNode().getId();
            POOL.execute(() -> {
                try {
                    FindNodeCmd.fire(endpoint, nodeId, transportServer);
                } catch (IOException ignore) {
                }
            });
        }
    }
}
