package com.skqtec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.*;
import com.skqtec.repository.*;
import com.skqtec.tools.RedisAPI;
import com.skqtec.tools.SessionTools;
import com.skqtec.wxtools.WXPayConstants;
import com.skqtec.wxtools.WXPayUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/applet/orders")
public class orders {
    static Logger logger = Logger.getLogger(orders.class.getName());

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SendAddressRepository sendAddressRepository;
    @Autowired
    private GroupRepository groupRepository;

    public void addNewOrderToRedis(String orderId,long createTime){
        JedisPool pool=RedisAPI.getPool();
        Jedis j=pool.getResource();
        JSONObject newOrderInRedis = new JSONObject();
        newOrderInRedis.put("orderId",orderId);
        newOrderInRedis.put("creatTime",createTime);
        j.lpush("changeOrder",JSON.toJSONString(newOrderInRedis));
    }

    public void removeCanceledOrderInRedis(String orderId){
        JedisPool pool=RedisAPI.getPool();
        Jedis j=pool.getResource();
        for(int i=0;i<j.llen("changeOrder");i++){
            JSONObject jsonObject=JSON.parseObject(j.lindex("changeOrder",i));
            String id=(String)jsonObject.get("orderId");
            if(orderId==id){
                OrderEntity order=orderRepository.get(id);
                order.setState(6);   //6已取消
                orderRepository.saveOrUpdate(order);
                j.lrem("changeOrder",1,j.lindex("changeOrder",i));
                break;
            }
        }
    }

