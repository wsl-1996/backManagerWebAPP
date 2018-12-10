package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
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
public class GroupRepositoryImpl implements  GroupRepository{
    static Logger logger = Logger.getLogger(GroupRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }

    public GroupEntity load(String id) {
        return (GroupEntity)getCurrentSession().load(GroupEntity.class,id);
    }

    public GroupEntity get(String id) {
        Session session=getCurrentSession();
        GroupEntity groupEntity=(GroupEntity)session.get(GroupEntity.class,id);
        session.close();
        return groupEntity;
    }

    public List<GroupEntity> findAll() {
        return getCurrentSession().createQuery("from "+GroupEntity.class.getSimpleName()).list();
    }

    public List<GroupEntity> query(JSONObject jsonObject) {
        Session s = null;
        List<GroupEntity> list = new ArrayList<GroupEntity>();
        try {
            s = getCurrentSession();
            Criteria c = s.createCriteria(GroupEntity.class);
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = (String)it.next();
                String value = jsonObject.getString(key);
                c.add(Restrictions.eq(key, value));
            }
            list = c.list();
            for (GroupEntity group : list) {
                System.out.println(group.getGroupName());
            }
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            if (s != null)
                s.close();
            return list;
        }
    }
    public List<GroupEntity>query(String page,String state){
        int p=Integer.parseInt(page);
        int k=(p-1)*10;
        List<GroupEntity>list=new ArrayList<GroupEntity>();
        try{
            Session s=getCurrentSession();
            Query q=s.createSQLQuery("select * from `GROUP` as a where a.group_state="+state+" limit "+k+","+"10").addEntity(GroupEntity.class);
            list=q.list();
            for (GroupEntity group : list) {
                System.out.println(group.getGroupName());
            }
            s.close();
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }finally{
            return list;
        }
    }
    public List<GroupEntity> search(String key) {
        Session s = null;
        List<GroupEntity> list = new ArrayList<GroupEntity>();
        try {
            s = getCurrentSession();
            //list = s.createQuery("from "+ GroupEntity.class.getSimpleName()+" as a where a.groupName like '%"+key+"%' and a.groupDiscription like '%"+key+"%' and a.deliverAddress like '%"+key+"%'").list();
//            Criteria c = s.createCriteria(GroupEntity.class);
//            c.add(Restrictions.like("groupName", "%"+key+"%"));
//            c.add(Restrictions.like("groupDiscription", "%"+key+"%"));
//            c.add(Restrictions.like("deliverAddress", "%"+key+"%"));
//            list = c.list();
            Query q = s.createSQLQuery("SELECT * FROM `GROUP` as a where (a.group_name like '%"+key+"%' or a.group_discription like '%"+key+"%') and a.group_state=1").addEntity(GroupEntity.class);
            list = q.list();
//            for (GroupEntity group : list) {
//                System.out.println(group.getGroupName());
//            }
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

    public void persist(GroupEntity entity) {
        Session session = getCurrentSession();
        session.persist(entity);
    }

    public String save(GroupEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String result = (String)session.save(entity);
        transaction.commit();
        session.close();
        return  result;
    }

    public void saveOrUpdate(GroupEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
        session.close();
    }

    public void delete(String id) {
        GroupEntity group = load(id);
        getCurrentSession().delete(group);
    }

    public void flush() {
        getCurrentSession().flush();
    }
}
