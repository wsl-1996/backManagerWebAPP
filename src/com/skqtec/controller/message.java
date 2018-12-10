package com.skqtec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.MessageEntity;
import com.skqtec.repository.MessageRepository;
import com.skqtec.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/applet/message")
public class message {
    static Logger logger = Logger.getLogger(message.class.getName());

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    /**
     * 发送消息处理，先存redis再存mysql
     * @param request
     * @return
     */
    @RequestMapping(value="/sendMessage",method = RequestMethod.GET)
    public @ResponseBody ResponseData sendMessages(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String data=request.getParameter("data");
        JSONObject j=JSON.parseObject(data);
        String messageFrom=(String)j.get("messageFrom");
        String messageTo=(String)j.get("messageTo");
        String messageContent=(String)j.get("messageContent");
        String contentType=(String)j.get("contentType");
        String messageType=(String)j.get("messageType");
        String headOwner=(String)j.get("headOwner");
        boolean sendResult = messageService.sendMessage(messageTo,messageFrom,messageContent,headOwner,contentType,messageType);
        if(!sendResult){
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.SEND_MESSAGE_FAILED);
        }
        return responseData;
    }

    /**
     * 处理消息后，更新消息的状态
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/handledMessage",method=RequestMethod.GET)
    public @ResponseBody ResponseData handledMessage(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String id=request.getParameter("messageId");
        try {
            MessageEntity message = messageRepository.get(id);
            message.setState(1);   //0：未读，1：已读，2：重点标记
            messageRepository.saveOrUpdate(message);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage("更新消息的状态失败，消息id为："+id);
        }
        finally {
            return responseData;
        }
    }



}
