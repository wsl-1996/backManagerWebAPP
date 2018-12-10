package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.AuthorityEntity;
import com.skqtec.entity.ProductEntity;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class AuthorityRepositoryImpl  implements  AuthorityRepository{
    static Logger logger = Logger.getLogger(AuthorityRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public AuthorityEntity load(String  id) {
        return (AuthorityEntity)getCurrentSession().load(AuthorityEntity.class,id);
    }

    public AuthorityEntity get(String id) {
        Session session=getCurrentSession();
        AuthorityEntity AuthorityEntity=(AuthorityEntity)session.get(AuthorityEntity.class,id);
        session.close();
        return AuthorityEntity;
    }

//    private String authorityId;
//    private String adminName;
//    private String adminCount;
//    private String adminPass;
//    private String adminAuthor;

    public List<AuthorityEntity> findAll() {
        return getCurrentSession().createQuery("select authorityId,adminName,adminCount,adminAuthor from "+AuthorityEntity.class.getSimpleName()).list();
    }

    public List<AuthorityEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<AuthorityEntity> list = new ArrayList<AuthorityEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(AuthorityEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObject.getString(key);
                c.add(Restrictions.eq(key, value));
            }
            list = c.list();
//            for (AuthorityEntity image : list) {
//                System.out.println(image.getAuthorityName());
//            }
        } finally {
            if (s != null)
                s.close();
            return list;
        }
    }

    public List<AuthorityEntity> search(String key) {
        Session s = null;
        List<AuthorityEntity> list = new ArrayList<AuthorityEntity>();
        try {
            s = getCurrentSession();                                //SELECT语句有错误，需要改正
            Query q = s.createSQLQuery("SELECT * FROM AUTHORITY as a where a.authority_name like '%"+key+"%' or a.authority_id like '%"+key+"%' or a.product_info like '%"+key+"%' or a.product_label like '%"+key+"%'").addEntity(ProductEntity.class);
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

    public void persist(AuthorityEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(AuthorityEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String result = (String)session.save(entity);
        transaction.commit();
        session.close();
        return  result;
    }

    public void saveOrUpdate(AuthorityEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
        session.close();
    }

    public void delete(String id) {
        AuthorityEntity product = load(id);
        getCurrentSession().delete(product);
    }

    public void flush() {
        getCurrentSession().flush();
    }

    public List<AuthorityEntity> getAdminByAuthority(List<String> authorities) {
        Session s = null;
        List<AuthorityEntity> list = new ArrayList<AuthorityEntity>();
        StringBuilder sb = new StringBuilder("SELECT * FROM `AUTHORITY` as a where ");
        for (String authory : authorities) {
            sb.append(" a.admin_author like '%\""+authory+"\"%' and ");
        }
        int index = sb.lastIndexOf("and");
        sb.delete(index,sb.length()-1);
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery(sb.toString()).addEntity(AuthorityEntity.class);
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
}
