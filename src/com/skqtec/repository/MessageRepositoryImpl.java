package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.MessageEntity;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Repository
public class MessageRepositoryImpl implements MessageRepository{
    static Logger logger = Logger.getLogger(MessageRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;
    public MessageEntity load(String id) {
        return null;
    }
    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }
    public MessageEntity get(String id) {
        Session session=getCurrentSession();
        MessageEntity messageEntity=(MessageEntity)session.get(MessageEntity.class,id);
        session.close();
        return messageEntity;
    }

    public List<MessageEntity> findAll() {
        return getCurrentSession().createQuery("from "+MessageEntity.class.getSimpleName()).list();
    }

    public List<MessageEntity> query(JSONObject jsonObject) {
        return null;
    }

    public List<MessageEntity> search(String key) {
        Session s = null;
        List<MessageEntity> list = new ArrayList<MessageEntity>();
        try {
            int key_int = Integer.parseInt(key);
        }
        catch (Exception e){
            logger.error(e);
            return list;
        }

        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM MESSAGE as a where a.message_type like '%"+key+"%' or a.message_type like '%"+key+"%'").addEntity(MessageEntity.class);
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

    public void persist(MessageEntity entity) {

    }

    public String save(MessageEntity entity) {
        Session session = null;
        session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Serializable pKey = session.save(entity);
        transaction.commit();
        session.close();
        return  (String)pKey;
    }

    public void saveOrUpdate(MessageEntity entity) {
        Session session = null;
        session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
    }

    public void delete(String id) {

    }

    public void flush() {

    }
}
