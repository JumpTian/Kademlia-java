package cn.jump.kademlia.dht;

import com.alibaba.fastjson.JSON;

/**
 * 基于FastJson的序列化器
 *
 * @author JumpTian
 */
public class JsonSerializer<T> implements Serializer<T> {

    @Override
    public byte[] serialize(T obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public T deserialize(byte[] data) {
        return (T) JSON.parse(data);
    }
}
