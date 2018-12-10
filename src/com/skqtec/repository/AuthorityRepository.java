package com.skqtec.repository;

import com.skqtec.entity.AuthorityEntity;

import java.util.List;

public interface AuthorityRepository extends DomainRepository<AuthorityEntity,String> {
    public List<AuthorityEntity> getAdminByAuthority(List<String> authorities);
}
