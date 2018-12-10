package com.skqtec.controller;


import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.AuthorityEntity;
import com.skqtec.repository.AuthorityRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/authoritys")
public class authoritys {

    static Logger logger = Logger.getLogger(authoritys.class.getName());

    @Autowired
    private AuthorityRepository authorityRepository;

    /**
     * 获取管理员详细信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/getAuthorById",method=RequestMethod.GET)
    public @ResponseBody
    ResponseData getAuthorById(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String authorityid = request.getParameter("authorityid");
        try {
            AuthorityEntity authority = authorityRepository.get(authorityid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authority",authority);
            responseData.setData(jsonObject);
        }
        catch (Exception e){
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        }
        finally {
            return responseData;
        }
    }


}
