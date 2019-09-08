package cn.jump.kademlia.dht;

import cn.jump.kademlia.KadConfig;
import cn.jump.kademlia.routing.Node;
import cn.jump.kademlia.transport.FindParam;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Kad节点的本地存储表
 *
 * @author JumpTian
 */
public class Table {

    private static final String FILE_SUFFIX = ".kad";

    private final String ownerId;

    private Serializer<KadRecord> serializer;

    private final Map<Node.Id, List<KadRecord>> recordMap = new ConcurrentHashMap<>();

    public Table(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 获取本地存储表中所有记录
     *
     * @return 记录列表
     */
    public List<KadRecord> getAllRecord() {
        List<KadRecord> recordList = Lists.newArrayList();
        for (List<KadRecord> entry : recordMap.values()) {
            if (entry.size() > 0) {
                recordList.addAll(entry);
            }
        }
        return recordList;
    }

    /**
     * 根据指定输入记录检查本地是否已经存储，如果包含则返
     * 回存储的记录，否则返回null。
     *
     * @param input 待获取输入
     * @return 存储的记录
     */
    public KadRecord get(KadRecord input) {
        if (input != null && recordMap.containsKey(input.getNodeId())) {
            for (KadRecord record : recordMap.get(input.getNodeId())) {
                if (record.satisfy(input.getOwnerId(), input.getNodeId())) {
                    return record;
                }
            }
        }
        return null;
    }

    /**
     * 根据指定条件检查本地是否已经存储，如果包含则返回存
     * 储的记录，否则返回null。
     *
     * @param param 条件
     * @return 存储的记录
     */
    public KadRecord get(FindParam param) {
        if (param != null && recordMap.containsKey(param.getKey())) {
            for (KadRecord record : recordMap.get(param.getKey())) {
                //if (record.satisfy(param.getOwnerId(), param.getNodeId())) {
                    return record;
                //}
            }
        }
        return null;
    }

    /**
     * 检查本地存储表中是否包含指定记录
     *
     * @param input 待检查记录
     * @return 是否包含
     */
    public boolean contains(KadRecord input) {
        return get(input) != null;
    }

    /**
     * 检查本地存储表中是否满足指定条件
     *
     * @param param 条件
     * @return 是否满足
     */
    public boolean contains(FindParam param) {
        return get(param) != null;
    }

    /**
     * 存储记录到本地表中
     *
     * @param input 需存储的记录，不可为null
     * @return 操作是否成功
     * @throws IOException
     */
    public boolean put(KadRecord input) throws IOException {
        Preconditions.checkNotNull(input);

        KadRecord record = get(input);
        if (record != null) {
            record.updateRepublishTime();

            if (record.getLastUpdateTime() >= input.getLastUpdateTime()) {
                return false;
            } else {
                remove(input);
            }
        }

        DataOutputStream out = null;
        try {
            String fileName = geInnerFileName(input.getNodeId(), input);
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
     * 从本地存储表中删除指定记录
     *
     * @param record 需删除的记录
     * @return 操作是否成功
     */
    public boolean remove(KadRecord record) {
        if (!contains(record)) {
            return false;
        }
        File file = new File(geInnerFileName(record.getNodeId(), record));
        if (!file.exists()) {
            return false;
        }

        recordMap.get(record.getNodeId()).remove(record);
        file.delete();
        return true;
    }


    /**
     * 根据节点id获取记录存储的文件夹，此时不会自动创建
     * 文件夹。
     *
     * @param nodeId 节点id，不可为null
     * @return 独立于系统的文件夹名
     */
    private String geInnerFolderName(Node.Id nodeId) {
        Preconditions.checkNotNull(nodeId);

        String folderName = nodeId.hexValue().substring(0, 5);
        File folder = new File(KadConfig.getNodeFolder(ownerId).concat(File.separator).concat(folderName));
        return folder.toString();
    }

    /**
     * 根据节点id和存储记录获取记录存储的文件名，此时不会
     * 创建文件但会静默创建文件夹。
     *
     * @param nodeId 节点id，不可为null
     * @param record 需存储的记录，不可为null
     * @return 独立于系统的文件名
     */
    private String geInnerFileName(Node.Id nodeId, KadRecord record) {
        Preconditions.checkNotNull(record);

        File folder = new File(geInnerFolderName(nodeId));
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        return folder.toString().concat(File.separator)
                .concat(String.valueOf(record.hashCode())).concat(FILE_SUFFIX);
    }

    /**
     * 获取记录的序列化器
     *
     * @return 序列化器
     */
    private Serializer getSerializer() {
        if (serializer == null) {
            serializer = new JsonSerializer();
        }
        return serializer;
    }
}
