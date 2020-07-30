package org.example.common.protocol;


import java.util.Arrays;
import java.util.Map;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 11:21
 */
public class HansMessage {
    private HansType type;
    private Map<String, Object> meta;
    private byte[] data;

    @Override
    public String toString() {
        return "HansMessage{" +
                "type=" + type +
                ", meta=" + meta +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public HansType getType() {
        return type;
    }

    public void setType(HansType type) {
        this.type = type;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
