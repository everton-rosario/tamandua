package br.com.tamandua.persistence.dao;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA


import static org.hibernate.criterion.Example.create;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.ejb.EntityManagerImpl;
import org.springframework.stereotype.Component;

import br.com.tamandua.persistence.Image;

/**
 * Home object for domain model class Image.
 * @see br.com.tamandua.persistence.Image
 * @author Hibernate Tools
 */
@Component
public class ImageHome {

    private static final Log log = LogFactory.getLog(ImageHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Image transientInstance) {
        log.debug("persisting Image instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Image persistentInstance) {
        log.debug("removing Image instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Image merge(Image detachedInstance) {
        log.debug("merging Image instance");
        try {
            Image result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Image findById(Long id) {
        log.debug("getting Image instance with id: " + id);
        try {
            Image instance = entityManager.find(Image.class, id);
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
        } catch (NoResultException nre) {
            return null;
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
    
    public void flush() {
        this.entityManager.flush();
    }

    public List<Image> findByProvider(String provider) {
        log.debug("getting Image instance with provider: " + provider);
        List<Image> instances = Collections.EMPTY_LIST;
        try {
            Query query = entityManager.createQuery("from Image where provider = ?");
            query.setParameter(1, provider);
            instances = query.getResultList();
            log.debug("get successful");
            return instances;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
