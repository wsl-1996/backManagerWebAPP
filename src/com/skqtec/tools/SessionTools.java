package com.skqtec.tools;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

public class SessionTools {
    //查询session
    public static String sessionQuery(String sessionId){
        JedisPool jedisPool=RedisAPI.getPool();
        Jedis jedis=jedisPool.getResource();
        String userId=jedis.get(sessionId);
        if(userId!=null)
        jedis.expire(sessionId,60*60*24*3);
        jedisPool.returnResource(jedis);
        return userId;
    }
    //生成session
    public static String setSession(String userId){
        JedisPool jedisPool=RedisAPI.getPool();
        Jedis jedis=jedisPool.getResource();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        jedis.set(uuid,userId);
        jedis.expire(uuid,600);
        jedisPool.returnResource(jedis);
        return uuid;
    }

}
