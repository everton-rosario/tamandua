package br.com.tamandua.persistence.dao;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA


import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.ejb.EntityManagerImpl;
import org.springframework.stereotype.Component;

import br.com.tamandua.persistence.Moderation;

/**
 * Home object for domain model class Moderation.
 * @see br.com.tamandua.persistence.Moderation
 * @author Hibernate Tools
 */
@Component
public class ModerationHome {

    private static final Log log = LogFactory.getLog(ModerationHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Moderation transientInstance) {
        log.debug("persisting Moderation instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Moderation persistentInstance) {
        log.debug("removing Moderation instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Moderation merge(Moderation detachedInstance) {
        log.debug("merging Moderation instance");
        try {
            Moderation result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Moderation findById(Long id) {
        log.debug("getting Moderation instance with id: " + id);
        try {
            Moderation instance = entityManager.find(Moderation.class, id);
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

    public List<Moderation> findByExample(Moderation instance) {
        log.debug("finding Moderation instance by example");
        try {
            List<Moderation> results = (List<Moderation>) getSession().createCriteria(
                    "br.com.tamandua.dao.Moderation").add(create(instance)).list();
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
}
