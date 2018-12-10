package com.skqtec.repository;

import com.skqtec.entity.UserEntity;

public interface UserRepository extends DomainRepository<UserEntity,String> {
    public UserEntity query(String openid);
}
