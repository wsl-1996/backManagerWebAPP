package com.skqtec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.repository.MessageRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Controller
public class SystemWebSocketHandler implements WebSocketHandler {
    static Logger logger = Logger.getLogger(SystemWebSocketHandler.class.getName());
    @Autowired
    private MessageRepository messageRepository;
    //private String sessionKey;
    public static HashMap<String,WebSocketSession> sessions = new HashMap<String, WebSocketSession>();
    private static HashMap<String,String>sessionKey=new HashMap<String, String>();
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("ConnectionEstablished:"+session.getId());
    }
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> msg) throws Exception {

        String chatMessage=(String) msg.getPayload();
        System.out.println(chatMessage);
        JSONObject j=JSON.parseObject(chatMessage);
        String messageFrom=(String)j.get("messageFrom");
       // String messageTo=(String)j.get("messageTo");
        String messageContent=(String)j.get("messageContent");
        String contentType=(String)j.get("contentType");
        String messageType=(String)j.get("messageType");
        //String headOwner=(String)j.get("headOwner");
        if(messageType.equals("-1")){
            sessionKey.put(session.getId(),messageFrom);
            sessions.put(messageFrom,session);
            logger.info("用户：" + messageFrom+" 已成功登陆");
            return;
        }
    }
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        String key=sessionKey.get(session.getId());
        sessions.remove(key);
    }
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("ConnectionClosed:"+session.getId());
        String key=sessionKey.get(session.getId());
        sessions.remove(key);
        logger.info("用户："+key+" 已成功下线");
    }
    public boolean supportsPartialMessages() {
        return false;
    }
}
