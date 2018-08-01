package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/7/29.
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 喜欢返回1，不喜欢返回-1，否则返回0
     * 某个用户对某个元素是否喜欢
     * @param userId
     * @param eneityType
     * @param eneityId
     * @return
     */
    //在redis中所有函数调用时都有一个key，所有key的开头都表示业务
    public int getLikeStatus(int userId , int eneityType , int eneityId){
        String likeKey = RedisKeyUtil.getLikeKey(eneityId,eneityType);
        //判断likeKey中有没有userid，如果有就是喜欢
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(eneityId,eneityType);
        //判断disLikeKey中有没有userid，如果有就是不喜欢
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1 : 0;
    }

    //增加喜欢的方法
    public long like(int userId , int eneityType , int eneityId){
        // 在喜欢集合里增加
        String likeKey = RedisKeyUtil.getLikeKey(eneityId,eneityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));

        // 从反对里删除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(eneityId,eneityType);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        //返回喜欢集合的个数
        return jedisAdapter.scard(likeKey);
    }

    //增加不喜欢的方法
    public long disLike(int userId , int eneityType , int eneityId){
        // 在不喜欢集合里增加
        String disLikeKey = RedisKeyUtil.getDisLikeKey(eneityId,eneityType);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));

        // 从喜欢里删除
        String likeKey = RedisKeyUtil.getLikeKey(eneityId,eneityType);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        //返回不喜欢集合的个数
        return jedisAdapter.scard(disLikeKey);
    }

}
