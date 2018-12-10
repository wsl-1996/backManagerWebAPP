package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.AuthorityDefineEntity;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Repository
public class AuthorityDefineRepositoryImpl implements AuthorityDefineRepository{

    static Logger logger = Logger.getLogger(AuthorityDefineRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public AuthorityDefineEntity load(String id) {
        return (AuthorityDefineEntity)getCurrentSession().load(AuthorityDefineEntity.class,id);
    }

    public AuthorityDefineEntity get(String id) {
        Session session=getCurrentSession();
        AuthorityDefineEntity authorityDefineEntity=(AuthorityDefineEntity)session.get(AuthorityDefineEntity.class,id);
        session.close();
        return authorityDefineEntity;
    }

    public List<AuthorityDefineEntity> findAll() {
        return getCurrentSession().createQuery("from "+AuthorityDefineEntity.class.getSimpleName()).list();

    }

    public List<AuthorityDefineEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<AuthorityDefineEntity> list = new ArrayList<AuthorityDefineEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(AuthorityDefineEntity.class);
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

    public List<AuthorityDefineEntity> search(String key) {
        return null;
    }

    public void persist(AuthorityDefineEntity entity) {

    }

    public String save(AuthorityDefineEntity entity) {
        return null;
    }

    public void saveOrUpdate(AuthorityDefineEntity entity) {

    }

    public void delete(String id) {

    }

    public void flush() {

    }
}
