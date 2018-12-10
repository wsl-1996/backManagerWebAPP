package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.CommentEntity;

import java.util.List;
public interface CommentRepository extends DomainRepository<CommentEntity,String> {
    public JSONObject getDegereeOfPraise(String productId);
    public List<CommentEntity>query(String page,String productId);
    void saveImg(String commentId,String imgs);
}
