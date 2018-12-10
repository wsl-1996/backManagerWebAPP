package com.skqtec.repository;

import com.skqtec.entity.LabelEntity;

import java.util.List;

public interface LabelRepository  extends DomainRepository<LabelEntity,String>{
    public List<LabelEntity> getHotLabels();
}
