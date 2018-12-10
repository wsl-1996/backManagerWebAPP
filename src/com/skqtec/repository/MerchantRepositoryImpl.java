package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.MerchantEntity;
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
public class MerchantRepositoryImpl implements  MerchantRepository{
    static Logger logger = Logger.getLogger(MerchantRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public MerchantEntity load(String id) {
        return (MerchantEntity)getCurrentSession().load(MerchantEntity.class,id);
    }

    public MerchantEntity get(String id) {
        Session session=getCurrentSession();
        MerchantEntity merchantEntity=(MerchantEntity)session.get(MerchantEntity.class,id);
        session.close();
        return merchantEntity;
    }

    public List<MerchantEntity> findAll() {
        return getCurrentSession().createQuery("from "+MerchantEntity.class.getSimpleName()).list();
    }

    public List<MerchantEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<MerchantEntity> list = new ArrayList<MerchantEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(MerchantEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObject.getString(key);
                c.add(Restrictions.eq(key, value));
            }
            list = c.list();
            for (MerchantEntity merchant : list) {
                System.out.println(merchant.getName());
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

    public List<MerchantEntity> search(String key) {
        Session s = null;
        List<MerchantEntity> list = new ArrayList<MerchantEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM MERCHANT as a where a.name like '%"+key+"%' or a.discription like '%"+key+"%'").addEntity(MerchantEntity.class);
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

    public void persist(MerchantEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(MerchantEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Serializable pKey = session.save(entity);

        transaction.commit();
        session.close();
        return  (String)pKey;
    }

    public void saveOrUpdate(MerchantEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(String id) {
        MerchantEntity image = load(id);
        getCurrentSession().delete(image);
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
