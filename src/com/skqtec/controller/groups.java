package com.skqtec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.GroupEntity;
import com.skqtec.entity.ProductEntity;
import com.skqtec.repository.CommentRepository;
import com.skqtec.repository.GroupRepository;
import com.skqtec.repository.ProductRepository;
import com.skqtec.tools.Datetools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/applet/groups")
public class groups {

    static Logger logger = Logger.getLogger(groups.class.getName());

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommentRepository commentRepository;


    /**
     * 创建团购
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/create",method=RequestMethod.POST)
    public @ResponseBody ResponseData createProduct(HttpServletRequest request, HttpServletResponse response)
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
        String[] contentimgs = request.getParameterValues("productContentimgs[]");
        String endtime = request.getParameter("groupbuy_end_time");
        String starttime = request.getParameter("groupbuy_start_time");
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setGroupName(name);
        groupEntity.setGroupDiscription(info);
        groupEntity.setProductId(productid);
        groupEntity.setUserId(ownerid);
        groupEntity.setGroupPrice(Double.parseDouble(price));
        groupEntity.setProductCost(Double.parseDouble(cost));
        groupEntity.setDeliverAddress(deliver_add);
        groupEntity.setGroupCount(Integer.parseInt(count));     //同时存在商品和团购的id，会不会冲突
        groupEntity.setGroupFirstImg(firstImg);
        groupEntity.setEndTime(Timestamp.valueOf(endtime));
        groupEntity.setGroupSlideImg(JSON.toJSONString(slideimgs));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        groupEntity.setId(uuid);
        try{
            logger.info("********group save returned :  "+groupRepository.save(groupEntity));
            JSONObject data = new JSONObject();
            data.put("groupid",uuid);
            responseData.setData(data);
        }
        catch (Exception e){
            logger.error(e,e.fillInStackTrace());
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.CREATE_GROUP_FAILED);
        }
        finally {
            return responseData;
        }
    }

    /**
     * 获取所有团购
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/listall",method=RequestMethod.GET)
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
     * 关键词查询团购
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/search",method=RequestMethod.GET)
    public @ResponseBody ResponseData queryGroups(HttpServletRequest request, HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String key = request.getParameter("keyword");
        try {
            List<GroupEntity> groups = groupRepository.search(key);
            JSONObject j = getJsonObject(groups);
            responseData.setData(j);
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

    private JSONObject getJsonObject(List<GroupEntity> groups) {
        List<JSONObject>jsonobject=new ArrayList<JSONObject>();
        int i=0;
        for(GroupEntity group:groups){
            JSONObject jsonObject=new JSONObject();
            ProductEntity product=productRepository.get(group.getProductId());
            String group_name=group.getGroupName();
            String product_first_image=product.getProductFistImg();
            JSONObject degree_of_praise=commentRepository.getDegereeOfPraise(group.getProductId());
            Date end_time=Datetools.timeStampToDate(group.getEndTime());
            String product_id=group.getProductId();
            String group_id=group.getId();
            String product_state_info=null;
            if(product.getProductState()==0){
                product_state_info="自营";
            }
            else
                product_state_info="";
            String group_type_info=null;
            if(group.getGroupType()==0){
                group_type_info="动态团";
            }
            else
                group_type_info="固定团";
            jsonObject.put("product_id",product_id);
            jsonObject.put("group_id",group_id);
            jsonObject.put("product_first_image",product_first_image);
            jsonObject.put("group_name",group_name);
            jsonObject.put("product_price",group.getGroupPrice());
            jsonObject.put("degree_of_praise",degree_of_praise);
            jsonObject.put("last_time",end_time);
            jsonObject.put("product_state",product_state_info);
            jsonObject.put("group_type",group_type_info);
            jsonObject.put("group_count",group.getGroupCount());
            jsonObject.put("offered_count",group.getOfferedCount());
            i++;
            jsonobject.add(jsonObject);
        }
        JSONObject j=new JSONObject();
        j.put("groups",jsonobject);
        return j;
    }


    /**
     * 获取团购列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/getgroup",method=RequestMethod.GET)
    public @ResponseBody ResponseData getCurrentGroup(HttpServletRequest request,HttpServletResponse response){
        ResponseData responseData=new ResponseData();
        String page=request.getParameter("page");
        String state=request.getParameter("state");
        try{
            List<GroupEntity> groups=groupRepository.query(page,state);
            JSONObject j = getJsonObject(groups);
            responseData.setData(j);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_LIST_FAILED);
        }finally{
            return responseData;
        }
    }

    /**
     * 从团购列表进入，获取团购详情。
     * @param request
     * @param response
     * @return 团购的详细信息包括团购商品的详情
     */
    @RequestMapping(value="/getgroupinfo",method=RequestMethod.GET)
    public @ResponseBody ResponseData getGroupDetails(HttpServletRequest request,HttpServletResponse response){
        ResponseData responseData=new ResponseData();
        String groupId=request.getParameter("groupid");
        try{
            JSONObject jsonObject=new JSONObject();
            GroupEntity group=groupRepository.get(groupId);
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String endTime = sdf.format(group.getEndTime());
            jsonObject.put("group",group);
            String productid = group.getProductId();
            ProductEntity product = productRepository.get(productid);
            jsonObject.put("product",product);
            responseData.setData(jsonObject);
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_GROUP_DETAILS_FAILED);
        }
        finally {
            return responseData;
        }
    }

}
