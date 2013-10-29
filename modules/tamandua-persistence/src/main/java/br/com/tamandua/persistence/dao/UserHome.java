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

import br.com.tamandua.persistence.User;

/**
 * Home object for domain model class User.
 * @see br.com.tamandua.persistence.User
 * @author Hibernate Tools
 */
@Component
public class UserHome {

    private static final Log log = LogFactory.getLog(UserHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(User transientInstance) {
        log.debug("persisting User instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(User persistentInstance) {
        log.debug("removing User instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public User merge(User detachedInstance) {
        log.debug("merging User instance");
        try {
            User result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public User findById(Long id) {
        log.debug("getting User instance with id: " + id);
        try {
            User instance = entityManager.find(User.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public User findByEmail(String email) {
        log.debug("getting User instance with email: " + email);
        User instance = null;
        try {
        	Query query = entityManager.createQuery("from User where email = ?");
            query.setParameter(1, email);
            List resultList = query.getResultList();
            if (resultList != null && !resultList.isEmpty()) {
                instance = (User) resultList.get(0);
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
    
    public void flush() {
		this.entityManager.flush();
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

    public List<User> findByExample(User instance) {
        log.debug("finding User instance by example");
        try {
            List<User> results = (List<User>) getSession().createCriteria("br.com.tamandua.dao.User").add(
                    create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
