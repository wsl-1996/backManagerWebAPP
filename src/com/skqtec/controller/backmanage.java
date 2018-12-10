
package com.skqtec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ConfigProperty;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.*;
import com.skqtec.repository.*;
import com.skqtec.service.MessageService;
import com.skqtec.tools.Base64Tool;
import com.skqtec.tools.DisposeUtil;
import com.skqtec.tools.RandomTools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/backmanage")
public class backmanage {
    static Logger logger = Logger.getLogger(groups.class.getName());

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  ExpressageRepository  expressageRepository;

    @Autowired
    private ProductClassifyCodeRepository productClassifyCodeRepository;

    @Autowired
    private  AuthorityRepository  authorityRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AuthorityDefineRepository authorityDefineRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SendAddressRepository sendAddressRepository;

    @Autowired
    private ConfigProperty config;

    @RequestMapping("/configs")
    public @ResponseBody String hello() {
        String host = config.getHost();
        String tempPathFile = config.getTempPathFile();
        JSONObject j = new JSONObject();
        j.put("host",host);
        j.put("tempPathFile",tempPathFile);
        return JSON.toJSONString(j);
    }

    /**
     * 创建商家
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/createMerchant",method=RequestMethod.POST)
    public @ResponseBody ResponseData createMerchant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResponseData responseData = new ResponseData();
        String name = request.getParameter("merchant_name");
        String addresss = request.getParameter("merchant_add");
        String phone = request.getParameter("merchant_phone");
        String discription = request.getParameter("merchant_info");
        String count = request.getParameter("merchant_count");
        String pass = request.getParameter("merchant_pass");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountname",count);
        List<MerchantEntity> mechants = merchantRepository.query(jsonObject);
        if(mechants.size()>0){
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.MERCHANT_ACOUNT_EXISTS);
            return responseData;
        }
        MerchantEntity merchantEntity = new MerchantEntity();
        merchantEntity.setName(name);
        merchantEntity.setAddress(addresss);
        merchantEntity.setPhone(phone);
        merchantEntity.setDiscription(discription);
        merchantEntity.setAccountname(count);
        merchantEntity.setAccoutpass(pass);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        merchantEntity.setId(uuid);
        try{
            logger.info("********product save returned :  "+ merchantRepository.save(merchantEntity));
            JSONObject data = new JSONObject();
            data.put("merchantid",uuid);
            responseData.setData(data);
        }
        catch (Exception e){
            logger.error(e,e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.SAVE_MERCHANT_INFO_FAILED);
        }
        finally {
            return  responseData;
            //return JSON.toJSONString(responseData);
        }
    }

    /**
     * 创建产品
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/createproduct",method=RequestMethod.POST)
    public @ResponseBody ResponseData createProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResponseData responseData = new ResponseData();
        String name = request.getParameter("productname");
        String info = request.getParameter("productinfo");
        String code = request.getParameter("productcode");
        String lable = request.getParameter("productlable");
        String ownertype = request.getParameter("productownertype");
        String ownerid = request.getParameter("productownerid");
        String count = request.getParameter("productcount");
        String price = request.getParameter("productprice");
        String cost = request.getParameter("productcost");
        String produceaddress = request.getParameter("productproduceadd");
        String packstand = request.getParameter("productpackstand");
        String aftersale = request.getParameter("productaftersale");
        String firstimg = request.getParameter("productFirstImg");
        String[] slideimgs = request.getParameterValues("productSlideimgs[]");
        String[] contentimgs = request.getParameterValues("productContentimgs[]");
        String onlinetime = request.getParameter("onlinetime");
        String offlinetime = request.getParameter("offlinetime");
        String express_code = request.getParameter("express_code");
        String express_name = request.getParameter("express_name");
        String express_price_stand = request.getParameter("express_price_stand");

        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(name);
        productEntity.setProductInfo(info);
        productEntity.setOnlineTime(Timestamp.valueOf(onlinetime));
        productEntity.setOfflineTime(Timestamp.valueOf(offlinetime));
        productEntity.setProductClassifyCode(code);
        productEntity.setProductLabel(lable);
        int ownerType = Integer.parseInt(ownertype);
        productEntity.setOwnerType(ownerType);
        if(ownerType==0){
            productEntity.setMerchantId(ownerid);
        }else{
            productEntity.setUserId(ownerid);
        }
        productEntity.setPrice(Double.parseDouble(price));
        productEntity.setProductCost(Double.parseDouble(cost));
        productEntity.setProductProduceAddress(produceaddress);
        productEntity.setPackStand(packstand);
        productEntity.setAfterSale(aftersale);
        productEntity.setProductFistImg(firstimg);
        productEntity.setProductSlideImg(JSON.toJSONString(slideimgs));
        productEntity.setImagesAddress(JSON.toJSONString(contentimgs));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        productEntity.setId(uuid);

        ExpressageEntity expressageEntity = new ExpressageEntity();
        expressageEntity.setExpressCode(express_code);
        expressageEntity.setExpressageName(express_name);
        expressageEntity.setPriceStand(express_price_stand);
        expressageEntity.setProductId(uuid);
        String expressid = UUID.randomUUID().toString().replace("-", "");
        expressageEntity.setId(expressid);
        expressageEntity.setIsNew(1);
        Date now = new Date();
        Timestamp time = new Timestamp(now.getTime());
        expressageEntity.setCreateTime(time);

        try{
            String productId =  productRepository.save(productEntity);
            String expressId = expressageRepository.save(expressageEntity);
            JSONObject data = new JSONObject();
            data.put("productid",uuid);
            data.put("expressid",expressid);
            responseData.setData(data);
        }
        catch (Exception e){
            logger.error(e,e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage("创建商品失败！");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 创建团购
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/creategroupbuy", method = RequestMethod.POST)
    public @ResponseBody
    ResponseData createGroupbuy(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResponseData responseData = new ResponseData();
        String name = request.getParameter("groupbuy_name");
        String info = request.getParameter("groupbuy_info");
        String productid = request.getParameter("productid");
        String ownerid = request.getParameter("ownerid");
        String count = request.getParameter("groupbuy_count");
        String price = request.getParameter("groupbuy_price");
        String cost = request.getParameter("groupbuy_cost");
        String deliver_add = request.getParameter("groupbuy_deliver_add");
        String firstImg = request.getParameter("productFirstImg");
        String[] slideimgs = request.getParameterValues("productSlideimgs[]");
        String endtime = request.getParameter("groupbuy_end_time");
        String starttime = request.getParameter("groupbuy_start_time");
        String[] group_style = request.getParameterValues("group_style[]");
        String return_cash_rate = request.getParameter("return_cash_rate");
        String return_cash_rate_inviter = request.getParameter("return_cash_rate_inviter");
        String customer_service = request.getParameter("customer_service");
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setGroupName(name);
        groupEntity.setGroupDiscription(info);
        groupEntity.setProductId(productid);
        groupEntity.setUserId(ownerid);
        groupEntity.setGroupPrice(Double.parseDouble(price));
        groupEntity.setProductCost(Double.parseDouble(cost));
        groupEntity.setReturnCashRate(Double.parseDouble(return_cash_rate));
        groupEntity.setReturnCashRateInviter(Double.parseDouble(return_cash_rate_inviter));
        groupEntity.setCustomerService(customer_service);
        groupEntity.setGroupStyle(JSON.toJSONString(group_style));
        groupEntity.setDeliverAddress(deliver_add);
        groupEntity.setGroupCount(Integer.parseInt(count));
        groupEntity.setGroupFirstImg(firstImg);
        groupEntity.setEndTime(Timestamp.valueOf(endtime));
        groupEntity.setGroupSlideImg(JSON.toJSONString(slideimgs));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        groupEntity.setId(uuid);
        try {
            logger.info("********product save returned :  " + groupRepository.save(groupEntity));
            JSONObject data = new JSONObject();
            data.put("groupid", uuid);
            responseData.setData(data);
        } catch (Exception e) {
            logger.error(e, e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.CREATE_GROUP_FAILED);
        } finally {
            return responseData;
        }
    }

    /**
     * 获取所有团购
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/grouplistall",method=RequestMethod.GET)
    public @ResponseBody ResponseData getAllGroup(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        try {
            List<GroupEntity> groups = groupRepository.findAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("groups",groups);
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
     * 团购上下线
     * @return
     */

