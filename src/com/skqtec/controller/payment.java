package com.skqtec.controller;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.BillEntity;
import com.skqtec.entity.OrderEntity;
import com.skqtec.entity.UserEntity;
import com.skqtec.repository.BillRepository;
import com.skqtec.repository.OrderRepository;
import com.skqtec.repository.UserRepository;
import com.skqtec.tools.SessionTools;
import com.skqtec.wxtools.WXPay;
import com.skqtec.wxtools.WXPayConfigImpl;
import com.skqtec.wxtools.WXPayUtil;
import com.sun.istack.internal.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/applet/payments")
public class payment {
    private WXPay wxpay;
    private WXPayConfigImpl config;
    private static Logger logger= Logger.getLogger(payment.class);
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillRepository billRepository;

    /**
     * 支付请求
     * @param orderId
     * @param productId
     * @param openId
     * @param fee
     * @return
     */
    public static JSONObject payRequest(String orderId,String productId,String openId,String fee){

        JSONObject jsonobject=new JSONObject();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("body", "克团-XXX");
        data.put("out_trade_no", orderId);
        data.put("device_info", "APP");
        data.put("fee_type", "CNY");
        data.put("total_fee", fee);
        data.put("spbill_create_ip", "114.212.81.63");
        data.put("notify_url", "http://www.skqtec.com:8080/ketuan/applet/payments/paycallback");
        data.put("trade_type", "JSAPI");
        data.put("product_id", productId);
        data.put("openid",openId);
        Map<String, String> r=new HashMap<String, String>();
        try {
            WXPayConfigImpl config=WXPayConfigImpl.getInstance();
            WXPay wxpay=new WXPay(config);
            r = wxpay.unifiedOrder(data);
            logger.info(JSONObject.toJSONString(r));
            System.out.println(r);
            jsonobject.put("data",r);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            return jsonobject;
        }
    }

    /**
     * 支付回调接口
     * 一、使用抵扣时，1、修改个人账户余额；2、个人账单添加使用记录
     * 二、修改订单信息
     *
     * @param body
     * @return
     */
    @RequestMapping(value="/paycallback",method=RequestMethod.POST)
    public @ResponseBody String payCallBack(@RequestBody String body){
        Map<String,String>info;
        String returns=null;
        try {
            info = WXPayUtil.xmlToMap(body);
            String orderId=info.get("out_trade_no");
            double totalFee=Double.parseDouble(info.get("total_fee"))/100;
            OrderEntity order=orderRepository.get(orderId);
            UserEntity user=userRepository.get(order.getUserId());
            user.setBalance(user.getBalance()-order.getDeduction());
            userRepository.saveOrUpdate(user);
            double deduction = order.getDeduction();
            Timestamp now = new Timestamp(new Date().getTime());
            if(deduction>0){
                BillEntity bill = new BillEntity();
                String uuid = UUID.randomUUID().toString().replace("-", "");
                bill.setId(uuid);
                bill.setInOut(1); //出账
                bill.setType(4); //使用抵扣
                bill.setMoney(deduction);
                bill.setBalance(user.getBalance());
                bill.setUserId(user.getId());
                bill.setDescription(order.getDescript());
                bill.setDate(now);
                billRepository.save(bill);
            }
            order.setState(2);
            order.setPayTime(now);
            orderRepository.saveOrUpdate(order);
            if(totalFee!=order.getTotalPrice()-deduction) {
                returns = "<xml>" +
                        "  <return_code><![CDATA[FAIL]]></return_code>" +
                        "  <return_msg><![CDATA[OK]]></return_msg>" +
                        "</xml>";
            } else {
                returns = "<xml>" +
                        "  <return_code><![CDATA[SUCCESS]]></return_code>" +
                        "  <return_msg><![CDATA[OK]]></return_msg>" +
                        "</xml>";
            }
        }catch(Exception e){
            e.printStackTrace();
            returns="<xml>" +
                    "  <return_code><![CDATA[FAIL]]></return_code>" +
                    "  <return_msg><![CDATA[OK]]></return_msg>" +
                    "</xml>";
        }finally{
            return returns;
        }
    }

    /**
     * 申请退款接口
     * @param request
     * @return
     */
    @RequestMapping(value="/refund",method=RequestMethod.GET)
    public @ResponseBody ResponseData refund(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String sessionId = request.getHeader("sessionid");
        String orderId = request.getParameter("orderid");
        try {
            //判断是否登录
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }
            String outRefundNo=WXPayUtil.getOrderNo();
            OrderEntity order=orderRepository.get(orderId);
            String totalFee=String.valueOf((int)(order.getTotalPrice()-order.getDeduction()));
            order.setOutRefundNo(outRefundNo);
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no",orderId);
            data.put("out_refund_no",outRefundNo);
            data.put("total_fee",totalFee);
            data.put("refund_fee",totalFee);
            Map<String, String> r=new HashMap<String, String>();
            this.config=WXPayConfigImpl.getInstance();
            this.wxpay=new WXPay(config);
            r=wxpay.refund(data);
            if(!r.get("result_code").equals("SUCCESS")){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.REFUND_FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.REFUND_FAILED);
        } finally {
            return responseData;
        }
    }
}
