package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.BannerEntity;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class BannerRepositoryImpl implements BannerRepository{

    static Logger logger = Logger.getLogger(BannerRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public BannerEntity load(String id) {
        return (BannerEntity)getCurrentSession().load(BannerEntity.class,Integer.parseInt(id));
    }

    public BannerEntity get(String id) {

        Session session=getCurrentSession();
        BannerEntity entity=(BannerEntity)session.get(BannerEntity.class,Integer.parseInt(id));
        session.close();
        return entity;
    }

    public List<BannerEntity> findAll() {
        return getCurrentSession().createQuery("from "+BannerEntity.class.getSimpleName()+" where state=1").list();

    }

    public List<BannerEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<BannerEntity> list = new ArrayList<BannerEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(BannerEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                int value = Integer.parseInt(jsonObject.getString(key));
                c.add(Restrictions.eq(key, value));
            }
            list = c.list();
        } finally {
            if (s != null)
                s.close();
            return list;
        }
    }

    public List<BannerEntity> search(String key) {
//        Session s = null;
//        List<BannerEntity> list = new ArrayList<BannerEntity>();
//        try {
//            s = getCurrentSession();                                //SELECT语句有错误，需要改正
//            Query q = s.createSQLQuery("SELECT * FROM AUTHORITY as a where a.authority_name like '%"+key+"%' or a.authority_id like '%"+key+"%' or a.product_info like '%"+key+"%' or a.product_label like '%"+key+"%'").addEntity(ProductEntity.class);
//            list = q.list();
//        }
//        catch(Exception e){
//            logger.error(e.getMessage(),e);
//        }
//        finally {
//            if (s != null)
//                s.close();
//            return list;
//        }
        return null;
    }

    public void persist(BannerEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(BannerEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return  "true";
    }

    public void saveOrUpdate(BannerEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
        session.close();
    }

    public void delete(String id) {
        BannerEntity entity = load(id);
        getCurrentSession().delete(entity);
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
