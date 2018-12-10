package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.OrderEntity;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    static Logger logger = Logger.getLogger(OrderRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public OrderEntity load(String id) {
        return (OrderEntity)getCurrentSession().load(OrderEntity.class,id);
    }

    public OrderEntity get(String id) {
        Session session=getCurrentSession();
        OrderEntity orderEntity=(OrderEntity)session.get(OrderEntity.class,id);
        session.close();
        return orderEntity;
    }

    public List<OrderEntity> findAll() {
        Session session=getCurrentSession();
        List<OrderEntity>list=session.createQuery("from "+OrderEntity.class.getSimpleName()).list();
        session.close();
        return  list;
    }

    public List<OrderEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<OrderEntity> list = new ArrayList<OrderEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(OrderEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObject.getString(key);
                c.add(Restrictions.eq(key, value));
            }
            list = c.list();
            for (OrderEntity Order : list) {
                //System.out.println(Order.getName());
            }
        }catch (Exception e){
            logger.error(e);
        }
        finally {
            if (s != null)
                s.close();
            return list;
        }
    }

    public List<OrderEntity> search(String key) {
        Session s = null;
        List<OrderEntity> list = new ArrayList<OrderEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM `ORDER` as a where a.send_name like '%"+key+"%' or a.descript like '%"+key+"%'").addEntity(OrderEntity.class);
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

    public List<OrderEntity> search(String userId,String key) {
        Session s = null;
        List<OrderEntity> list = new ArrayList<OrderEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM `ORDER` as a where a.descript like '%"+key+"%' and a.user_id='"+userId+"' and a.state>=0").addEntity(OrderEntity.class);
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
    public List<OrderEntity>query(String userId,String orderState){
        Session s = null;
        List<OrderEntity> list = new ArrayList<OrderEntity>();
        try {
            s = getCurrentSession();
            int state=Integer.parseInt(orderState);
            Query q=null;
            q= s.createSQLQuery("SELECT * FROM `ORDER` as a where a.user_id='"+userId+"' and a.state="+state).addEntity(OrderEntity.class);
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

    public List<OrderEntity>query(String userId){
        Session s = null;
        List<OrderEntity> list = new ArrayList<OrderEntity>();
        try {
            s = getCurrentSession();
            Query q=null;
            q= s.createSQLQuery("SELECT * FROM `ORDER` as a where a.user_id='"+userId+"'").addEntity(OrderEntity.class);
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

    public void persist(OrderEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(OrderEntity entity) {
        Session session = null;
        session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Serializable pKey = session.save(entity);
        transaction.commit();
        session.close();
        return  (String)pKey;
    }

    public void saveOrUpdate(OrderEntity entity) {
        Session session = null;
        session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
        session.close();
    }

    public void delete(String id) {
        Session session = null;
        session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        OrderEntity image = get(id);
        session.delete(image);
        transaction.commit();
        session.close();
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
