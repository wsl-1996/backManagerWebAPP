package com.skqtec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.entity.ProductEntity;
import com.skqtec.repository.GroupRepository;
import com.skqtec.repository.ProductClassifyCodeRepository;
import com.skqtec.repository.ProductRepository;
import com.skqtec.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/applet/products")
public class products {

    static Logger logger = Logger.getLogger(products.class.getName());

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductClassifyCodeRepository productClassifyCodeRepository;
    @Autowired
    private GroupRepository groupRepository;
    /***
     * 管理页面上传图片（买家秀页面上传图片也用该接口）
     * @return
     */
    @RequestMapping()
    public @ResponseBody
    String upload(){
        return "";
    }


    /**
     * 获取所有商品
     * @return
     */
    @RequestMapping(value="/listall",method=RequestMethod.GET)
    public @ResponseBody ResponseData getAllProducts(){
        ResponseData responseData = new ResponseData();
        try {
            List<ProductEntity> products = productRepository.findAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("products",products);
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
     * 关键词查询商品
     * @param request
     * @return
     */
    @RequestMapping(value="/search",method=RequestMethod.GET)
    public @ResponseBody ResponseData queryProducts(HttpServletRequest request){
        ResponseData responseData = new ResponseData();
        String key = request.getParameter("key");
        try {
            List<ProductEntity> products = productRepository.search(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("products",products);
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
     * 获取商品款式
     * @param request
     * @return
     */
    @RequestMapping(value="/getproductstyle",method=RequestMethod.GET)
    public @ResponseBody ResponseData getProductStyle(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String productId = request.getParameter("productid");
        try {
            ProductEntity product=productRepository.get(productId);
            String productStyle=product.getProductStyle();
            String productFistImg=product.getProductFistImg();
            String stylePrice=product.getStylePrice();
            JSONObject j=new JSONObject();
            j.put("productStyle",JSONArray.parseArray(productStyle));
            j.put("stylePrice",JSONArray.parseArray(stylePrice));
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("Style",j);
            jsonObject.put("FistImg",productFistImg);
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_PRODUCT_STYLE_FAILED);
        } finally {
            return responseData;
        }
    }

    /**
     * 获取商品参数
     * @param request
     * @return
     */
    @RequestMapping(value="/getproductparameter",method=RequestMethod.GET)
    public @ResponseBody ResponseData getProductParameter(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String productId = request.getParameter("productid");
        try {
            ProductEntity product = productRepository.get(productId);
            JSONObject jsonObject=JSON.parseObject(product.getPackStand());
            System.out.println(jsonObject.toString());
            responseData.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.GET_PRODUCT_PARAMETER_FAILED);
        } finally {
            return responseData;
        }
    }
}
