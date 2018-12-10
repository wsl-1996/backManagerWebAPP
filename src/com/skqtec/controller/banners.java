package com.skqtec.controller;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.BannerEntity;
import com.skqtec.repository.BannerRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/applet/banners")
public class banners {

    static Logger logger = Logger.getLogger(banners.class.getName());
    @Autowired
    private BannerRepository bannerRepository;


    @RequestMapping(value="/getbanner",method=RequestMethod.GET)
    public @ResponseBody
    ResponseData getBanner(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        try{
            List<BannerEntity> banners=new ArrayList<BannerEntity>();
            JSONObject jb = new JSONObject();
            jb.put("state",1);
            banners=bannerRepository.query(jb);
            JSONObject resultJSON = new JSONObject();
            resultJSON.put("banners",banners);
            responseData.setData(resultJSON);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage("获取广告信息失败！");
        }finally{
            return responseData;
        }
    }

}
