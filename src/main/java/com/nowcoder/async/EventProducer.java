package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/7/30.
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 把传递过来的事件放入到队列里面
     * @param eventModel
     * @return
     */
    public  boolean fireEvent(EventModel eventModel){
        try{
            //先把事件序列化
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            //加入队列
            jedisAdapter.lpush(key, json);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
