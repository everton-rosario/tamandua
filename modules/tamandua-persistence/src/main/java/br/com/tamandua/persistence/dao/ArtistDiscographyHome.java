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

import br.com.tamandua.persistence.ArtistDiscography;
import br.com.tamandua.persistence.ArtistDiscographyId;

/**
 * Home object for domain model class ArtistDiscography.
 * @see br.com.tamandua.persistence.ArtistDiscography
 * @author Hibernate Tools
 */
@Component
public class ArtistDiscographyHome {

    private static final Log log = LogFactory.getLog(ArtistDiscographyHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(ArtistDiscography transientInstance) {
        log.debug("persisting ArtistDiscography instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(ArtistDiscography persistentInstance) {
        log.debug("removing ArtistDiscography instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public ArtistDiscography merge(ArtistDiscography detachedInstance) {
        log.debug("merging ArtistDiscography instance");
        try {
            ArtistDiscography result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public ArtistDiscography findById(ArtistDiscographyId id) {
        log.debug("getting ArtistDiscography instance with id: " + id);
        try {
            ArtistDiscography instance = entityManager.find(ArtistDiscography.class, id);
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

    public List<ArtistDiscography> findByExample(ArtistDiscography instance) {
        log.debug("finding ArtistDiscography instance by example");
        try {
            List<ArtistDiscography> results = (List<ArtistDiscography>) getSession().createCriteria(
                    "br.com.tamandua.dao.ArtistDiscography").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
