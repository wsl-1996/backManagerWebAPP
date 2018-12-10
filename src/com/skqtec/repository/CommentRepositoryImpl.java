package com.skqtec.repository;

import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.CommentEntity;
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
    public class CommentRepositoryImpl implements CommentRepository{
        static Logger logger = Logger.getLogger(CommentRepositoryImpl.class.getName());
        @Autowired
        private SessionFactory sessionFactory;

        private Session getCurrentSession() {
            return this.sessionFactory.openSession();
        }

        public CommentEntity load(String id) {
            return (CommentEntity)getCurrentSession().load(CommentEntity.class,id);
        }

        public CommentEntity get(String id) {
            Session session=getCurrentSession();
            CommentEntity commentEntity=(CommentEntity)session.get(CommentEntity.class,id);
            session.close();
            return commentEntity;
        }

        public List<CommentEntity> findAll() {
            return getCurrentSession().createQuery("from "+CommentEntity.class.getSimpleName()).list();
        }

        public List<CommentEntity> query(JSONObject jsonObject) {
            Session s = null;
            List<CommentEntity> list = new ArrayList<CommentEntity>();
            try {
                s = getCurrentSession();
                Criteria c = s.createCriteria(CommentEntity.class);
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

        public List<CommentEntity> search(String key) {
            Session s = null;
            List<CommentEntity> list = new ArrayList<CommentEntity>();
            try {
                s = getCurrentSession();
                Query q = s.createSQLQuery("SELECT * FROM `COMMENT` as a where a.name like '%"+key+"%' and a.discription like '%"+key+"%'").addEntity(CommentEntity.class);
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
        public JSONObject getDegereeOfPraise(String productId){
            Session s=null;
            double k=0;
            JSONObject jsonObject=new JSONObject();
            try {
                s = getCurrentSession();
                Query q1 = s.createSQLQuery("SELECT * FROM `COMMENT` as a where a.product_id='"+productId+"'");
                double k1= q1.list().size();
                Query q2 = s.createSQLQuery("SELECT * FROM `COMMENT` as a where a.product_id='"+productId+"' and a.star_level>3");
                double k2= q2.list().size();
                long k3=Double.doubleToLongBits(k1);
                if(k3!=0)
                    k=k2/k1;
                else
                    k=1;
            }
            catch(Exception e){
                logger.error(e.getMessage(),e);
            }
            finally {
                if (s != null)
                    s.close();
                jsonObject.put("favorableRate",String.valueOf(k*100)+"%");
                jsonObject.put("favorableNums",k);
                return jsonObject;
            }

        }
        public void persist(CommentEntity entity) {
            Session session = getCurrentSession();
            session.persist(entity);
        }

        public String save(CommentEntity entity){
            Session session=null;
            session=getCurrentSession();
            Transaction transaction = session.beginTransaction();
            Serializable pKey = session.save(entity);
            transaction.commit();
            session.close();
            return  (String)pKey;
        }
        public List<CommentEntity>query(String page,String productId){
            int p=Integer.parseInt(page);
            int k=(p-1)*10;
            List<CommentEntity>list=new ArrayList<CommentEntity>();
            try{
                Session s=getCurrentSession();
                Query q=s.createSQLQuery("select * from `COMMENT` as a where a.product_id='"+productId+"' limit "+k+","+"10").addEntity(CommentEntity.class);
                list=q.list();
                for (CommentEntity comment : list) {
                    System.out.println(comment.getId());
                }
                s.close();
            }catch(Exception e){
                logger.error(e.getMessage(),e);
            }finally{

                return list;
            }
        }

        public void saveOrUpdate(CommentEntity entity) {
            Session s=getCurrentSession();
            Transaction transaction=s.beginTransaction();
            s.saveOrUpdate(entity);
            transaction.commit();
            s.close();
        }
        public void saveImg(String commentId,String imgs){
            Session s = getCurrentSession();
            try {
                Transaction transaction=s.beginTransaction();
                Query q=s.createSQLQuery("UPDATE `COMMENT` set attach_imgs='"+imgs+"' where id='"+commentId+"'");
                q.executeUpdate();
                transaction.commit();
            }catch (Exception e){
                e.printStackTrace();
            }
            s.close();
        }
        public void delete(String id) {
            CommentEntity image = load(id);
            getCurrentSession().delete(image);
        }

        public void flush() {
            getCurrentSession().flush();
        }
    }


