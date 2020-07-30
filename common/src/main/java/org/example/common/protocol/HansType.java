package org.example.common.protocol;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 12:09
 */
public enum HansType {
    REGISTER(0xefe),
    REGISTER_RESP(0xefe << 1),
    ACTIVE(0xefe << 2),
    INACTIVE(0xefe << 3),
    HEART(0xefe << 4),
    READ(0xefe << 5),
    ;

    int type;

    HansType(int i) {
        this.type = i;
    }
    public int getCode() {
        return type;
    }
    public static HansType valueOf(int i) {
        for (HansType status : HansType.values()) {
            if (status.type == i) {
                return status;
            }
        }
        return null;
    }
}
