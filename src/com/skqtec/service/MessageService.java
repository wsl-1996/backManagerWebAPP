package com.skqtec.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.MessageEntity;
import com.skqtec.repository.MessageRepository;
import com.skqtec.tools.RedisAPI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class MessageService {

    static Logger logger = Logger.getLogger(MessageService.class.getName());

    @Autowired
    MessageRepository messageRepository;

    /**
     * 发送消息（消息存储至redis和mysql）
     * @param messageTo 接受者id
     * @param messageFrom 发送者id，用户id或管理员id
     * @param messageContent 消息内容
     * @param headOwner 发送者头像（后台管理发送至小程序时为空）
     * @param contentType 消息的内容类型
     * @param messageType 消息的类型
     * @return
     */
    public boolean sendMessage(String messageTo, String messageFrom, String messageContent, String headOwner, String contentType, String messageType){
        try{
            String uuid = UUID.randomUUID().toString().replace("-", "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageFrom", messageFrom);
            jsonObject.put("id", uuid);
            jsonObject.put("messageContent", messageContent);
            jsonObject.put("contentType", contentType);
            jsonObject.put("messageType", messageType);
            jsonObject.put("headOwner",headOwner);
            DateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
            String date=sdf.format(new Date());
            jsonObject.put("createTime", date);
            JedisPool pool = RedisAPI.getPool();
            Jedis jedis = pool.getResource();
            String jsonStr=jsonObject.toString();
            jsonStr.replace("{\"","{'");
            jsonStr.replace("\":","':");
            jsonStr.replace(",\"",",'");
            jedis.lpush(messageTo, jsonStr);
            pool.returnResource(jedis);
            //存入数据库
            MessageEntity message = new MessageEntity();
            message.setId(uuid);
            message.setContent(messageContent);
            message.setTime(new Timestamp(new Date().getTime()));
            message.setContentType(Integer.parseInt(contentType));
            message.setMessageType(Integer.parseInt(messageType));
            message.setFromUserId(messageFrom);
            message.setToUserId(messageTo);
            String key = messageRepository.save(message);
            if(key==null||key==""){
                return false;
            }
            else{
                logger.info("send message successed:"+ JSON.toJSONString(message));
                return true;
            }
        }
        catch (Exception e){
            logger.error(e);
            return false;
        }

    }

}
