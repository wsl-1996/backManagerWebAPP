package com.skqtec.repository;

import com.skqtec.entity.TrackEntity;

import java.util.List;

public interface TrackRepository extends DomainRepository<TrackEntity,String> {
    public List<TrackEntity> query(String isAccomplish);
    public String find(String trackId);
}
