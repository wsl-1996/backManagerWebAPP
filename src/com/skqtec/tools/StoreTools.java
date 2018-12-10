package com.skqtec.tools;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class StoreTools {
    public static void storeInfo(String key,String value,int second){
        JedisPool jedisPool=RedisAPI.getPool();
        Jedis jedis=jedisPool.getResource();
        //System.out.println("连接成功");
        jedis.set(key,value);
        jedis.expire(key,second);
        jedisPool.returnResource(jedis);
    }
    public static  String getInfo(String key){
        JedisPool jedisPool=RedisAPI.getPool();
        Jedis jedis=jedisPool.getResource();
        //System.out.println("连接成功");
        String value=jedis.get(key);
        jedisPool.returnResource(jedis);
        return value;
    }
}
