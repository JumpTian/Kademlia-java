package cn.jump.kademlia.cmd;

import cn.jump.kademlia.Endpoint;
import cn.jump.kademlia.KadConfig;
import cn.jump.kademlia.handler.AbstractHandler;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.routing.NodeComparator;
import cn.jump.kademlia.transport.AbstractMsg;
import cn.jump.kademlia.transport.TransportServer;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 节点路由查询指令抽象，封装了相关逻辑，作为FIND_NODE以
 * 及FIND_RECORD指令的公共父类。
 *
 * @author JumpTian
 */
@Getter
public abstract class AbstractFindCmd implements Cmd {

    public static final byte UNASKED = 0;
    public static final byte AWAITING = 1;
    public static final byte ASKED = 2;
    public static final byte FAILED = -1;

    protected final Endpoint endpoint;
    protected final TransportServer transportServer;

    protected final Map<Node, Byte> nodeMap;
    protected final Comparator comparator;
    protected final Map<Long, Node> msgTransitingMap = new ConcurrentHashMap<>();

    protected AbstractFindCmd(Endpoint endpoint, Node.Id lookupId, TransportServer transportServer) {
        this.endpoint = endpoint;
        this.transportServer = transportServer;
        this.comparator = new NodeComparator(lookupId);
        this.nodeMap = new TreeMap<>(comparator);
    }

    /**
     * 添加查询节点列表
     *
     * @param nodeList 节点列表
     */
    public void addNodeList(List<Node> nodeList) {
        for (Node node : nodeList) {
            if (!nodeMap.containsKey(node)) {
                nodeMap.put(node, UNASKED);
            }
        }
    }

    /**
     * 根据路由表对节点发起请求
     *
     * @return 是否成功
     * @throws IOException
     */
    public boolean askNodeFinish() throws IOException {
        if (KadConfig.maxMsgTransiting() <= msgTransitingMap.size()) {
            return false;
        }

        List<Node> unaskedNodeList = getNodeListByStatus(UNASKED, KadConfig.k());
        if (unaskedNodeList.isEmpty() && msgTransitingMap.isEmpty()) {
            return true;
        }
        Collections.sort(unaskedNodeList, this.comparator);

        for (int i = 0; msgTransitingMap.size() < KadConfig.maxMsgTransiting() && i < unaskedNodeList.size(); i++) {
            Node node = unaskedNodeList.get(i);
            long sessionId = transportServer.sendMsg(node, getMsg(), getHandler());

            nodeMap.put(node, AWAITING);
            msgTransitingMap.put(sessionId, node);
        }
        return false;
    }

    /**
     * 获取指定发送的请求消息
     *
     * @return 请求消息
     */
    protected abstract AbstractMsg getMsg();

    /**
     * 获取请求响应回调处理器
     *
     * @return 响应处理器
     */
    protected abstract AbstractHandler getHandler();

    /**
     * 获取最多指定个数的指定状态的节点
     *
     * @param status 节点状态
     * @param count 返回个数，0或负数表示返回全部
     * @return 节点列表
     */
    protected List<Node> getNodeListByStatus(byte status, int count) {
        List<Node> nodeList = Lists.newArrayList();
        for (Map.Entry<Node, Byte> entry : nodeMap.entrySet()) {
            if (entry.getValue() == status) {
                nodeList.add(entry.getKey());
                if (nodeList.size() == count) {
                    break;
                }
            }
        }
        return nodeList;
    }
}
