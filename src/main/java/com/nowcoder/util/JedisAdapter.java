package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by Administrator on 2018/7/29.
 */
@Service
public class JedisAdapter implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool = null;

    //JedisAdapter对象在初始化完之后，要把pool也初始化
    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);//不写参数也可以，默认就是localhost，6379

    }

    //获取redise的方法
    public Jedis getJedis(){
        return pool.getResource();
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    //用集合的概念实现赞和踩,包装集合
    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            //先从池里面取一个
            jedis = pool.getResource();
            //添加到集合
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //取消点赞，相当于把刚才加入到集合的用户删除
    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            //先从池里面取一个
            jedis = pool.getResource();
            //添加到集合
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //判断是否已经点过赞
    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            //先从池里面取一个
            jedis = pool.getResource();
            //添加到集合
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //查询集合里有多少人
    public long scard(String key){
        Jedis jedis = null;
        try{
            //先从池里面取一个
            jedis = pool.getResource();
            //添加到集合
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    //把对象序列化成一个函数串，保存到集合
    public void setObject(String key, Object obj) {
        set(key, JSON.toJSONString(obj));
    }

    //反序列化，获取对象的函数
    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            //把json串解析成文本
            return JSON.parseObject(value, clazz);
        }
        return null;
    }

    //插入队列
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    //弹出队列
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


}
    /*
    public static void main(String[] args){
        Jedis jedis = new Jedis();
        //先清空数据库
        jedis.flushAll();

        jedis.set("hello","world");
        System.out.println(jedis.get("hello"));
        //修改数据库的名字
        jedis.rename("hello","newhello");
        System.out.println(jedis.get("newhello"));
        //设置过期时间为30秒
        jedis.setex("hello3",30,"world");
        System.out.println(jedis.get("hello2"));

        jedis.set("pv","100");
        //加一操作
        jedis.incr("pv");
        System.out.println(jedis.get("pv"));
        //一次加入多个
        jedis.incrBy("pv",5);
        System.out.println(jedis.get("pv"));

        //列表操作
        String listName = "listA";
        for(int i = 0; i<10 ;i++){
            jedis.lpush("listName","a"+String.valueOf(i));//valueOf把int变字符串
        }
        System.out.println(jedis.lrange("listName",0,10));
        System.out.println(jedis.lpop("listName"));
        System.out.println(jedis.llen("listName"));
        //查找指定位置下标对应的列表
        System.out.println(jedis.lindex("listName",4));//pop弹出了几个

        //向指定元素前后位置插入数据
        System.out.println(jedis.linsert("listName", BinaryClient.LIST_POSITION.AFTER,"a4", "b1"));
        System.out.println(jedis.linsert("listName", BinaryClient.LIST_POSITION.BEFORE,"a4","b2"));
        System.out.println(jedis.lrange("listName",0,12));

        //使用hashSet，可以随意的添加扩展字段

        String userKey = "user12";
        jedis.hset(userKey,"name","jimimy");
        jedis.hset(userKey,"age","25");
        jedis.hset("user12","sex","男");
        jedis.hset(userKey,"phone","1825555444554");
        System.out.println(jedis.hget(userKey,"phone"));
        System.out.println(jedis.hget("user12","sex"));
        System.out.println(jedis.hget(userKey,"name"));
        System.out.println(jedis.hgetAll(userKey));
        //删除属性字段
        System.out.println(jedis.hdel(userKey,"phone"));
        System.out.println(jedis.hgetAll(userKey));
        //获取所有的key
        System.out.println(jedis.hkeys(userKey));
        //获取所有的val
        System.out.println(jedis.hvals(userKey));
        //判断某个属性是否存在
        System.out.println(jedis.hexists(userKey,"email"));

        //如果属性存在不添加，不存在就添加
        System.out.println(jedis.hsetnx(userKey,"school","abcd"));
        System.out.println(jedis.hsetnx(userKey,"name","zhx"));
        System.out.println(jedis.hgetAll(userKey));

        //Set集合
        String likeKeys1 = "newsLike1";
        String likeKeys2 = "newsLike2";

        for(int i = 0; i<10;i++){
            jedis.sadd(likeKeys1,String.valueOf(i));
            jedis.sadd(likeKeys2,String.valueOf(2*i));
        }
        System.out.println(jedis.smembers(likeKeys1));
        System.out.println(jedis.smembers(likeKeys2));

        //集合求交集
        System.out.println(jedis.sinter(likeKeys1,likeKeys2));
        //求并
        System.out.println(jedis.sunion(likeKeys1,likeKeys2));
        //取不同
        System.out.println(jedis.sdiff(likeKeys1,likeKeys2));
        //判断某个数值是否存在
        System.out.println(jedis.sismember(likeKeys1,"5"));
        //删除某个元素
        System.out.println(jedis.srem(likeKeys1,"5"));
        System.out.println(jedis.smembers(likeKeys1));

        //查看有多少个元素
        System.out.println(jedis.scard(likeKeys1));
        //把集合likeKeys2中的某个元素移到集合likeKeys1中
        System.out.println(jedis.smove(likeKeys2,likeKeys1,"14"));
        System.out.println(jedis.scard(likeKeys1));
        System.out.println(jedis.smembers(likeKeys1));

        //Sorted Set优先队列，用在排行榜
        String ranKey = "ranKey";
        jedis.zadd(ranKey,15,"jim");
        jedis.zadd(ranKey,60,"lucy");
        jedis.zadd(ranKey,78,"mary");
        jedis.zadd(ranKey,86,"alun");
        jedis.zadd(ranKey,90,"jack");
        jedis.zadd(ranKey,68,"dave");
        //查询集合中有多少个分数值
        System.out.println(jedis.zcard(ranKey));
        //查询某一范围的值的个数
        System.out.println(jedis.zcount(ranKey,50,90));
        //查询某一个人的分数
        System.out.println(jedis.zscore(ranKey,"lucy"));
        //给集合中某个人加分
        System.out.println(jedis.zincrby(ranKey,20,"lucy"));
        //给不存在集合中的人加分，会把这个人计入集合
        System.out.println(jedis.zincrby(ranKey,20,"lu"));
        System.out.println(jedis.zcount(ranKey,0,100));

        //查询排名前三的人
        System.out.println(jedis.zrevrange(ranKey,1,3));
        //默认是从小到大排序，最后三名
        System.out.println(jedis.zrange(ranKey,1,3));

        //打印所有人及对应的分数
        for(Tuple tuple : jedis.zrangeByScoreWithScores(ranKey,0,100)){
            System.out.println(tuple.getElement()+":" + String.valueOf(tuple.getScore()));
        }

        //查找某个人是第几名
        System.out.println(jedis.zrank(ranKey,"mary"));
        System.out.println(jedis.zrevrank(ranKey,"mary"));


        //redis是单线程的，用完后需要关闭
        JedisPool pool = new JedisPool();
        for(int i = 0 ; i<100 ;i++){
            Jedis j = pool.getResource();
            j.get("asdsa");
            System.out.println("POOL"+ i);
            j.close();
        }

    }
    */
