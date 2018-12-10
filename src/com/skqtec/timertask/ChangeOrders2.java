package com.skqtec.timertask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.OrderEntity;
import com.skqtec.repository.OrderRepository;
import com.skqtec.tools.RedisAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class ChangeOrders2 {
    @Autowired
    private OrderRepository orderRepository;
    public static void addToRedis(String orderId,Timestamp creatTime){
        JedisPool pool=RedisAPI.getPool();
        Jedis j=pool.getResource();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("orderId",orderId);
        jsonObject.put("creatTime",creatTime);
        j.lpush("changeOrder",jsonObject.toString());
        pool.returnResource(j);
    }

    /**
     * 定时任务：订单下单1小时未付款，自动将订单取消
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void run(){
        JedisPool pool=RedisAPI.getPool();
        Jedis j=pool.getResource();
        for(int i=0;i<j.llen("changeOrder");i++){
            JSONObject jsonObject=JSON.parseObject(j.lindex("changeOrder",i));
            String orderId=(String)jsonObject.get("orderId");
            long creatTime=(Long)jsonObject.get("creatTime");
            if((new Date().getTime()-creatTime)/(1000*60)>60){
                OrderEntity order=orderRepository.get(orderId);
                if(order!=null){
                    order.setState(6);   //6已取消
                    orderRepository.saveOrUpdate(order);
                    j.lrem("changeOrder",1,jsonObject.toString());
                }
            }
        }
        pool.returnResource(j);
    }
}
