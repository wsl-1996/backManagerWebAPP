package com.skqtec.controller;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.MerchantEntity;
import com.skqtec.repository.MerchantRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
@Controller
@RequestMapping("/applet/merchants")
public class merchants {

    static Logger logger = Logger.getLogger(merchants.class.getName());

    @Autowired
    private MerchantRepository merchantRepository;

    /**
     * 获取所有商家
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/listall",method=RequestMethod.GET)
    public @ResponseBody ResponseData getAllMerchants(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        try {
            List<MerchantEntity> merchants = merchantRepository.findAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("merchants",merchants);
            responseData.setData(jsonObject);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        }
        finally {
            return responseData;
        }
    }

    /**
     * 关键词查询商家
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/search",method=RequestMethod.GET)
    public @ResponseBody ResponseData queryMerchants(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String key = request.getParameter("key");
        try {
            List<MerchantEntity> merchants = merchantRepository.search(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("merchants",merchants);
            responseData.setData(jsonObject);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        }
        finally {
            return responseData;
        }
    }

    /**
     * 获取商家详细信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/getdetail",method=RequestMethod.GET)
    public @ResponseBody ResponseData getMerchant(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String merchantid = request.getParameter("merchantid");
        try {
            MerchantEntity merchant = merchantRepository.get(merchantid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("merchant",merchant);
            responseData.setData(jsonObject);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        }
        finally {
            return responseData;
        }
    }

}