    @RequestMapping(value = "/changeGroupstate", method = RequestMethod.GET)
    public @ResponseBody ResponseData changeGroupstate(HttpServletRequest request,HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String productState=request.getParameter("groupState");
        Integer state = Integer.parseInt(productState);
        String groupId=request.getParameter("groupId");
        try {
            GroupEntity groupEntity=groupRepository.get(groupId);
            if(groupEntity!=null)
            {
                groupEntity.setGroupState(state);
                groupRepository.saveOrUpdate(groupEntity);
            }
            else{
                responseData.setFailed(true);
                responseData.setFailedMessage("团购上下线失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage("团购上下线失败!");
        } finally {
            return responseData;
        }
    }


    /**
     * 获取admin状态
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/checkli",method=RequestMethod.GET)
    public @ResponseBody ResponseData checkli(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        try {
                HttpSession session = request.getSession();
                String authorid = (String)session.getAttribute("authorid");
                if(authorid==null||authorid==""){
                    responseData.setFailed(true);
                    responseData.setFailedMessage("未登录");
                }
                else{
                    responseData.setFailed(false);
                    JSONObject jo = new JSONObject();
                    jo.put("sessionId",session.getId());
                    jo.put("authorid",authorid);
                    responseData.setData(jo);
                }
        }
        catch (Exception e){
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage("未登录");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 登录接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/login",method=RequestMethod.POST)
    public @ResponseBody ResponseData login(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String username = request.getParameter("username");
        String pass = request.getParameter("pass");
        try {
            JSONObject jb = new JSONObject();
            jb.put("adminCount",username);
            jb.put("adminPass",pass);
            List<AuthorityEntity> author = authorityRepository.query(jb);
            if(author.size()==0){
                responseData.setFailed(true);
                responseData.setFailedMessage("用户名或密码错误");
            }
            else{
                HttpSession session = request.getSession();
                session.setAttribute("authorid", author.get(0).getAuthorityId());
                String sessionId = session.getId();
                //判断session是不是新创建的
                if (session.isNew()) {
                    logger.info("session创建成功，session的id是："+sessionId);
                }else {
                    logger.info("服务器已经存在该session了，session的id是："+sessionId);
                }
                JSONObject data = new JSONObject();
                data.put("author",author.get(0));
                responseData.setData(data);
            }
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage("登录失败");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 退出登录接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/logout",method=RequestMethod.POST)
    public @ResponseBody ResponseData logout(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        try {
            HttpSession session = request.getSession();
            session.setAttribute("authorid",null);
            //手工调用session.invalidate方法，摧毁session
            session.invalidate();
            responseData.setFailed(false);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage("退出登录失败");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 关键词查询团购
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/groupsearch", method = RequestMethod.GET)
    public @ResponseBody
    ResponseData queryGroups(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String key = request.getParameter("key");
        try {
//            JSONObject queryObject = new JSONObject();
//            queryObject.put("key",key);
            List<GroupEntity> groups = groupRepository.search(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("groups", groups);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        } finally {
            return responseData;
        }
    }



    //获取团购详情
    @RequestMapping(value="/getgroupinfo",method=RequestMethod.GET)
    public @ResponseBody ResponseData getGroupDetails(HttpServletRequest request,HttpServletResponse response){
        ResponseData responseData=new ResponseData();
        String groupId=request.getParameter("groupid");
        try {
            GroupEntity group = groupRepository.get(groupId);
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("groupdetails",group);
            responseData.setData(jsonobject);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_DETAILS_FAILED);
        }finally{
            return responseData;
        }
    }


    /**
     * 获取所有订单
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/orderlistall",method=RequestMethod.GET)
    public @ResponseBody ResponseData getAllOrders(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        try {
            List<OrderEntity> orders = orderRepository.findAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orders",orders);
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



    //订单搜索
    @RequestMapping(value="/searchorders",method = RequestMethod.GET)
    public @ResponseBody ResponseData searchOrder(HttpServletRequest request,HttpServletResponse response){
        ResponseData responseData=new ResponseData();
        //String userId=request.getParameter("userid");
        String key=request.getParameter("key");
        try{
            List<JSONObject>j=new ArrayList<JSONObject>();
            List<OrderEntity>orders=orderRepository.search(key);
            //orders=orderRepository.search(key);
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
                String sendName = order.getSendName();
                String paymethod=order.getPaymethod();
                String orderId=order.getId();
                String orderState=null;
                switch(order.getState()){
                    case 1:orderState="待付款";break;
                    case 2:orderState="待发货";break;
                    case 3:orderState="待收货";break;
                    case 4:orderState="待评价";break;
                    case 5:orderState="已评价";break;
                }
                String productImg=product.getProductFistImg();
                String productTitle=product.getProductName();
                String productPrice=String.valueOf(product.getPrice());
                String sums=String.valueOf(order.getSums());
                String typeSpecification=order.getTypeSpecification();
                String sumPrice=String.valueOf(order.getTotalPrice());
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("sendName",sendName);
                jsonObject.put("paymethod",paymethod);
                jsonObject.put("shopName",shopName);
                jsonObject.put("orderId",orderId);
                jsonObject.put("orderState",orderState);
                jsonObject.put("productImg",productImg);
                jsonObject.put("productTitle",productTitle);
                jsonObject.put("productPrice",productPrice);
                jsonObject.put("sums",sums);
                jsonObject.put("typeSpecification",typeSpecification);
                jsonObject.put("sumPrice",sumPrice);
                j.add(jsonObject);
            }
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("searchResult",j);
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
    @RequestMapping(value = "/getorderdetails", method = RequestMethod.GET)
    public @ResponseBody
    ResponseData getOrderDetails(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String orderId = request.getParameter("orderid");
        try {
            OrderEntity order = orderRepository.get(orderId);
            ProductEntity product = productRepository.get(order.getProductId());
            String orderState = null;
            switch (order.getState()) {
                case 1:
                    orderState = "待付款";
                    break;
                case 2:
                    orderState = "待发货";
                    break;
                case 3:
                    orderState = "待收货";
                    break;
                case 4:
                    orderState = "待评价";
                    break;
                case 5:
                    orderState = "已评价";
                    break;
                case 6:
                    orderState = "已取消";
                    break;
            }
            String sendName = order.getSendName();
            String sendAddress = order.getSendAddress();
            String sendTel = order.getSendTel();
            String productImg = product.getProductFistImg();
            String productTitle = product.getProductName();
            String productPrice = String.valueOf(product.getPrice());
            String acticePrice = String.valueOf(order.getProductPrice());
            String sums = String.valueOf(order.getSums());
            String typeSpecification = order.getTypeSpecification();
            String sumPrice = String.valueOf(order.getTotalPrice());
            String carriagePrice = String.valueOf(order.getCarriagePrice());
            String deduction = String.valueOf(order.getDeduction());
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp paytimelong= order.getPayTime();
            String payTime = "";
            Timestamp deliverTimelong= order.getDeliverTime();
            String deliverTime = "";
            Timestamp receiptTimelong=order.getReceiptTime();
            String receiptTime = "";
            Timestamp ordertimelong = order.getOrderTime();
            String orderTime="";
            if(receiptTimelong!=null){
                receiptTime=sdf.format(receiptTimelong);
            }
            if(ordertimelong!=null){
                orderTime=sdf.format(ordertimelong);
            }
            if(paytimelong!=null){
                payTime = sdf.format(paytimelong);
            }
            if(deliverTimelong!=null){
                deliverTime = sdf.format(deliverTimelong);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",order.getId());
            jsonObject.put("orderState", orderState);
            jsonObject.put("sendName", sendName);
            jsonObject.put("sendAddress", sendAddress);
            jsonObject.put("sendTel", sendTel);
            jsonObject.put("acticePrice",acticePrice);
            jsonObject.put("productImg", productImg);
            jsonObject.put("productTitle", productTitle);
            jsonObject.put("productPrice", productPrice);
            jsonObject.put("sums", sums);
            jsonObject.put("deduction",deduction);
            jsonObject.put("receiptTime",receiptTime);
            jsonObject.put("orderTime", orderTime);
            jsonObject.put("typeSpecification", typeSpecification);
            jsonObject.put("sumPrice", sumPrice);
            jsonObject.put("carriagePrice", carriagePrice);
            jsonObject.put("payTime", payTime);
            jsonObject.put("deliverTime", deliverTime);
            jsonObject.put("trackCode", order.getTrackCode());
            jsonObject.put("trackId", order.getTrackId());

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("orderDetails", jsonObject);
            responseData.setData(jsonObject1);
        } catch (Exception e) {
            logger.error(e, e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_ORDER_DETAILS_FAILED);
        } finally {
            return responseData;
        }
    }


    /**
     * 关键词查询商家
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/merchantsearch", method = RequestMethod.GET)
    public @ResponseBody ResponseData queryMerchants(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String key = request.getParameter("key");
        try {
            List<MerchantEntity> merchants = merchantRepository.search(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("merchants", merchants);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        } finally {
            return responseData;
        }
    }


    /**
     * 获取商家详细信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/merchantgetdetail", method = RequestMethod.GET)
    public @ResponseBody
    ResponseData getMerchant(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String merchantid = request.getParameter("merchantid");
        try {
            MerchantEntity merchant = merchantRepository.get(merchantid);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("merchant", merchant);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        } finally {
            return responseData;
        }
    }


    /**
     * 获取所有商家
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/merchantlistall", method = RequestMethod.GET)
    public @ResponseBody
    ResponseData getAllMerchants(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        try {
            List<MerchantEntity> merchants = merchantRepository.findAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("merchants", merchants);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        } finally {
            return responseData;
        }
    }


    /**
     * 关键词查询商品
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/productsearch", method = RequestMethod.GET)
    public @ResponseBody ResponseData queryProducts(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String key = request.getParameter("key");
        try {
            List<ProductEntity> products = productRepository.search(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("products", products);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        } finally {
            return responseData;
        }
    }

    /**
     * 获取商品类型code
     * @param request
     * @return
     */
    @RequestMapping(value="/getproductclassify",method=RequestMethod.GET)
    public @ResponseBody ResponseData getProductClassify(HttpServletRequest request) {
        ResponseData responseData=new ResponseData();
        try{
            List<ProductClassifyCodeEntity> lists=productClassifyCodeRepository.findAll();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("productclassify",lists);
            responseData.setData(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            responseData.setFailed(true);
        }finally {
            return responseData;
        }
    }

    //获取商品详情
    @RequestMapping(value = "/getproductinfo", method = RequestMethod.GET)
    public @ResponseBody
    ResponseData getProductDetails(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String productId = request.getParameter("productid");
        try {
            JSONObject jsonObject = new JSONObject();
            ProductEntity product = productRepository.get(productId);
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String onlineTime = sdf.format(product.getOnlineTime());
            String offlineTime = sdf.format(product.getOfflineTime());
            product.setProductFistImg(product.getProductFistImg());
            product.setImagesAddress(product.getImagesAddress());
            product.setProductSlideImg(product.getProductSlideImg());

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1 = DisposeUtil.dispose(product);
            jsonObject1.put("onlineTime", onlineTime);
            jsonObject1.put("offlineTime", offlineTime);
            jsonObject.put("productdetails", jsonObject1);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_PRODUCT_DETAILS_FAILED);
        } finally {
            return responseData;
        }
    }


    /**
     * 获取所有商品
     * @return
     */
    @RequestMapping(value = "/productlistall", method = RequestMethod.GET)
    public @ResponseBody ResponseData getAllProducts() {
        ResponseData responseData = new ResponseData();
        try {
            List<ProductEntity> products = productRepository.findAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("products", products);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage("获取商品列表失败");
        } finally {
            return responseData;
        }
    }


   /**
     * 商品下线
     * @return
     */

   @RequestMapping(value = "/changeproductstate", method = RequestMethod.GET)
   public @ResponseBody ResponseData changeProductState(HttpServletRequest request,HttpServletResponse response) {
       ResponseData responseData = new ResponseData();
       String productState=request.getParameter("productState");
       Integer state = Integer.parseInt(productState);
       String productId=request.getParameter("productId");
       try {
           ProductEntity productEntity=productRepository.get(productId);
          if(productEntity!=null)
           {
               productEntity.setProductState(state);
               productRepository.saveOrUpdate(productEntity);
           }
           else{
              responseData.setFailed(true);
              responseData.setFailedMessage("商品下线失败！");
          }
       } catch (Exception e) {
           logger.error(e.getMessage(), e);
           responseData.setFailed(true);
           responseData.setFailedMessage("商品下线失败!");
       } finally {
           return responseData;
       }
   }

    /**
     * 修改商品零售价
     * @return
     */
    @RequestMapping(value = "/changeproductprice", method = RequestMethod.GET)
    public @ResponseBody ResponseData changeProductPrice(HttpServletRequest request,HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String productPrice=request.getParameter("productPrice");
        String productId=request.getParameter("productId");
        try {
            double price = Double.parseDouble(productPrice);
            ProductEntity productEntity=productRepository.get(productId);
            if(productEntity!=null)
            {
                productEntity.setPrice(price);
                productRepository.saveOrUpdate(productEntity);
            }
            else{
                responseData.setFailed(true);
                responseData.setFailedMessage("商品价格修改失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage("商品价格修改失败!");
        } finally {
            return responseData;
        }
    }

    /**
     * 修改商品成本价格
     * @return
     */
    @RequestMapping(value = "/changeproductcost", method = RequestMethod.GET)
    public @ResponseBody ResponseData changeProductCosts(HttpServletRequest request,HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String productCost=request.getParameter("productCost");
        String productId=request.getParameter("productId");
        try {
            double cost = Double.parseDouble(productCost);
            ProductEntity productEntity=productRepository.get(productId);
            if(productEntity!=null)
            {
                productEntity.setProductCost(cost);
                productRepository.saveOrUpdate(productEntity);
            }
            else{
                responseData.setFailed(true);
                responseData.setFailedMessage("商品成本价格修改失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage("商品成本价格修改失败!");
        } finally {
            return responseData;
        }
    }

    /**
     * 修改商品上线时间
     * @return
     */
    @RequestMapping(value = "/changeproductonlinetime", method = RequestMethod.GET)
    public @ResponseBody ResponseData changeProductOnlineTime(HttpServletRequest request,HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String onlinetime=request.getParameter("productOnlineTime");
        String productId=request.getParameter("productId");
        try {
            ProductEntity productEntity=productRepository.get(productId);
            if(productEntity!=null)
            {
                productEntity.setOnlineTime(Timestamp.valueOf(onlinetime));
                productRepository.saveOrUpdate(productEntity);
            }
            else{
                responseData.setFailed(true);
                responseData.setFailedMessage("商品上线时间修改失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage("商品上线时间修改失败!");
        } finally {
            return responseData;
        }
    }

    /**
     * 修改商品下线时间
     * @return
     */
    @RequestMapping(value = "/changeproductofflinetime", method = RequestMethod.GET)
    public @ResponseBody ResponseData changeProductOfflineTime(HttpServletRequest request,HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String offlinetime=request.getParameter("productOfflineTime");
        String productId=request.getParameter("productId");
        try {
            ProductEntity productEntity=productRepository.get(productId);
            if(productEntity!=null)
            {
                productEntity.setOfflineTime(Timestamp.valueOf(offlinetime));
                productRepository.saveOrUpdate(productEntity);
            }
            else{
                responseData.setFailed(true);
                responseData.setFailedMessage("商品上线时间修改失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage("商品上线时间修改失败!");
        } finally {
            return responseData;
        }
    }

    /**
     * 修改商品的描述信息
     * @return
     */
    @RequestMapping(value = "/changeproductinfo", method = RequestMethod.GET)
    public @ResponseBody ResponseData changeProductInfo(HttpServletRequest request,HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String info=request.getParameter("productInfo");
        String productId=request.getParameter("productId");
        try {
            ProductEntity productEntity=productRepository.get(productId);
            if(productEntity!=null)
            {
                productEntity.setProductInfo(info);
                productRepository.saveOrUpdate(productEntity);
            }
            else{
                responseData.setFailed(true);
                responseData.setFailedMessage("商品描述信息修改失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage("商品描述信息修改失败!");
        } finally {
            return responseData;
        }
    }

    /**
     * 修改商品的标签
     * @return
     */
    @RequestMapping(value = "/changeproductlabel", method = RequestMethod.GET)
    public @ResponseBody ResponseData changeProductLabel(HttpServletRequest request,HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String labels=request.getParameter("productLabels");
        String productId=request.getParameter("productId");
        try {
            ProductEntity productEntity=productRepository.get(productId);
            if(productEntity!=null)
            {
                productEntity.setProductLabel(labels);
                productRepository.saveOrUpdate(productEntity);
            }
            else{
                responseData.setFailed(true);
                responseData.setFailedMessage("商品标签修改失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage("商品标签修改失败!");
        } finally {
            return responseData;
        }
    }

    /**
     * 获取所有用户
     * @return
     */
    @RequestMapping(value="/userlistall",method=RequestMethod.GET)
    public @ResponseBody ResponseData getAllUsers(){
        ResponseData responseData = new ResponseData();
        try {
            List<UserEntity> users = userRepository.findAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("users",users);
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
     * 关键词查询用户
     * @param request
     * @return
     */
    @RequestMapping(value="/usersearch",method=RequestMethod.GET)
    public @ResponseBody ResponseData queryUsers(HttpServletRequest request,HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String key = request.getParameter("key");
        try {
            List<UserEntity> users = userRepository.search(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("users",users);
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
     * 获取用户详细信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/usergetdetail",method=RequestMethod.GET)
    public @ResponseBody ResponseData getuser(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String usersid = request.getParameter("userid");
        try {

            UserEntity user= userRepository.get(usersid);
            List<String> ids = JSONArray.parseArray(user.getChildren(),String.class);
            JSONArray jsonArray = new JSONArray();
            for (String id : ids) {
                UserEntity child = userRepository.get(id);
                String headImgUrl = child.getHeadImgUrl();
                JSONObject childJson = new JSONObject();
                childJson.put("id",id);
                childJson.put("head_img_url",headImgUrl);
                jsonArray.add(childJson);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user",user);
            jsonObject.put("childrenInfo",jsonArray);
            responseData.setData(jsonObject);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_USER_GRADE_FAILED);
        }
        finally {
            return responseData;
        }
    }




    /**
     * 获取所有快递
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/expressagelistall",method=RequestMethod.GET)
    public @ResponseBody ResponseData getAllExpressages(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String isnew = request.getParameter("isnew");
        int isNew = Integer.parseInt(isnew);
        try {
            List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
            List<ExpressageEntity> expressages = expressageRepository.query(isNew);
            for(ExpressageEntity expressageEntity :expressages){
                String productId = expressageEntity.getProductId();
                ProductEntity productEntity = productRepository.get(productId);
                String productName = productEntity.getProductName();
                int productState = productEntity.getProductState();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("productName",productName);
                jsonObject.put("productId",productId);
                String productFirstImgUrl = (String)JSONArray.parseArray(productEntity.getProductSlideImg()).get(0);
                jsonObject.put("productFirstImg",productFirstImgUrl);
                jsonObject.put("productState",productState);
                jsonObject.put("expressId",expressageEntity.getId());
                jsonObject.put("expressName",expressageEntity.getExpressageName());
                jsonObject.put("expressCode",expressageEntity.getExpressCode());
                jsonObject.put("priceStand",expressageEntity.getPriceStand());
                jsonObject.put("expressCreateTime",expressageEntity.getCreateTime());
                jsonObjects.add(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("expressages",jsonObjects);
            responseData.setData(jsonObject);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage("获取所有的快递定价信息失败。");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 用商品的关键词查询快递
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/expressagesearch",method=RequestMethod.GET)
    public @ResponseBody ResponseData queryExpressages(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String key = request.getParameter("key");
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        try {
            JSONObject jsonObject = new JSONObject();
            List<ProductEntity> productEntityList = productRepository.search(key);
            if(productEntityList==null||productEntityList.size()==0){
                jsonObject.put("expressages",jsonObjects);
                responseData.setData(jsonObject);
                return  responseData;
            }
            for (ProductEntity productEntity:  productEntityList) {
                List<ExpressageEntity> expressageEntitys = expressageRepository.query(productEntity.getId(),1);
                if(expressageEntitys==null||expressageEntitys.size()==0){
                    continue;
                }
                JSONObject jsonObjectTemp = new JSONObject();
                jsonObjectTemp.put("productName",productEntity.getProductName());
                jsonObjectTemp.put("productId",productEntity.getId());
                String productFirstImgUrl = (String)JSONArray.parseArray(productEntity.getProductSlideImg()).get(0);
                jsonObjectTemp.put("productFirstImg",productFirstImgUrl);
                jsonObjectTemp.put("productState",productEntity.getProductState());
                jsonObjectTemp.put("expressId",expressageEntitys.get(0).getId());
                jsonObjectTemp.put("expressName",expressageEntitys.get(0).getExpressageName());
                jsonObjectTemp.put("expressCode",expressageEntitys.get(0).getExpressCode());
                jsonObjectTemp.put("priceStand",expressageEntitys.get(0).getPriceStand());
                jsonObjectTemp.put("expressCreateTime",expressageEntitys.get(0).getCreateTime());
                jsonObjects.add(jsonObjectTemp);
            }

            jsonObject.put("expressages",jsonObjects);
            responseData.setData(jsonObject);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage("查询指定商品的快递定价信息失败");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 获取快递详情
     * @param request
     * @return
     */
    @RequestMapping(value="/getexpressagedetails",method = RequestMethod.GET)
    public @ResponseBody ResponseData getExpressageDetails(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String expressageId = request.getParameter("expressageid");
        try {
            ExpressageEntity expressage=expressageRepository.get(expressageId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("expressage",expressage);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e,e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage("获取快递信息失败");
        } finally {
            return  responseData;
        }
    }

    //创建快递
    @RequestMapping(value="/storeinformation",method = RequestMethod.GET)
    public @ResponseBody  ResponseData doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,IOException{
        ResponseData responseData = new ResponseData();                         //不是修改，信息并没有锁定，而是增加了信息
        try {
            String expressageIsnew = request.getParameter("expressageIsnew");
            Integer isnew = Integer.parseInt(expressageIsnew);
            ExpressageEntity expressageEntity = new ExpressageEntity();
            expressageEntity.setIsNew(isnew);            //把页面的值创建一个
            String uuid = UUID.randomUUID().toString().replace("-", "");
            expressageEntity.setId(uuid);

            String expresageName=request.getParameter("expresageName");
            expressageEntity.setExpressageName(expresageName);

            String expresage_shipAddress=request.getParameter("shipAddress");
//            expressageEntity.setShipAddress(expresage_shipAddress);
            expressageRepository.save(expressageEntity);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result",true);
            responseData.setData(jsonObject);
        }
        catch (Exception e){
            responseData.setFailed(true);
            responseData.setFailedMessage("保存快递信息失败。");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 保存新的快递定价信息，原来的快递定价信息isnew字段置为 0 ，
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value="/savenewexpress",method = RequestMethod.POST)
    public @ResponseBody  ResponseData changeInformation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException{
        ResponseData responseData = new ResponseData();
        try {
            String expressId = request.getParameter("expressId");
            String price_stand=request.getParameter("price_stand");
            String productId=request.getParameter("productId");
            String express_name = request.getParameter("express_name");
            String expressCode = request.getParameter("expressCode");

            ExpressageEntity oldexpressageEntity = expressageRepository.get(expressId);
            if(oldexpressageEntity!=null){
                oldexpressageEntity.setIsNew(0);
                expressageRepository.saveOrUpdate(oldexpressageEntity);
            }
            ExpressageEntity  expressageEntity = new ExpressageEntity();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            expressageEntity.setId(uuid);
            expressageEntity.setProductId(productId);
            expressageEntity.setPriceStand(price_stand);
            expressageEntity.setExpressageName(express_name);
            expressageEntity.setExpressCode(expressCode);
            expressageEntity.setIsNew(1);
            expressageEntity.setCreateTime(new Timestamp(new Date().getTime()));
            expressageRepository.save(expressageEntity);
        }
        catch (Exception e){
               responseData.setFailed(true);
               responseData.setFailedMessage("更新快递信息失败！");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 创建管理员
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/addmanager",method=RequestMethod.POST)
    public @ResponseBody ResponseData addManager(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String admin_count = request.getParameter("admin_count");
        String admin_name = request.getParameter("admin_name");
        String admin_user_name = request.getParameter("admin_user_name");
        String admin_user_phone = request.getParameter("admin_user_phone");
        String[] auths = (String[]) request.getParameterMap().get("auths[]");
        try {
            logger.info("admin_count"+admin_count);
            logger.info("admin_name"+admin_name);
            logger.info("admin_user_name"+admin_user_name);
            logger.info("admin_user_phone"+admin_user_phone);
            logger.info("auths"+JSON.toJSONString(auths));
            JSONObject jb = new JSONObject();
            jb.put("adminCount",admin_count);
            List<AuthorityEntity> authritys = authorityRepository.query(jb);
            if(authritys.size()>0){
                responseData.setFailed(true);
                responseData.setFailedMessage("账号名已存在，请重新填写账户名");
                return responseData;
            }
            String pass = RandomTools.genRandomString(8);
            String passEncode = Base64Tool.jdkBase64Encode(pass);
            AuthorityEntity entity = new AuthorityEntity();
            entity.setAdminCount(admin_count);
            entity.setAdminName(admin_name);
            entity.setAdminAuthor(JSON.toJSONString(auths));
            entity.setAdminUserName(admin_user_name);
            entity.setAdminUserPhone(admin_user_phone);
            entity.setAdminPass(passEncode);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            entity.setAuthorityId(uuid);
            String authorityId = authorityRepository.save(entity);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authorityId",uuid);
            jsonObject.put("pass",pass);
            jsonObject.put("count",admin_count);
            responseData.setData(jsonObject);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage("创建管理员失败");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 添加权限
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/addAuth",method=RequestMethod.GET)
    public @ResponseBody ResponseData addAuth(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String authid = request.getParameter("btnId");
        String authorId = request.getParameter("auid");
        try {
            AuthorityEntity entity = authorityRepository.get(authorId);
            String auth = entity.getAdminAuthor();
            List<String> auths = JSONArray.parseArray(auth,String.class);
            auths.add(authid);
            entity.setAdminAuthor(JSON.toJSONString(auths));
            authorityRepository.saveOrUpdate(entity);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage("添加管理员权限失败");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 删除权限
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/deleteAuth",method=RequestMethod.GET)
    public @ResponseBody ResponseData deleteAuth(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String authid = request.getParameter("btnId");
        String authorId = request.getParameter("auid");
        try {
            AuthorityEntity entity = authorityRepository.get(authorId);
            String auth = entity.getAdminAuthor();
            List<String> auths = JSONArray.parseArray(auth,String.class);
            auths.remove(authid);
            entity.setAdminAuthor(JSON.toJSONString(auths));
            authorityRepository.saveOrUpdate(entity);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage("添加管理员权限失败");
        }
        finally {
            return responseData;
        }
    }

//    //删除管理员
//    @RequestMapping(value="/deletemanage",method=RequestMethod.GET)
//    public @ResponseBody ResponseData deletemanage(HttpServletRequest request, HttpServletResponse response){
//        ResponseData responseData = new ResponseData();
//        String key = request.getParameter("key");
//        try {
//            List<ExpressageEntity> expressages = expressageRepository.search(key);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("expressages",expressages);
//            responseData.setData(jsonObject);
//        }
//        catch (Exception e){
//            logger.error(e.getMessage(),e);
//            responseData.setFailed(true);
//            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
//        }
//        finally {
//            return responseData;
//        }
//    }

    /**
     * 关键词查询管理员
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/authoritySearch",method=RequestMethod.GET)
    public @ResponseBody ResponseData queryAuthoritys(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String key = request.getParameter("key");
        try {
            List<AuthorityEntity> authoritys = authorityRepository.search(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authoritys",authoritys);
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
     * 获取所有的管理员
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/authorityListall",method=RequestMethod.GET)
    public @ResponseBody ResponseData getAllAuthoritys(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        try {
            List<AuthorityEntity> authoritys = authorityRepository.findAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authoritys",authoritys);
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
     * 获取所有的权限定义
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/getallauthordefine",method=RequestMethod.GET)
    public @ResponseBody ResponseData getallauthordefine(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        try {
            List<AuthorityDefineEntity> authoritys = authorityDefineRepository.findAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authorityDefines",authoritys);
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
     * 获取热门标签
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/hostlabels",method=RequestMethod.GET)
    public @ResponseBody ResponseData getHotLabels(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        try {
            List<LabelEntity> labels = labelRepository.getHotLabels();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("labels",labels);
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

/*
****关键词搜索聊天信息
 */

    @RequestMapping(value="/messageSearch",method=RequestMethod.GET)
    public @ResponseBody ResponseData queryMessages(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String key= request.getParameter("key");Integer key2=0;
        /*if(key=="未读邮件")
        {
             key2=0000000000;
        }else if(key=="已读邮件")
        {
             key2=0000000001;
        }
        else{key2=0000000002;}*/
        try {
            List<MessageEntity> messages = messageRepository.search(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messages",messages);
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

    //标记未读，已读，垃圾邮件

    @RequestMapping(value="/messageChange",method = RequestMethod.GET)
    public @ResponseBody  ResponseData emailChange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException{
        ResponseData responseData = new ResponseData();
        try {
            String messageId = request.getParameter("messageId");

            String messageType = request.getParameter("messageType");
            Integer myInt = new Integer(1);
            if(messageType=="未读邮件")
            {
                myInt=0;
            }
            else if(messageType=="已读邮件")
            {
                myInt=1;
            }
            else if(messageType=="垃圾邮件"){
                myInt=0;
            }
            MessageEntity messageEntity = messageRepository.get(messageId);
            if(messageEntity!=null){
                messageEntity.setMessageType(myInt);
                messageRepository.saveOrUpdate(messageEntity);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result",true);
                responseData.setData(jsonObject);
            }
            else {
                responseData.setFailed(true);
                responseData.setFailedMessage("更新聊天信息失败！");
            }
        }
        catch (Exception e){
            responseData.setFailed(true);
            responseData.setFailedMessage("更新聊天信息失败！");
        }
        finally {
            return responseData;
        }
    }

    /**
     *
     * 获取聊天信息详情
     */
    @RequestMapping(value = "/getMessageInfo", method = RequestMethod.GET)
    public @ResponseBody ResponseData getMessageDetails(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        String messageId = request.getParameter("messageid");
        try {
            JSONObject jsonObject = new JSONObject();
            MessageEntity message= messageRepository.get(messageId);
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String time = sdf.format(message.getTime());
            String content=message.getContent();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1 = DisposeUtil.dispose(message);
            jsonObject1.put("time", time);
            jsonObject1.put("content",content);
            jsonObject.put("messagedetails", jsonObject1);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_PRODUCT_DETAILS_FAILED);
        } finally {
            return responseData;
        }
    }

    /**
     * 发送消息处理，先存redis再存mysql
     * @param request
     * @return
     */
    @RequestMapping(value="/sendBackMessage",method = RequestMethod.GET)
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
     * 批量发送消息处理，先存redis再存mysql
     * @param request
     * @return
     */
    @RequestMapping(value="/sendsomeBackMessage",method = RequestMethod.GET)
    public @ResponseBody ResponseData sendsomeBackMessage(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String data=request.getParameter("data");
        JSONObject j=JSON.parseObject(data);
        String messageFrom=(String)j.get("messageFrom");
        String messageContent=(String)j.get("messageContent");
        String contentType=(String)j.get("contentType");
        String messageType=(String)j.get("messageType");
        String messageTo = (String) j.get("messageTo");
        List<String> msgArr;
        msgArr =JSONArray.parseArray(messageTo,String.class);
        for (int i=0;i<msgArr.size();i++){
            String msgTo=msgArr.get(i);
            boolean sendResult = messageService.sendMessage(msgTo,messageFrom,messageContent,"",contentType,messageType);
            if(!sendResult){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.SEND_MESSAGE_FAILED);
            }
        }

        return responseData;
    }

    /**
     * 获取可用客服列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getusefullcustomservice", method = RequestMethod.GET)
    public @ResponseBody ResponseData getUsefullCustomService(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        try {
            List<String> al = new ArrayList<String>();
            al.add("27");
            al.add("26");
            List<AuthorityEntity> authorityEntities = authorityRepository.getAdminByAuthority(al);
            JSONArray ja = new JSONArray();
            for (AuthorityEntity authority :
                    authorityEntities) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("authorityId", authority.getAuthorityId());
                jsonObject1.put("authorityName",authority.getAdminName());
                ja.add( jsonObject1);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authorityList",ja);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage("获取可用客服列表失败");
        } finally {
            return responseData;
        }
    }

    /**
     * 客服安排发货
     * 1：创建快递记录，2：更新订单状态和信息；3：发送通知信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/sendgood", method = RequestMethod.GET)
    public @ResponseBody ResponseData sendGood(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        HttpSession session = request.getSession();
        String authorid = (String)session.getAttribute("authorid");
        if(authorid==null||authorid==""){
            responseData.setFailed(true);
            responseData.setFailedMessage("发货失败：用户未登录");
            return responseData;
        }
        String orderId = request.getParameter("orderid");
        String trackNumber = request.getParameter("tarckid");
        String expressCode = request.getParameter("express_code");
        String expressName = request.getParameter("express_name");
        if(orderId==null||orderId==""){
            responseData.setFailed(true);
            responseData.setFailedMessage("发货失败：订单号为空");
            return responseData;
        }
        TrackEntity trackEntity = new TrackEntity();
        String trackId =  UUID.randomUUID().toString().replace("-", "");
        trackEntity.setId(trackId);
        trackEntity.setTrackCode(expressCode);
        trackEntity.setTrackNumber(trackNumber);
        trackEntity.setTrackName(expressName);
        try{
            trackRepository.save(trackEntity);
        }catch (Exception e){
            logger.error("发货失败：保存快递信息失败",e);
            responseData.setFailed(true);
            responseData.setFailedMessage("发货失败：保存快递信息失败");
            return responseData;
        }
        OrderEntity order=null;
        Timestamp now = new Timestamp(new Date().getTime());
        try{
            order = orderRepository.get(orderId);
            order.setTrackId(trackId);
            order.setTrackCode(expressCode);
            order.setDeliverTime(now);
            order.setState(3);
            orderRepository.saveOrUpdate(order);
        }catch (Exception e){
            logger.error("发货失败：更新订单信息失败",e);
            responseData.setFailed(true);
            responseData.setFailedMessage("发货失败：更新订单信息失败");
            return responseData;
        }
        if(null==order){
            responseData.setFailed(true);
            responseData.setFailedMessage("发货成功：推送通知时获取订单信息失败");
            return responseData;
        }
        String userId=order.getUserId();
        DateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        String date=sdf.format(new Date());
        String messageContent="尊敬的顾客：您在"+order.getOrderTime()+"购买的【"+order.getDescript()
                +"】已于"+date+"发货。本单使用【"+expressCode+"】快递，快递单号为【"+trackNumber+"】，配送地址为【"
                +order.getSendAddress()+"】，请您留意快递消息，尽快接收；如有信息错误请及时联系客服。";
        boolean sendResult = messageService.sendMessage(userId,authorid,messageContent,"","0","5");
        if(!sendResult){
            responseData.setFailed(true);
            responseData.setFailedMessage("发货成功，通知消息发送失败");
            return responseData;
        }
        return responseData;
    }

    /**
     * 根据配送地址id获取地址详情
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getaddinfo", method = RequestMethod.GET)
    public @ResponseBody ResponseData getAddInfo(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        try {
            String addrId = request.getParameter("addrid");
            SendaddressEntity addr = sendAddressRepository.get(addrId);
            JSONObject addrJSON = new JSONObject();
            addrJSON.put("address",addr);
            responseData.setData(addrJSON);
        } catch (Exception e) {
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage("获取地址详情失败");
        } finally {
            return responseData;
        }
    }

    /**
     * 根据trackid（并非快递单号：trackNumber）获取快递详情
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/gettrackdetail", method = RequestMethod.GET)
    public @ResponseBody ResponseData getTrackDetail(HttpServletRequest request, HttpServletResponse response) {
        ResponseData responseData = new ResponseData();
        try {
            String trackid = request.getParameter("trackid");
            TrackEntity track = trackRepository.get(trackid);
            JSONObject trackJSON = new JSONObject();
            trackJSON.put("track",track);
            responseData.setData(trackJSON);
        } catch (Exception e) {
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage("获取快递详情失败");
        } finally {
            return responseData;
        }
    }

    @Autowired
    BannerRepository bannerRepository;

    /**
     * 获取所有首页滑动内容
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/bannerlistall",method=RequestMethod.GET)
    public @ResponseBody ResponseData getAllBanners(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        try {
            List<BannerEntity> banners = bannerRepository.findAll();
            List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
            for (BannerEntity banner : banners) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",banner.getId());
                jsonObject.put("imgUrl",banner.getImgUrl());
                jsonObject.put("canTab",banner.getCanTab());
                if(banner.getGroupId()==null||banner.getGroupId()==""){

                }
                else{
                    GroupEntity group = groupRepository.get(banner.getGroupId());
                    if(group!=null){
                        jsonObject.put("groupName",group.getGroupName());
                    }
                }
                jsonObjects.add(jsonObject);
            }
            JSONObject returnJsonObject = new JSONObject();
            returnJsonObject.put("banners",jsonObjects);
            responseData.setData(returnJsonObject);
        }
        catch (Exception e){
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage("获取所有的首页滑动内容失败");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 移除指定的首页滑动内容
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/removebanner",method=RequestMethod.GET)
    public @ResponseBody ResponseData removeBanner(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String id = request.getParameter("id");
        try {
            BannerEntity banner = bannerRepository.get(id);
            banner.setState(0);
            bannerRepository.saveOrUpdate(banner);
        }
        catch (Exception e){
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage("移除指定的首页滑动内容失败");
        }
        finally {
            return responseData;
        }
    }

    /**
     * 移除指定的首页滑动内容
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/createbanner",method=RequestMethod.POST)
    public @ResponseBody ResponseData createBanner(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String imgUrl = request.getParameter("imgUrl");
        String canTab = request.getParameter("canTab");
        String groupId = request.getParameter("groupId");
        try {
            BannerEntity banner = new BannerEntity();
            banner.setImgUrl(imgUrl);
            banner.setCanTab(Integer.parseInt(canTab));
            banner.setGroupId(groupId);
            banner.setState(1);
            bannerRepository.save(banner);
        }
        catch (Exception e){
            logger.error(e);
            responseData.setFailed(true);
            responseData.setFailedMessage("创建首页滑动内容失败");
        }
        finally {
            return responseData;
        }
    }

}
