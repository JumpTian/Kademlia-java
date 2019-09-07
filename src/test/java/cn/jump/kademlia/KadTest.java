package cn.jump.kademlia;

import cn.jump.kademlia.dht.Record;
import cn.jump.kademlia.dht.RecordImpl;
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

    @Before
    public void setup() throws Exception {
        nodeId = new Node.Id();
        endpoint = new Endpoint(nodeId, 88888);
    }

    @Test
    public void testBootstrap() throws Exception {
        Endpoint peerEndpoint = new Endpoint(new Node.Id(), 99999);
        endpoint.bootstrap(peerEndpoint.getLocalNode());

        Assert.assertNotNull(peerEndpoint);
        Assert.assertTrue(endpoint.getRoutingTable().getBucketArr().length > 0);
    }

    @Test
    public void testStore() throws Exception {
        Endpoint peerEndpoint = new Endpoint(new Node.Id(), 99999);
        endpoint.bootstrap(peerEndpoint.getLocalNode());

        String data = "China";
        Record record = new RecordImpl(nodeId, data);
        endpoint.put(record);
    }

    @Test
    public void testGet() throws Exception {
        Node.Id peerId = new Node.Id();
        Endpoint peerEndpoint = new Endpoint(peerId, 99999);
        endpoint.bootstrap(peerEndpoint.getLocalNode());

        endpoint.get(new FindParam.Builder().key(peerId).build());
    }
}
