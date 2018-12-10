package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.ImageEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Repository
public class ImageRepositoryImpl implements  ImageRepository{

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public ImageEntity load(String id) {
        return (ImageEntity)getCurrentSession().load(ImageEntity.class,id);
    }

    public ImageEntity get(String id) {
        Session session=getCurrentSession();
        ImageEntity imageEntity=(ImageEntity)session.get(ImageEntity.class,id);
        session.close();
        return imageEntity;
    }

    public List<ImageEntity> findAll() {
        return null;
    }

    public List<ImageEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<ImageEntity> list = new ArrayList<ImageEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(ImageEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObject.getString(key);
                c.add(Restrictions.eq(key, value));
            }
//            c.add(Restrictions.eq("aname", name));//eq是等于，gt是大于，lt是小于,or是或

            list = c.list();
            for (ImageEntity image : list) {
                System.out.println(image.getUrl());
            }
        } finally {
            if (s != null)
                s.close();
            return list;
        }
    }

    public List<ImageEntity> search(String key) {
        return null;
    }

    public void persist(ImageEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(ImageEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Serializable pKey = session.save(entity);

        transaction.commit();
        return  (String)pKey;
    }

    public void saveOrUpdate(ImageEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(String id) {
        ImageEntity image = load(id);
        getCurrentSession().delete(image);
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
