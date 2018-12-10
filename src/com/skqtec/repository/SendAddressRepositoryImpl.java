package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.SendaddressEntity;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Repository
public class SendAddressRepositoryImpl implements SendAddressRepository{
    static Logger logger = Logger.getLogger(SendAddressRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public SendaddressEntity load(String id) {
        return (SendaddressEntity)getCurrentSession().load(SendaddressEntity.class,id);
    }

    public SendaddressEntity get(String id) {
        Session session=getCurrentSession();
        SendaddressEntity sendaddressEntity=(SendaddressEntity)session.get(SendaddressEntity.class,id);
        session.close();
        return sendaddressEntity;
    }

    public List<SendaddressEntity> findAll() {
        return getCurrentSession().createQuery("from "+SendaddressEntity.class.getSimpleName()).list();
    }

    public List<SendaddressEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<SendaddressEntity> list = new ArrayList<SendaddressEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(SendaddressEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObject.getString(key);
                c.add(Restrictions.eq(key, value));
            }
            list = c.list();
        } finally {
            if (s != null)
                s.close();
            return list;
        }
    }
    public List<SendaddressEntity>query(String userId){
        Session s = null;
        List<SendaddressEntity> list = new ArrayList<SendaddressEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM `SENDADDRESS` as a where a.USER_id='"+userId+"'").addEntity(SendaddressEntity.class);
            list = q.list();
        }
        catch(Exception e){
            logger.error(e.getMessage(),e);
        }
        finally {
            if (s != null)
                s.close();
            return list;
        }
    }

    public List<SendaddressEntity> search(String key) {
        Session s = null;
        List<SendaddressEntity> list = new ArrayList<SendaddressEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM `SENDADDRESS` as a where a.nickname like '%"+key+"%' and a.email like '%"+key+"%' and a.phone like '%"+key+"%'").addEntity(SendaddressEntity.class);
            list = q.list();
        }
        catch(Exception e){
            logger.error(e.getMessage(),e);
        }
        finally {
            if (s != null)
                s.close();
            return list;
        }
    }

    public void persist(SendaddressEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(SendaddressEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String result = (String)session.save(entity);
        transaction.commit();
        session.close();
        return  result;
    }

    public void saveOrUpdate(SendaddressEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);

        transaction.commit();
        session.close();
    }

    public void delete(String id) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        SendaddressEntity Sendaddress =get(id);
        session.delete(Sendaddress);
        transaction.commit();
        session.close();
    }

    public void flush() {
        getCurrentSession().flush();
    }
}


