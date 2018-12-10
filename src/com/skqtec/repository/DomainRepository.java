package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

public interface DomainRepository <T,PK extends Serializable>{
    T load(PK id);   //尽量不要用load方法

    T get(PK id);

    List<T> findAll();

    List<T> query(JSONObject jsonObject);

    List<T> search(String key);

    void persist(T entity);

    PK save(T entity);

    void saveOrUpdate(T entity);

    void delete(PK id);

    void flush();

}
