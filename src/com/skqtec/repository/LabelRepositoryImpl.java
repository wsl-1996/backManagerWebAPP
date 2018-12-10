package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.LabelEntity;
import org.apache.log4j.Logger;
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
public class LabelRepositoryImpl implements LabelRepository{
    static Logger logger = Logger.getLogger(LabelRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }


    public LabelEntity load(String id) {
        return (LabelEntity)getCurrentSession().load(LabelEntity.class,id);
    }

    public LabelEntity get(String id) {
        Session session=getCurrentSession();
        LabelEntity labelEntity=(LabelEntity)session.get(LabelEntity.class,id);
        session.close();
        return labelEntity;
    }

    public List<LabelEntity> findAll() {
        return null;
    }

    public List<LabelEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<LabelEntity> list = new ArrayList<LabelEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(LabelEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObject.getString(key);
                c.add(Restrictions.eq(key, value));
            }
//          c.add(Restrictions.eq("aname", name));//eq是等于，gt是大于，lt是小于,or是或
            list = c.list();
        } finally {
            if (s != null)
                s.close();
            return list;
        }
    }

    public List<LabelEntity> search(String key) {
        return null;
    }

    public void persist(LabelEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(LabelEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Serializable pKey = session.save(entity);
        transaction.commit();
        return  (String)pKey;
    }

    public void saveOrUpdate(LabelEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(String id) {
        LabelEntity label = load(id);
        getCurrentSession().delete(label);
    }

    public void flush() {
        getCurrentSession().flush();
    }

    public List<LabelEntity> getHotLabels() {
        List<LabelEntity> list = new ArrayList<LabelEntity>();
        Session s=null;
        try{
            s = getCurrentSession();
            Criteria c = s.createCriteria(LabelEntity.class);
            c.add(Restrictions.gt("hotDegree", 0));
            list = c.list();
        }
        catch (Exception e){
            logger.error(e);
        }
        finally {
            if (s != null)
                s.close();
            return list;
        }

    }
}
