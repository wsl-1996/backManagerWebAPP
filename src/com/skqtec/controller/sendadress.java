package com.skqtec.controller;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.SendaddressEntity;
import com.skqtec.entity.UserEntity;
import com.skqtec.repository.SendAddressRepository;
import com.skqtec.repository.UserRepository;
import com.skqtec.tools.SessionTools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Controller
@RequestMapping("applet/sendaddress")
public class sendadress {
    static Logger logger = Logger.getLogger(sendadress.class.getName());
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SendAddressRepository sendAddressRepository;

    /**
     * 获取用户的默认收货地址
     * @param request
     * @return
     */
    @RequestMapping(value="/getdefaultaddress",method=RequestMethod.GET)
    public @ResponseBody
    ResponseData getAddresss(HttpServletRequest request){
        ResponseData responseData=new ResponseData();
        String sessionId=request.getHeader("sessionid");
        try{
            //判断是否登录
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }
            JSONObject jsonObject=new JSONObject();
            UserEntity user=userRepository.get(userId);
            String userAddressId=user.getFirstDeliverAddress();
            SendaddressEntity sendaddress=sendAddressRepository.get(userAddressId);
            String addressId = sendaddress.getId();
            String userCountry=sendaddress.getCountry();
            String userProvince=sendaddress.getProvince();
            String userCity=sendaddress.getCity();
            String userDistricts=sendaddress.getDistricts();
            String userStreet=sendaddress.getStreet();
            String userAddressDetails=sendaddress.getAddressDetail();
            String sendName=sendaddress.getSendName();
            String sendPhone=sendaddress.getSendPhone();
            jsonObject.put("addressId",addressId);
            jsonObject.put("userCountry",userCountry);
            jsonObject.put("userProvince",userProvince);
            jsonObject.put("userCity",userCity);
            jsonObject.put("userDistricts",userDistricts);
            jsonObject.put("userStreet",userStreet);
            jsonObject.put("userAddressDetails",userAddressDetails);
            jsonObject.put("sendName",sendName);
            jsonObject.put("sendPhone",sendPhone);
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("addressinfo",jsonObject);
            responseData.setData(jsonObject1);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_ADDRESS_FAILED);
        }finally{
            return responseData;
        }
    }

    /**
     * 获取用户的全部收货地址
     * @param request
     * @return
     */
    @RequestMapping(value="/getalladdress",method=RequestMethod.GET)
    public @ResponseBody ResponseData getAllAddresss(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String sessionId=request.getHeader("sessionid");
        try {
            //判断是否登录
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }
            List<SendaddressEntity> sendaddress = new ArrayList<SendaddressEntity>();
            sendaddress = sendAddressRepository.query(userId);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("alladdress",sendaddress);
            responseData.setData(jsonObject);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_ALL_ADDRESS_FAILED);
        }finally{
            return responseData;
        }

    }

    /**
     * 添加用户收货地址
     * @param request
     * @return
     */
    @RequestMapping(value="/addaddress",method=RequestMethod.GET)
    public @ResponseBody ResponseData addAddresss(HttpServletRequest request){
        ResponseData responseData=new ResponseData();
        String sessionId=request.getHeader("sessionid");
        String userCountry=request.getParameter("usercountry");
        String userProvince=request.getParameter("userprovince");
        String userCity=request.getParameter("usercity");
        String userDistricts=request.getParameter("userdistricts");
        String userAddressDetails=request.getParameter("useraddressdetails");
        String sendName=request.getParameter("sendname");
        String sendPhone=request.getParameter("sendphone");
        try{
            //判断是否登录
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }
            SendaddressEntity sendaddress=new SendaddressEntity();
            sendaddress.setAddressDetail(userAddressDetails);
            sendaddress.setCity(userCity);
            sendaddress.setCountry("China");
            sendaddress.setDistricts(userDistricts);
            sendaddress.setProvince(userProvince);
            sendaddress.setStreet("");
            sendaddress.setUserId(userId);
            sendaddress.setSendName(sendName);
            sendaddress.setSendPhone(sendPhone);
            sendaddress.setDefaultAddress(0);
            sendaddress.setAddressDetail(userAddressDetails);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            sendaddress.setId(uuid);
            logger.info("********product save returned :  "+sendAddressRepository.save(sendaddress));
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.ADD_ADDRESS_FAILED);
        }finally{
            return responseData;
        }
    }

    /**
     * 修改用户收货地址
     * @param request
     * @return
     */
    @RequestMapping(value="/updateaddress",method=RequestMethod.GET)
    public @ResponseBody ResponseData updateAddresss(HttpServletRequest request){
        ResponseData responseData=new ResponseData();
        String addressId=request.getParameter("addressid");
        String userProvince=request.getParameter("userprovince");
        String userCity=request.getParameter("usercity");
        String userDistricts=request.getParameter("userdistricts");
        String userAddressDetails=request.getParameter("useraddressdetails");
        String sendName=request.getParameter("sendname");
        String sendPhone=request.getParameter("sendphone");
        try{
            SendaddressEntity sendaddress=sendAddressRepository.get(addressId);
            sendaddress.setAddressDetail(userAddressDetails);
            sendaddress.setCity(userCity);
            sendaddress.setDistricts(userDistricts);
            sendaddress.setProvince(userProvince);
            sendaddress.setSendName(sendName);
            sendaddress.setSendPhone(sendPhone);
            sendAddressRepository.saveOrUpdate(sendaddress);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.ADD_ADDRESS_FAILED);
        }finally{
            return responseData;
        }
    }

    /**
     * 设置用户的默认收获地址
     * @param request
     * @return
     */
    @RequestMapping(value="/setdefaultaddress",method=RequestMethod.GET)
    public @ResponseBody ResponseData setDefaultAddresss(HttpServletRequest request){
        ResponseData responseData=new ResponseData();
        String sessionId=request.getHeader("sessionid");
        String FirstDeliverAddress=request.getParameter("fdid");
        try{
            String userId=SessionTools.sessionQuery(sessionId);
            if(userId==null){
                responseData.setFailed(true);
                responseData.setFailedMessage(CommonMessage.NOT_LOG_IN);
                return responseData;
            }
            UserEntity user=userRepository.get(userId);
            String oldFirstDeliverAddress=user.getFirstDeliverAddress();
            user.setFirstDeliverAddress(FirstDeliverAddress);
            if(oldFirstDeliverAddress!=null&&(!oldFirstDeliverAddress.equals(""))) {
                SendaddressEntity oldSendaddress = sendAddressRepository.get(oldFirstDeliverAddress);
                oldSendaddress.setDefaultAddress(0);
                sendAddressRepository.saveOrUpdate(oldSendaddress);
            }
            SendaddressEntity sendaddress=sendAddressRepository.get(FirstDeliverAddress);
            sendaddress.setDefaultAddress(1);
            sendAddressRepository.saveOrUpdate(sendaddress);
            userRepository.saveOrUpdate(user);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.SET_DEFAULT_ADDRESS_FAILED);
        }finally{
            return responseData;
        }
    }

    /**
     * 删除收货地址
     * @param request
     * @return
     */
    @RequestMapping(value="/deleteaddress",method=RequestMethod.GET)
    public @ResponseBody ResponseData deleteAddresss(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String fdid = request.getParameter("fdid");
        try {
            sendAddressRepository.delete(fdid);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.DELETE_ADDRESS_FAILED);
        } finally {
            return responseData;
        }
    }

}
