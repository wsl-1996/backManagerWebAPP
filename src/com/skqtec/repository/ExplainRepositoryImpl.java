package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.ExplainEntity;
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
public class ExplainRepositoryImpl implements ExplainRepository{

    static Logger logger = Logger.getLogger(ExplainRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public ExplainEntity load(String id) {
        return (ExplainEntity)getCurrentSession().load(ExplainEntity.class,id);
    }

    public ExplainEntity get(String id) {
        Session session=getCurrentSession();
        ExplainEntity explainEntity=(ExplainEntity)session.get(ExplainEntity.class,id);
        session.close();
        return explainEntity;
    }

    public List<ExplainEntity> findAll() {
        return getCurrentSession().createQuery("from "+ExplainEntity.class.getSimpleName()).list();
    }

    public List<ExplainEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<ExplainEntity> list = new ArrayList<ExplainEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(ExplainEntity.class);
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

    public List<ExplainEntity> search(String key) {
        return null;
    }

    public void persist(ExplainEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(ExplainEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String result = (String)session.save(entity);
        transaction.commit();
        session.close();
        return  result;
    }

    public void saveOrUpdate(ExplainEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(String id) {
        ExplainEntity explain = load(id);
        getCurrentSession().delete(explain);
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
