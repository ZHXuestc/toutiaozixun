package com.nowcoder.async;

/**
 * Created by Administrator on 2018/7/30.
 */

//定义枚举类型
public enum EventType {

    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;
    EventType(int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
