package com.skqtec.repository;

import com.skqtec.entity.BillEntity;

import java.util.List;

public interface BillRepository extends  DomainRepository<BillEntity,String> {
    public List<BillEntity> query(String userId,int type);
}
