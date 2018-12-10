package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.TrackEntity;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Repository
public class TrackRepositoryImpl implements TrackRepository {
    static Logger logger = Logger.getLogger(TrackRepositoryImpl.class.getName());
    @Autowired
    private SessionFactory sessionFactory;
    private Session getCurrentSession() {
        return this.sessionFactory.openSession();
    }
    public List<TrackEntity> query(String isAccomplish){
        Session s = null;
        List<TrackEntity> list = new ArrayList<TrackEntity>();
        try {
            s = getCurrentSession();
            Query q = s.createSQLQuery("SELECT * FROM `TRACK` as a where a.is_accomplish="+isAccomplish).addEntity(TrackEntity.class);
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
    public TrackEntity load(String id) {
        return null;
    }

    public TrackEntity get(String id) {
        Session session=getCurrentSession();
        TrackEntity trackEntity=(TrackEntity)session.get(TrackEntity.class,id);
        session.close();
        return trackEntity;
    }

    public List<TrackEntity> findAll() {
        return null;
    }

    public List<TrackEntity> query(JSONObject jsonObject) {
        return null;
    }

    public List<TrackEntity> search(String key) {
        return null;
    }

    public void persist(TrackEntity entity) {

    }

    public String save(TrackEntity entity) {
        Session session = null;
        session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Serializable pKey = session.save(entity);
        transaction.commit();
        session.close();
        return  (String)pKey;
    }

    public void saveOrUpdate(TrackEntity entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
        session.close();
    }
    public String find(String trackId){
        Session session=getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query q=session.createSQLQuery("select * from `TRACK` where track_number="+trackId).addEntity(TrackEntity.class);
        List<TrackEntity>list=q.list();
        transaction.commit();
        session.close();
        return list.get(0).getTrack();
    }
    public void delete(String id) {

    }

    public void flush() {

    }
}
