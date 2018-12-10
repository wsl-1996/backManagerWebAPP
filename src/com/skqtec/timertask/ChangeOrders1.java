package com.skqtec.timertask;

import com.skqtec.entity.CommentEntity;
import com.skqtec.entity.OrderEntity;
import com.skqtec.repository.CommentRepository;
import com.skqtec.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class ChangeOrders1 {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CommentRepository commentRepository;

    /**
     * 定时任务：订单下单后15天内系统自动关闭订单，并设置评价好评
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    public void run(){

        List<OrderEntity>list=orderRepository.findAll();
        for(OrderEntity order:list){
            if(order.getDeliverTime()==null)
                return;
            long deliverTime=order.getDeliverTime().getTime();
            long reciptTime=order.getReceiptTime().getTime();
            long currentTime=new Date().getTime();
            if((currentTime-deliverTime)/(1000*60*60*24)>15) {
                order.setState(4);
                order.setReceiptTime(new Timestamp(new Date().getTime()));
                orderRepository.saveOrUpdate(order);
            }
            if(order.getReceiptTime()==null)
                return;
            if((currentTime-reciptTime)/(1000*60*60*24)>15){
                order.setState(5);
                CommentEntity comment=new CommentEntity();
                String uuid = UUID.randomUUID().toString().replace("-", "");
                comment.setId(uuid);
                comment.setCommentContent("默认好评");
                comment.setCommentTime(new Timestamp(new Date().getTime()));
                comment.setEvaluateLabel("默认好评");
                comment.setGroupId(order.getGroupId());
                comment.setProductId(order.getProductId());
                comment.setProductStyle(order.getTypeSpecification());
                comment.setStarLevel(5);
                comment.setUserId(order.getUserId());
                commentRepository.save(comment);
            }
        }
    }

}
