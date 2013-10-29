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

import br.com.tamandua.persistence.Artist;
import br.com.tamandua.persistence.MusicArtist;
import br.com.tamandua.persistence.MusicArtistId;

/**
 * Home object for domain model class MusicArtist.
 * @see br.com.tamandua.persistence.MusicArtist
 * @author Hibernate Tools
 */
@Component
public class MusicArtistHome {

    private static final Log log = LogFactory.getLog(MusicArtistHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(MusicArtist transientInstance) {
        log.debug("persisting MusicArtist instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(MusicArtist persistentInstance) {
        log.debug("removing MusicArtist instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public MusicArtist merge(MusicArtist detachedInstance) {
        log.debug("merging MusicArtist instance");
        try {
            MusicArtist result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public MusicArtist findById(MusicArtistId id) {
        log.debug("getting MusicArtist instance with id: " + id);
        try {
            MusicArtist instance = entityManager.find(MusicArtist.class, id);
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

    public List<MusicArtist> findByExample(MusicArtist instance) {
        log.debug("finding MusicArtist instance by example");
        try {
            List<MusicArtist> results = (List<MusicArtist>) getSession().createCriteria(
                    "br.com.tamandua.dao.MusicArtist").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<MusicArtist> findByMusicId(Long idMusic) {
        log.debug("getting MusicArtist's instances by id music: " + idMusic);
        List<MusicArtist> instances = null;
        try {
            Query query = entityManager.createQuery("from MusicArtist where music.idMusic = ?");
            query.setParameter(1, idMusic);
            instances = (List<MusicArtist>) query.getResultList();
            log.debug("get MusicArtist's successful");
            return instances;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public void flush() {
        this.entityManager.flush();
    }
}
