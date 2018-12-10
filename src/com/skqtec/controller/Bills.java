package com.skqtec.controller;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.BillEntity;
import com.skqtec.repository.BillRepository;
import com.skqtec.tools.SessionTools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/applet/bills")
public class Bills {
    static Logger logger = Logger.getLogger(Bills.class.getName());
    @Autowired
    private BillRepository billRepository;
    @RequestMapping(value="/getcashback",method=RequestMethod.GET)
    public @ResponseBody
    ResponseData getCashBack(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String sessionId=request.getHeader("sessionid");
        try{
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }
            List<BillEntity>bills=new ArrayList<BillEntity>();
            bills=billRepository.query(userId,0);
            List<JSONObject>j=new ArrayList<JSONObject>();
            for(BillEntity bill:bills){
                Calendar cal = Calendar.getInstance();
                DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String date=sdf.format(bill.getDate());
                String nowDate=sdf.format(new Date());
                String month=date.substring(5,7);
                String thisMonth=nowDate.substring(5,7);
                String cashBackMoney=String.valueOf(bill.getMoney());
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("cashbackmonth",month);
                jsonObject.put("thismonth",thisMonth);
                jsonObject.put("cashbackmoney",cashBackMoney);
                j.add(jsonObject);
            }
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("cashback",j);
            responseData.setData(jsonObject1);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_CASHBACK_LIST_FAILED);
        }finally{
            return responseData;
        }
    }
    //返现订单

}
