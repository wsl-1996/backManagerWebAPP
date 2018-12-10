package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.CashbackEntity;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Repository
public class CashBackRepositoryImpl implements CashBackRepository {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public CashbackEntity load(String id) {
        return (CashbackEntity)getCurrentSession().load(CashbackEntity.class,id);
    }

    public CashbackEntity get(String id) {
        Session session=getCurrentSession();
        CashbackEntity cashbackEntity=(CashbackEntity)session.get(CashbackEntity.class,id);
        session.close();
        return cashbackEntity;
    }

    public List<CashbackEntity> findAll() {
        Session session=getCurrentSession();
        List<CashbackEntity>list=session.createQuery("from "+CashbackEntity.class.getSimpleName()).list();
        session.close();
        return  list;
    }
    public int statisticCashback(String userId){
        Session s = null;
        int totalMoney=0;
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM `CASHBACK` as a where a.cashback_USER_id="+userId).addEntity(CashbackEntity.class);
            List<CashbackEntity>list=q.list(); //****查询需要加上时间限定******//
            for(CashbackEntity cashback:list){
                totalMoney+=cashback.getSumMoney();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (s != null)
                s.close();
            return totalMoney;
        }
    }
    public List<CashbackEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<CashbackEntity> list = new ArrayList<CashbackEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(CashbackEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObject.getString(key);
                c.add(Restrictions.eq(key, value));
            }
//            c.add(Restrictions.eq("aname", name));//eq是等于，gt是大于，lt是小于,or是或

            list = c.list();

        } finally {
            if (s != null)
                s.close();
            return list;
        }
    }

    public List<CashbackEntity> search(String key) {
        return null;
    }

    public void persist(CashbackEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(CashbackEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Serializable pKey = session.save(entity);

        transaction.commit();
        return  (String)pKey;
    }

    public void saveOrUpdate(CashbackEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(String id) {
        CashbackEntity image = load(id);
        getCurrentSession().delete(image);
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
