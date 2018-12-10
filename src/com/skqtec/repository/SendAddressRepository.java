package com.skqtec.repository;

import com.skqtec.entity.SendaddressEntity;

import java.util.List;

public interface SendAddressRepository extends DomainRepository<SendaddressEntity,String> {
    public List<SendaddressEntity>query(String userId);
}
