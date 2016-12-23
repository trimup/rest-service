package com.lihe.until;

/**
 * Created by trimup on 2016/8/24.
 */
public interface Serializable {
    byte[] serialize();
    void unserialize(byte[] ss);
}
