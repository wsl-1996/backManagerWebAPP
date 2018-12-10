package com.skqtec.timertask;

import com.skqtec.entity.BillEntity;
import com.skqtec.entity.UserEntity;
import com.skqtec.repository.BillRepository;
import com.skqtec.repository.CashBackRepository;
import com.skqtec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class StatisticCashBack {
    @Autowired
    private CashBackRepository cashBackRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private UserRepository userRepository;
    @Scheduled(cron = "0 0 0 1 * ? ")
    public void run() {
        List<UserEntity>list=userRepository.findAll();
        for(UserEntity user:list){
            int totalMoney=cashBackRepository.statisticCashback(user.getId());
            if(totalMoney!=0) {
                BillEntity bill = new BillEntity();
                bill.setBalance(bill.getBalance() + totalMoney);
                bill.setDate(new Timestamp(new Date().getTime()));
                String uuid = UUID.randomUUID().toString().replace("-", "");
                double balance = user.getBalance()+totalMoney;
                bill.setId(uuid);
                bill.setInOut(0);
                bill.setBalance(balance);
                bill.setMoney(totalMoney);
                bill.setDescription("返现");
                bill.setType(0);
                bill.setUserId(user.getId());
                billRepository.save(bill);
                user.setBalance(balance);
                userRepository.saveOrUpdate(user);
            }
        }
    }
}
