package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.ExpressageEntity;
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
public class ExpressageRepositoryImpl implements ExpressageRepository {
    static Logger logger = Logger.getLogger(ExpressageRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public ExpressageEntity load(String id) {
        return (ExpressageEntity)getCurrentSession().load(ExpressageEntity.class,id);
    }

    public ExpressageEntity get(String id) {
        return (ExpressageEntity)getCurrentSession().get(ExpressageEntity.class,id);
    }

    public List<ExpressageEntity> findAll() {
        return getCurrentSession().createQuery("from "+ExpressageEntity.class.getSimpleName()).list();
    }

    public List<ExpressageEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<ExpressageEntity> list = new ArrayList<ExpressageEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(ExpressageEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObject.getString(key);
                c.add(Restrictions.eq(key, value));
            }
            list = c.list();
        }catch (Exception e){
            logger.error(e);
        }
        finally {
            if (s != null)
                s.close();
            return list;
        }
    }

    public List<ExpressageEntity> search(String key) {
        Session s = null;
        List<ExpressageEntity> list = new ArrayList<ExpressageEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM EXPRESSAGE as a where a.expressage_name like '%"+key+"%' or a.expressage_details like '%"+key+"%'").addEntity(ExpressageEntity.class);
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

    public List<ExpressageEntity> query(String productId, int isNew) {
        Session s = null;
        List<ExpressageEntity> list = new ArrayList<ExpressageEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM EXPRESSAGE as a where a.is_new =  "+isNew+" and a.product_id = '"+productId+"'").addEntity(ExpressageEntity.class);
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

    public List<ExpressageEntity> query(int isNew) {
        Session s = null;
        List<ExpressageEntity> list = new ArrayList<ExpressageEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM EXPRESSAGE as a where a.is_new =  "+isNew).addEntity(ExpressageEntity.class);
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

    public List<ExpressageEntity>query(String expressageId,String expressageState){
        Session s = null;
        List<ExpressageEntity> list = new ArrayList<ExpressageEntity>();
        try {
            s = getCurrentSession();
            int state=Integer.parseInt(expressageState);
            Query q=null;
            if(state==0)//获取全部快递
                q= s.createSQLQuery("SELECT * FROM `EXPRESSAGE` as a where a.expressage_id="+expressageId+" and a.state>"+state).addEntity(ExpressageEntity.class);
            else
                q= s.createSQLQuery("SELECT * FROM `EXPRESSAGE` as a where a.expressage_id="+expressageId+" and a.state="+state).addEntity(ExpressageEntity.class);
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

    public void persist(ExpressageEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(ExpressageEntity entity) {
        Session session = null;
        session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Serializable pKey = session.save(entity);

        transaction.commit();
        return  (String)pKey;
    }

    public void saveOrUpdate(ExpressageEntity entity) {
        Session session = null;
        session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
    }

    public void delete(String id) {
        Session session = null;
        session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        ExpressageEntity image = get(id);
        session.delete(image);
        transaction.commit();
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
