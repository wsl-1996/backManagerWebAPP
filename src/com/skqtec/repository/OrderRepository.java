package com.skqtec.repository;

import com.skqtec.entity.OrderEntity;

import java.util.List;

public interface OrderRepository extends DomainRepository<OrderEntity,String>{
    public List<OrderEntity>query(String userId,String orderState);
    public List<OrderEntity>query(String userId);
    public List<OrderEntity> search(String userId,String key);

}