    /**
     * 生成订单
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value="/createorder",method = RequestMethod.GET)
    public @ResponseBody ResponseData createOrder(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData=new ResponseData();
        String deduction=request.getParameter("deduction");//抵扣金额
        String productId=request.getParameter("productid");
        String groupId=request.getParameter("groupid");
        String linksender=request.getParameter("linksender");
        String sessionId=request.getHeader("sessionid");
        String productStyle=request.getParameter("style");
        String meno=request.getParameter("meno");
        String totalPriceString = request.getParameter("totalprice");
        double totalPrice=Double.parseDouble(totalPriceString); //订单金额
        double productPrice=Double.parseDouble(request.getParameter("productprice"));   //单价
        int sums=Integer.parseInt(request.getParameter("sums"));   //数量
        double carriagePrice=Double.parseDouble(request.getParameter("carriageprice"));   //运费
        String actualPaymentString = request.getParameter("actualPayment");
        double actualPayment=Double.parseDouble(actualPaymentString);     //实际支付金额

        //判断是否登录
        String userId=SessionTools.sessionQuery(sessionId);
        if(userId==null){
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
            return responseData;
        }
        UserEntity user=userRepository.get(userId);
        String openId=user.getOpenid();
        String fdAddress=user.getFirstDeliverAddress();
        if(fdAddress==null){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("error","未填收货地址");
            responseData.setData(jsonObject);
        }
        String out_trade_no=WXPayUtil.getOrderNo();
        OrderEntity orderEntity=new OrderEntity();
        orderEntity.setCarriagePrice(carriagePrice);
        orderEntity.setActualPayment(actualPayment);
        orderEntity.setGroupId(groupId);
        orderEntity.setId(out_trade_no);
        orderEntity.setLinkSender(linksender);
        orderEntity.setMeno(meno);
        orderEntity.setOrderTime( new Timestamp(new Date().getTime()));
        orderEntity.setProductId(productId);
        orderEntity.setProductPrice(productPrice);
        ProductEntity product=productRepository.get(productId);
        SendaddressEntity sendaddress=sendAddressRepository.get(fdAddress);
        orderEntity.setSendAddress(sendaddress.getId());
        orderEntity.setSendName(sendaddress.getSendName());
        orderEntity.setSendTel(sendaddress.getSendPhone());
        orderEntity.setSendZip(sendaddress.getZip());
        orderEntity.setState(1);
        orderEntity.setTotalPrice(totalPrice);
        orderEntity.setUserId(userId);
        orderEntity.setPaymethod("微信支付");
        orderEntity.setTypeSpecification(productStyle);
        orderEntity.setSums(sums);
        orderEntity.setDeduction(Double.parseDouble(deduction));
        orderEntity.setDescript(product.getProductName()+product.getProductInfo());
        //将订单信息填写至redis，1小时内未付款取消
        addNewOrderToRedis(out_trade_no,orderEntity.getOrderTime().getTime());
        try{
            logger.info("********product save returned :  "+orderRepository.save(orderEntity));
            //支付
            JSONObject jsonObject = payOrder(productId, actualPayment, openId, out_trade_no);
            responseData.setData(jsonObject);
            //参团人数加1
            GroupEntity group=groupRepository.get(groupId);
            if(group.getOfferedCount()<group.getGroupCount()) {
                group.setOfferedCount(group.getOfferedCount() + 1);
                groupRepository.saveOrUpdate(group);
            }
        }
        catch (Exception e){
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.CREATE_ORDER_FAILED);
        }
        finally {
            return responseData;
        }
    }

    private JSONObject payOrder(String productId, double totalPrice, String openId, String out_trade_no) throws Exception {
        JSONObject j=null;
        String payMoney=String.valueOf((int)(totalPrice*100));
        System.out.println(payMoney);
        j=payment.payRequest(out_trade_no,productId,openId,payMoney);
        Map<String, String> data=(Map)j.get("data");
        logger.info("***********data :    "+JSON.toJSONString(data) +"*****************");
        String timeStamp=String.valueOf(new Date().getTime()/1000);
        System.out.println("**********"+timeStamp+"************");
        logger.info("***************"+timeStamp+"*****************");
        String nonceStr=data.get("nonce_str");
        String package1="prepay_id="+data.get("prepay_id");
        String signType="MD5";
        Map<String,String>reqData=new HashMap<String, String>();
        reqData.put("appId","wx5733cafea467c980");
        reqData.put("nonceStr",nonceStr);
        reqData.put("package",package1);
        reqData.put("signType",signType);
        reqData.put("timeStamp",timeStamp);
        String paySign=WXPayUtil.generateSignature(reqData,"lijie1108NANCYlijie1108skqtec01s",WXPayConstants.SignType.MD5);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("timeStamp",timeStamp);
        jsonObject.put("nonceStr",nonceStr);
        jsonObject.put("package",package1);
        jsonObject.put("signType",signType);
        jsonObject.put("paySign",paySign);
        return jsonObject;
    }

    /**
     * 未支付订单支付接口
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value="/orderpay",method = RequestMethod.GET)
    public @ResponseBody ResponseData orderPay(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String orderId = request.getParameter("orderid");
        try {
            OrderEntity order = orderRepository.get(orderId);
            UserEntity user = userRepository.get(order.getUserId());
            double actualPayment = order.getActualPayment();
            String productId = order.getProductId();
            String openId = user.getOpenid();
            String out_trade_no = orderId;
            JSONObject jsonObject = payOrder( productId, actualPayment, openId, out_trade_no);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e, e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.PAY_FAILED);
        } finally {
            return responseData;
        }
    }

    /**
     * 获取订单
     * @param request
     * @return
     */
    @RequestMapping(value="/getorder",method = RequestMethod.GET)
    public @ResponseBody ResponseData getOrder(HttpServletRequest request){
        ResponseData responseData=new ResponseData();
        String orderState=request.getParameter("orderstate");
        String sessionId=request.getHeader("sessionid");
        try{
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }

            List<OrderEntity>orders;
            if("0".equals(orderState)){
                orders=orderRepository.query(userId);
            }
            else{
                orders=orderRepository.query(userId,orderState);
            }
            JSONObject jsonObject1 = getJsonObject(orders);
            responseData.setData(jsonObject1);
        }catch(Exception e){
            logger.error(e,e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_ORDER_FAILED);
        }finally{
            return responseData;
        }
    }

    private JSONObject getJsonObject(List<OrderEntity> orders) {
        List<JSONObject>j=new ArrayList<JSONObject>();
        for(OrderEntity order:orders){
            ProductEntity product=productRepository.get(order.getProductId());
            String shopName=null;
            if(product.getOwnerType()==0) {
                MerchantEntity merchant = merchantRepository.get(product.getMerchantId());
                shopName=merchant.getName();
            }
            else{
                UserEntity user=userRepository.get(product.getUserId());
                shopName=user.getNickname();
            }
            String orderId=order.getId();
            String productImg=product.getProductFistImg();
            String productTitle=product.getProductName();
            String productPrice=String.valueOf(product.getPrice());
            String sums=String.valueOf(order.getSums());
            String typeSpecification=order.getTypeSpecification();
            String sumPrice=String.valueOf(order.getTotalPrice());
            String deduction=String.valueOf(order.getDeduction());
            String orderState=String.valueOf(order.getState());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("shopName",shopName);
            jsonObject.put("orderId",orderId);
            jsonObject.put("orderState",orderState);
            jsonObject.put("productImg",productImg);
            jsonObject.put("productTitle",productTitle);
            jsonObject.put("productPrice",productPrice);
            jsonObject.put("sums",sums);
            jsonObject.put("deduction",deduction);
            jsonObject.put("typeSpecification",typeSpecification);
            jsonObject.put("sumPrice",sumPrice);
            j.add(jsonObject);
        }
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("searchResult",j);
        return jsonObject1;
    }

    /**
     * 删除订单
     * @param request
     * @return
     */
    @RequestMapping(value="/removeorder",method = RequestMethod.GET)
    public @ResponseBody ResponseData removeOrder(HttpServletRequest request){
        ResponseData responseData=new ResponseData();
        String orderId=request.getParameter("orderid");
        String sessionId=request.getHeader("sessionid");
        try{
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }
            OrderEntity order=orderRepository.get(orderId);
            removeCanceledOrderInRedis(order.getId());
            order.setState(6);
            orderRepository.saveOrUpdate(order);
        }catch(Exception e){
            logger.error(e,e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.REMOVE_ORDER_FAILED);
        }finally{
            return responseData;
        }
    }

    /**
     * 订单搜索
     * @param request
     * @return
     */
    @RequestMapping(value="/searchorders",method = RequestMethod.GET)
    public @ResponseBody ResponseData searchOrder(HttpServletRequest request){
        ResponseData responseData=new ResponseData();
        String key=request.getParameter("key");
        String sessionId=request.getHeader("sessionid");
        try{
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }
            List<OrderEntity>orders=new ArrayList<OrderEntity>();
            orders=orderRepository.search(userId,key);
            JSONObject jsonObject1 = getJsonObject(orders);
            responseData.setData(jsonObject1);
        }catch(Exception e){
            logger.error(e,e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.SEARCH_ORDERS_FAILED);
        }finally{
            return responseData;
        }
    }

    /**
     * 获取订单详情
     * @param request
     * @return
     */
    @RequestMapping(value="/getorderdetails",method = RequestMethod.GET)
    public @ResponseBody ResponseData getOrderDetails(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String orderId = request.getParameter("orderid");
        String sessionId=request.getHeader("sessionid");
        try {
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }
            OrderEntity order=orderRepository.get(orderId);
            ProductEntity product=productRepository.get(order.getProductId());
            String orderState=null;
            switch(order.getState()){
                case 1:orderState="待付款";break;
                case 2:orderState="待发货";break;
                case 3:orderState="待收货";break;
                case 4:orderState="待评价";break;
                case 5:orderState="已评价";break;
                case 6:orderState="已取消";break;
            }
            String sendName=order.getSendName();
            SendaddressEntity sendaddressEntity=sendAddressRepository.get(order.getSendAddress());
            String sendAddress=sendaddressEntity.getProvince()+sendaddressEntity.getCity()+sendaddressEntity.getDistricts()+sendaddressEntity.getAddressDetail();
            String sendTel=order.getSendTel();
            String productImg=product.getProductFistImg();
            String productTitle=product.getProductName();
            String productPrice=String.valueOf(product.getPrice());
            String sums=String.valueOf(order.getSums());
            String typeSpecification=order.getTypeSpecification();
            String sumPrice=String.valueOf(order.getTotalPrice());
            String carriagePrice=String.valueOf(order.getCarriagePrice());
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String payTime=null;
            if(order.getState()>=2&&order.getState()!=6)
                payTime=sdf.format(order.getPayTime());
            String deliverTime=null;
            if(order.getState()>=3&&order.getState()!=6)
                deliverTime=sdf.format(order.getDeliverTime());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("orderId",order.getId());
            jsonObject.put("orderState",orderState);
            jsonObject.put("sendName",sendName);
            jsonObject.put("sendAddress",sendAddress);
            jsonObject.put("sendTel",sendTel);
            jsonObject.put("productImg",productImg);
            jsonObject.put("productTitle",productTitle);
            jsonObject.put("productPrice",productPrice);
            jsonObject.put("sums",sums);
            jsonObject.put("typeSpecification",typeSpecification);
            jsonObject.put("sumPrice",sumPrice);
            jsonObject.put("carriagePrice",carriagePrice);
            jsonObject.put("payTime",payTime);
            jsonObject.put("deliverTime",deliverTime);
            JSONObject j[]=new JSONObject[1];
            j[0]=jsonObject;
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("orderDetails",j);
            responseData.setData(jsonObject1);
        } catch (Exception e) {
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_ORDER_DETAILS_FAILED);
        } finally {
            return  responseData;
        }
    }
}