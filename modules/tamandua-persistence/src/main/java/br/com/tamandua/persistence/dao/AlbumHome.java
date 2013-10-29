package br.com.tamandua.persistence.dao;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA


import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.ejb.EntityManagerImpl;
import org.springframework.stereotype.Component;

import br.com.tamandua.persistence.Album;
import br.com.tamandua.persistence.Artist;

/**
 * Home object for domain model class Album.
 * @see br.com.tamandua.persistence.Album
 * @author Hibernate Tools
 */
@Component
public class AlbumHome {

    private static final Log log = LogFactory.getLog(AlbumHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Album transientInstance) {
        log.debug("persisting Album instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Album persistentInstance) {
        log.debug("removing Album instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Album merge(Album detachedInstance) {
        log.debug("merging Album instance");
        try {
            Album result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Album findById(Long id) {
        log.debug("getting Album instance with id: " + id);
        try {
            Album instance = entityManager.find(Album.class, id);
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
    
    public Album findByUri(String uri) {
        log.debug("getting Album instance with uri: " + uri);
        Album instance = null;
        try {
            Query query = entityManager.createQuery("from Album where uri = ?");
            query.setParameter(1, uri);
            List resultList = query.getResultList();
            if (resultList != null && !resultList.isEmpty()) {
                instance = (Album) resultList.get(0);
            } else {
                instance = null;
            }
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<Album> findByExample(Album instance) {
        log.debug("finding Album instance by example");
        try {
            List<Album> results = (List<Album>) getSession().createCriteria("br.com.tamandua.dao.Album")
                                                            .add( create(instance) )
                                                            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public void flush() {
        this.entityManager.flush();
    } 

}
