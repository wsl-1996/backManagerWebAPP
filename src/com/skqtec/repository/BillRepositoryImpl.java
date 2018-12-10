package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.BillEntity;
import com.skqtec.entity.GroupEntity;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Repository
public class BillRepositoryImpl implements BillRepository {
    static Logger logger = Logger.getLogger(BillRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public BillEntity load(String id) {
        return (BillEntity)getCurrentSession().load(BillEntity.class,id);
    }

    public BillEntity get(String id) {
        Session session=getCurrentSession();
        BillEntity billEntity=(BillEntity)session.get(BillEntity.class,id);
        session.close();
        return billEntity;
    }

    public List<BillEntity> findAll() {
        return getCurrentSession().createQuery("from "+BillEntity.class.getSimpleName()).list();
    }

    public List<BillEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<BillEntity> list = new ArrayList<BillEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(BillEntity.class);
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
    public List<BillEntity> query(String userId,int type){
        Session s = null;
        List<BillEntity> list = new ArrayList<BillEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM `BILL` as a where a.user_id='"+userId+"' and a.type="+type).addEntity(BillEntity.class);
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
    public List<BillEntity> search(String key) {
        Session s = null;
        List<BillEntity> list = new ArrayList<BillEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM ketuanDB.`BILL` as a where a.Bill_name like '%"+key+"%' and a.Bill_produce_address like '%"+key+"%' and a.Bill_info like '%"+key+"%' and a.Bill_label like '%"+key+"%'").addEntity(GroupEntity.class);
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

    public void persist(BillEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(BillEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String result = (String)session.save(entity);
        transaction.commit();
        session.close();
        return  result;
    }

    public void saveOrUpdate(BillEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(String id) {
        BillEntity Bill = load(id);
        getCurrentSession().delete(Bill);
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
