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

import br.com.tamandua.persistence.MusicAlbum;
import br.com.tamandua.persistence.MusicAlbumId;

/**
 * Home object for domain model class MusicArtist.
 * @see br.com.tamandua.persistence.MusicArtist
 * @author Hibernate Tools
 */
@Component
public class MusicAlbumHome {

    private static final Log log = LogFactory.getLog(MusicAlbumHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(MusicAlbum transientInstance) {
        log.debug("persisting MusicArtist instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(MusicAlbum persistentInstance) {
        log.debug("removing MusicArtist instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public MusicAlbum merge(MusicAlbum detachedInstance) {
        log.debug("merging MusicArtist instance");
        try {
            MusicAlbum result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public MusicAlbum findById(MusicAlbumId id) {
        log.debug("getting MusicArtist instance with id: " + id);
        try {
            MusicAlbum instance = entityManager.find(MusicAlbum.class, id);
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

    public List<MusicAlbum> findByExample(MusicAlbum instance) {
        log.debug("finding MusicArtist instance by example");
        try {
            List<MusicAlbum> results = (List<MusicAlbum>) getSession().createCriteria(
                    "br.com.tamandua.dao.MusicAlbum").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<MusicAlbum> findByMusicId(Long idMusic) {
        log.debug("getting MusicArtist's instances by id music: " + idMusic);
        List<MusicAlbum> instances = null;
        try {
            Query query = entityManager.createQuery("from MusicAlbum where id.idMusic = ?");
            query.setParameter(1, idMusic);
            instances = (List<MusicAlbum>) query.getResultList();
            log.debug("get MusicAlbum's successful");
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
