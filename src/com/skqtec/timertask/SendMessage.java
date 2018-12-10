package com.skqtec.timertask;

import com.skqtec.controller.SystemWebSocketHandler;
import com.skqtec.tools.RedisAPI;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
//发送redis中的消息给客户端
@Component
public class SendMessage{
    Logger logger=Logger.getLogger(SendMessage.class.getName());
    @Scheduled(cron = "0/1 * * * * ?")
    public void sendMessage(){
        HashMap<String,WebSocketSession> sessions=SystemWebSocketHandler.sessions;
        //System.out.println((new Date())+"start111");
       JedisPool pool=RedisAPI.getPool();
       Jedis jedis=pool.getResource();
       try{
           for (HashMap.Entry<String, WebSocketSession>entry: sessions.entrySet()) {
               String messageFrom = entry.getKey();
               //logger.info(new Date() + "接受者ID" + messageFrom);
               WebSocketSession session = entry.getValue();

               while (jedis.exists(messageFrom) && jedis.llen(messageFrom) > 0) {
                   if (session.isOpen()) {
                       String msg = jedis.lpop(messageFrom);
                       logger.info(msg);
                       TextMessage textMessage = new TextMessage(msg, true);
                       synchronized (session) {
                           session.sendMessage(textMessage);
                           logger.info("**sendSuccess:" + textMessage.getPayload());
                       }
                   } else {
                       logger.info("connect not open："+messageFrom);
                       break;
                   }
               }
           }
       }catch(Exception e){
           logger.error(e);
       }finally{
            pool.returnResource(jedis);
       }
   }
}
