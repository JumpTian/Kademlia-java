package cn.jump.kademlia.dht;

import cn.jump.kademlia.routing.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author TianYiming
 */
public class TableTest {

    private String ownerId;
    private Table table;

    @Before
    public void setup() {
        ownerId = "Jump-Owner";
        table = new Table(ownerId);
    }

    @Test
    public void testGeIRecord() {
        Node.Id nodeId = new Node.Id();
        String data = "Test data";
        KadRecord record = table.get(new DefaultKadRecord(nodeId, ownerId, data));
        Assert.assertNotNull(record);
    }
}
