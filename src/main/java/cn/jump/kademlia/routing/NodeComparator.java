package cn.jump.kademlia.routing;

import java.math.BigInteger;
import java.util.Comparator;

/**
 * @author JumpTian
 */
public class NodeComparator implements Comparator<Node> {

    private Node.Id nodeId;

    public NodeComparator(Node.Id nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public int compare(Node cmp1, Node cmp2) {
        BigInteger b1 = new BigInteger(1, cmp1.getId().getKeySpace());
        BigInteger b2 = new BigInteger(1, cmp2.getId().getKeySpace());

        b1 = b1.xor(new BigInteger(1, nodeId.getKeySpace()));
        b2 = b2.xor(new BigInteger(1, nodeId.getKeySpace()));

        return b1.abs().compareTo(b2.abs());
    }
}
