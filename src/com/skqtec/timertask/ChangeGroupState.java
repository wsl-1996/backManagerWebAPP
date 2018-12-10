package com.skqtec.timertask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.GroupEntity;
import com.skqtec.repository.GroupRepository;
import com.skqtec.tools.RedisAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class ChangeGroupState {
    @Autowired
    private GroupRepository groupRepository;
    //将团购结束时间存入redis
    public static void addToRedis(String groupId, Timestamp endTime){
        JedisPool pool=RedisAPI.getPool();
        Jedis j=pool.getResource();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("groupId",groupId);
        jsonObject.put("endTime",endTime);
        j.lpush("changeGroupState",jsonObject.toString());
        pool.returnResource(j);
    }
    //判断团购是否到期
    @Scheduled(cron = "0 * * * * ? ")
    public void changeGroupState(){

        JedisPool pool=RedisAPI.getPool();
        Jedis j=pool.getResource();
        for(int i=0;i<j.llen("changeGroupState");i++){
            JSONObject jsonObject=JSON.parseObject(j.lindex("changeGroupState",i));
            String groupId=(String)jsonObject.get("groupId");
            long endTime=(Long)jsonObject.get("endTime");
            long currentTime=new Date().getTime();
            if(currentTime-endTime>0){
                GroupEntity group=groupRepository.get(groupId);
                group.setGroupState(0);
                groupRepository.saveOrUpdate(group);
                j.lrem("changeGroupState",0,jsonObject.toString());

            }
        }
        pool.returnResource(j);
    }
}
