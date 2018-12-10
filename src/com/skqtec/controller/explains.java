package com.skqtec.controller;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.ExplainEntity;
import com.skqtec.repository.ExplainRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/applet/explains")
public class explains {

    static Logger logger = Logger.getLogger(explains.class.getName());

    @Autowired
    private ExplainRepository explainRepository;

    /**
     * 通过key,获取解释说明文字
     * @param request
     * @return
     */
    @RequestMapping(value="/getexplain",method=RequestMethod.GET)
    public @ResponseBody ResponseData getComments(HttpServletRequest request){
        ResponseData responseData=new ResponseData();
        String key=request.getParameter("key");
        JSONObject jb = new JSONObject();
        jb.put("explainKey",key);
        try{
            List<ExplainEntity> explains = explainRepository.query(jb);
            if(explains.size()==0){
                responseData.setFailed(true);
                responseData.setFailedMessage("没有找到相应key的解释");
            }
            else{
                JSONObject returnJB= new JSONObject();
                returnJB.put("explain",explains.get(0));
                responseData.setData(returnJB);
            }
        }catch (Exception e){
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage("获取解释信息失败");
        }
        return responseData;

    }
}
