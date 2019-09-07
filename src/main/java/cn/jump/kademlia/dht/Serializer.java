package cn.jump.kademlia.dht;

/**
 * @author JumpTian
 */
public interface Serializer<T> {

    /**
     * 序列化指定对象为字节数组
     *
     * @param obj 对象
     * @return 字节数组
     */
    byte[] serialize(T obj);

    /**
     * 将字节数组反序列为相应对象
     *
     * @param data 字节数组
     * @return 对象
     */
    T deserialize(byte[] data);
}
