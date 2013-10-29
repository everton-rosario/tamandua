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

import br.com.tamandua.persistence.Tag;

/**
 * Home object for domain model class Tag.
 * @see br.com.tamandua.persistence.Tag
 * @author Hibernate Tools
 */
@Component
public class TagHome {

    private static final Log log = LogFactory.getLog(TagHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Tag transientInstance) {
        log.debug("persisting Tag instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Tag persistentInstance) {
        log.debug("removing Tag instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Tag merge(Tag detachedInstance) {
        log.debug("merging Tag instance");
        try {
            Tag result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Tag findById(Long id) {
        log.debug("getting Tag instance with id: " + id);
        try {
            Tag instance = entityManager.find(Tag.class, id);
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

    public List<Tag> findByExample(Tag instance) {
        log.debug("finding Tag instance by example");
        try {
            List<Tag> results = (List<Tag>) getSession().createCriteria("br.com.tamandua.dao.Tag").add(
                    create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<Tag> findByEntity(Long idEntityDescriptor, Long idEntity, String type) {
        log.debug("getting Tag's instances by id_entity_descriptor["+idEntityDescriptor+"] and id_entity["+idEntity+"]");
        List<Tag> instances = null;
        try {
            Query query = entityManager.createQuery("from Tag where idEntity = ? and entityDescriptor.idEntityDescriptor = ? and type = ?");
            query.setParameter(1, idEntity);
            query.setParameter(2, idEntityDescriptor);
            query.setParameter(3, type);
            instances = (List<Tag>) query.getResultList();
            log.debug("get Tag's successful");
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
