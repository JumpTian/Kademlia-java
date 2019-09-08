package cn.jump.kademlia;

import cn.jump.kademlia.dht.DefaultKadRecord;
import cn.jump.kademlia.dht.KadRecord;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.FindParam;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author JumpTian
 */
public class KadTest {

    private Node.Id nodeId;
    private Endpoint endpoint;
    private String ownerId;

    @Before
    public void setup() throws Exception {
        ownerId = "Jump-owner";
        nodeId = new Node.Id();
        endpoint = Endpoint.newInstance(nodeId, ownerId, 88888);
    }

    @Test
    public void testBootstrap() throws Exception {
        Endpoint peerEndpoint = Endpoint.newInstance(nodeId, ownerId, 88888);
        endpoint.bootstrap(peerEndpoint.getLocalNode());

        Assert.assertNotNull(peerEndpoint);
        Assert.assertTrue(endpoint.getRoutingTable().getBucketArr().length > 0);
    }

    @Test
    public void testStore() throws Exception {
        Endpoint peerEndpoint = Endpoint.newInstance(nodeId, ownerId, 88888);
        endpoint.bootstrap(peerEndpoint.getLocalNode());

        String data = "China";
        KadRecord record = new DefaultKadRecord(nodeId, ownerId, data);
        endpoint.put(record);
    }

    @Test
    public void testGet() throws Exception {
        Node.Id peerId = new Node.Id();
        Endpoint peerEndpoint = Endpoint.newInstance(nodeId, ownerId, 88888);
        endpoint.bootstrap(peerEndpoint.getLocalNode());

        endpoint.get(new FindParam.Builder().key(peerId).build());
    }
}
