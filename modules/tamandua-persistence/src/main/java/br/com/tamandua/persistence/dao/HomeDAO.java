package br.com.tamandua.persistence.dao;

import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.ejb.EntityManagerImpl;

import br.com.tamandua.persistence.Image;

public class HomeDAO<T, K> {

    static final Log log = LogFactory.getLog(ImageHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public HomeDAO() {
        super();
    }

    public void persist(T transientInstance) {
        log.debug("persisting " + transientInstance.getClass().getName() +" instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(T persistentInstance) {
        log.debug("removing " + persistentInstance.getClass().getName() +" instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public T merge(T detachedInstance) {
        log.debug("merging " + detachedInstance.getClass().getName() +" instance");
        try {
            T result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public T findById(K id) {
        log.debug("getting Image instance with id: " + id);
        try {
            T instance = null; //entityManager.find(T.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public Image findByUri(String uri) {
        log.debug("getting Image instance with uri: " + uri);
        Image instance = null;
        try {
            Query query = entityManager.createQuery("from Image where uri = ?");
            query.setParameter(1, uri);
            instance = (Image) query.getSingleResult();
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private Session getSession() {
        Session session = null;  
        if (entityManager.getDelegate() instanceof EntityManagerImpl) {  
            EntityManagerImpl entityManagerImpl = (EntityManagerImpl) entityManager.getDelegate();  
            session = entityManagerImpl.getSession();  
        } else {  
            session = (Session) entityManager.getDelegate();  
        }  
        return session;
    }

    public List<Image> findByExample(Image instance) {
        log.debug("finding Image instance by example");
        try {
            List<Image> results = (List<Image>) getSession().createCriteria("br.com.tamandua.dao.Image")
                    .add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

}