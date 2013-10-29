package br.com.tamandua.persistence.dao;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA


import static org.hibernate.criterion.Example.create;

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

import br.com.tamandua.persistence.Lyric;

/**
 * Home object for domain model class Lyric.
 * @see br.com.tamandua.persistence.Lyric
 * @author Hibernate Tools
 */
@Component
public class LyricHome {

    private static final Log log = LogFactory.getLog(LyricHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Lyric transientInstance) {
        log.debug("persisting Lyric instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Lyric persistentInstance) {
        log.debug("removing Lyric instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Lyric merge(Lyric detachedInstance) {
        log.debug("merging Lyric instance");
        try {
            Lyric result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Lyric findById(long id) {
        log.debug("getting Lyric instance with id: " + id);
        try {
            Lyric instance = entityManager.find(Lyric.class, id);
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

    public List<Lyric> findByExample(Lyric instance) {
        log.debug("finding Lyric instance by example");
        try {
            List<Lyric> results = (List<Lyric>) getSession().createCriteria("br.com.tamandua.dao.Lyric")
                    .add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

	public Lyric findByUri(String uri, String provider) {
        log.debug("getting Lyric instance with uri: " + uri + " provider: " + provider);
        Lyric instance = null;
        try {
            Query query = entityManager.createQuery("from Lyric where uri = ? and provider = ?");
            query.setParameter(1, uri);
            query.setParameter(2, provider);
            instance = (Lyric) query.getSingleResult();
            log.debug("get successful");
            return instance;
        } catch (NoResultException nre) {
            return null;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
	}

	public void flush() {
		this.entityManager.flush();
	}

}
