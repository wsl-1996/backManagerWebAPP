package com.skqtec.repository;

import com.skqtec.entity.GroupEntity;

import java.util.List;

public interface GroupRepository extends DomainRepository<GroupEntity,String> {

    List<GroupEntity> query(String page, String state);

}
