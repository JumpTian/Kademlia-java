package cn.jump.kademlia;

import cn.jump.kademlia.routing.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author JumpTian
 */
public class EndpointTest {

    @Before
    public void setup() {
    }

    @Test
    public void testBootstrap() {
        Endpoint localEndpoint = null;
        try {
            localEndpoint = new Endpoint(new Node.Id(), 18888);
            Endpoint remoteEndpoint = new Endpoint(new Node.Id(), 18888);
            localEndpoint.bootstrap(remoteEndpoint.getLocalNode());
        } catch (Exception e) {
        }
        Assert.assertNotNull(localEndpoint);
        Assert.assertTrue(localEndpoint.getRoutingTable().getBucketArr().length > 0);
    }
}
