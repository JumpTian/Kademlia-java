package cn.jump.kademlia.dht;

import cn.jump.kademlia.routing.Node;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 节点本地存储表
 *
 * @author JumpTian
 */
public class Table {

    private Serializer serializer;

    private final Map<Node.Id, List<Record>> recordMap = new ConcurrentHashMap<>();

    public Record get(Node.Id nodeId) {
        if (recordMap.containsKey(nodeId)) {
            for (Record record : recordMap.get(nodeId)) {
                //todo 检查是否是符合
            }
        }
        throw new IllegalStateException("Not found record");
    }

    public boolean store(Record record) throws IOException {
        DataOutputStream out = null;
        try {
            String fileName = geInnerFileName(record.getNodeId());
            out = new DataOutputStream(new FileOutputStream(fileName));

            byte[] data = getSerializer().serialize(record);
            out.write(data);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return true;
    }

    /**
     * 根据节点id获取内容存储的文件。
     *
     * 每个存储记录放在节点id的首2位自负的文件夹下，且其文件
     * 名就是文件包含内容的哈希值。
     *
     * @param nodeId 节点id
     * @return 独立于系统的文件名
     */
    private String geInnerFileName(Node.Id nodeId) {
        String folderName = nodeId.hexValue().substring(0, 2);
        File folder = new File("ownerId".concat(File.separator).concat(folderName));
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        return folderName.toString();
    }

    private Serializer getSerializer() {
        if (serializer == null) {
            serializer = new JsonSerializer();
        }
        return serializer;
    }
}
