package com.nowcoder.util;

/**
 * Created by Administrator on 2018/7/29.
 */
//根据规范生成key,因为redis的有很多key，所以要用专门的规范进行管理，以免出错
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    //队列的名字，用于存放事件的key
    private static String BIZ_EVENT = "EVENT";

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

    //喜欢的key的规范
    public static String getLikeKey(int eneityId , int eneityType){
        return BIZ_LIKE + SPLIT + String.valueOf(eneityType) + SPLIT +String.valueOf(eneityId);
    }

    //不喜欢的key的规范
    public static String getDisLikeKey(int eneityId , int eneityType){
        return BIZ_DISLIKE + SPLIT + String.valueOf(eneityType) + SPLIT +String.valueOf(eneityId);
    }
}
