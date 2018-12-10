package com.skqtec.controller;

import com.skqtec.common.CommonMessage;
import com.skqtec.common.ResponseData;
import com.skqtec.repository.CashBackRepository;
import com.skqtec.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/applet")
public class test {
    @Autowired
    private CashBackRepository cashBackRepository;
    @Autowired
    private GroupRepository groupRepository;
    @RequestMapping(value="/test",method=RequestMethod.GET)
    public @ResponseBody ResponseData test(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        try {
            /*Map<String,String> reqData=new HashMap<String, String>();
            reqData.put("appId","wx5733cafea467c980");
            reqData.put("nonceStr","fjVOtMdZ9f0ALW74");
            reqData.put("package","prepay_id=wx03174838573134b762d0256c3218565446");
            reqData.put("signType","MD5");
            reqData.put("timeStamp","1533289949");
            String paySign=WXPayUtil.generateSignature(reqData,"lijie1987110801spaceorg1234qwerl",WXPayConstants.SignType.MD5);
            System.out.println(paySign);*/

            /*int totalMoney=cashBackRepository.statisticCashback("01");
            System.out.println(totalMoney);*/

            /*DateFormat dateFormatterChina = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);//格式化输出
            TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");//获取时区
            dateFormatterChina.setTimeZone(timeZoneChina);//设置系统时区
            Date curDate = new Date();//获取系统时间
            System.out.println(dateFormatterChina.format(curDate));

            String id = request.getParameter("id");
            GroupEntity groupEntity=groupRepository.get(id);
            Timestamp endTime=groupEntity.getEndTime();
            ChangeGroupState.addToRedis("04",endTime);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            List<GroupEntity> groups = groupRepository.query(jsonObject);
            System.out.println(groups.toString());*/

            /*KdniaoTrackQueryAPI api = new KdniaoTrackQueryAPI();
            try {
                String result = api.getOrderTracesByJson("ZTO", "219094353460");
                System.out.print(result);

            } catch (Exception e) {
                e.printStackTrace();
            }*/

        } catch (Exception e) {
            responseData.setFailed(true);
            responseData.setFailedMessage(CommonMessage.TEST_FAILED);
        } finally {
            return responseData;
        }
    }
}
