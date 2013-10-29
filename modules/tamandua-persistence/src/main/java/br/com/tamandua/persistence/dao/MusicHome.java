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

import br.com.tamandua.persistence.Music;

/**
 * Home object for domain model class Music.
 * @see br.com.tamandua.persistence.Music
 * @author Hibernate Tools
 */
@Component
public class MusicHome {

    private static final Log log = LogFactory.getLog(MusicHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Music transientInstance) {
        log.debug("persisting Music instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Music persistentInstance) {
        log.debug("removing Music instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Music merge(Music detachedInstance) {
        log.debug("merging Music instance");
        try {
            Music result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Music findById(Long id) {
        log.debug("getting Music instance with id: " + id);
        try {
            Music instance = entityManager.find(Music.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<Music> findByArtistId(Long artistId) {
        log.debug("getting Music instances with artistId: " + artistId);
        List<Music> instances = null;
        try {
            Query query = entityManager.createQuery("from Music where artist.idArtist = ?");
            query.setParameter(1, artistId);

            instances = query.getResultList();
            log.debug("get successful");
            return instances;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public Music findByUri(String uri) {
        log.debug("getting Music instance with uri: " + uri);
        Music instance = null;
        try {
            Query query = entityManager.createQuery("from Music where uri = ?");
            query.setParameter(1, uri);

            instance = (Music) query.getSingleResult();
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

    public List<Music> findByExample(Music instance) {
        log.debug("finding Music instance by example");
        try {
            List<Music> results = (List<Music>) getSession().createCriteria("br.com.tamandua.dao.Music")
                    .add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
	
	public void moderateAllByArtist(Boolean moderate, Long artistId) {
		log.debug("update musics from artist ["+artistId+"] to flag_moderate: " + moderate);
        try {
            Query query = entityManager.createQuery("update Music a set a.flag_moderate = ? where a.artist.idArtist = ?");
            query.setParameter(1, moderate?"S":"N");
            query.setParameter(2, artistId);
            query.executeUpdate();
            
        } catch (RuntimeException re) {
            log.error("moderate all musics by artist failed", re);
            throw re;
        }
    }
	
	public void moderateAllByIds(Boolean moderate, List<Long> ids){
		log.debug("update musics ["+ids+"] to flag_moderate: " + moderate);
		try {
			String sql = "update Music a set a.flag_moderate = ? where a.idMusic in (";
			for(int i=0; i<ids.size(); i++){
				sql += i == 0 ? "" : ",";
				sql += ids.get(i);
			}
			sql += ")";

            Query query = entityManager.createQuery(sql);
            query.setParameter(1, moderate?"S":"N");
            query.executeUpdate();
            
        } catch (RuntimeException re) {
            log.error("moderate all musics by ids failed", re);
            throw re;
        }
	}
	
	public void publishAllByArtist(Boolean publish, Long artistId) {
		log.debug("update musics from artist ["+artistId+"] to flag_public: " + publish);
        try {
            Query query = entityManager.createQuery("update Music a set a.flag_public = ? where a.artist.idArtist = ?");
            query.setParameter(1, publish?"S":"N");
            query.setParameter(2, artistId);
            query.executeUpdate();
            
        } catch (RuntimeException re) {
            log.error("publish all musics by artist failed", re);
            throw re;
        }
    }
	
	public void publishAllByIds(Boolean publish, List<Long> ids){
		log.debug("update musics ["+ids+"] to flag_public: " + publish);
		try {
			String sql = "update Music a set a.flag_public = ? where a.idMusic in (";
			for(int i=0; i<ids.size(); i++){
				sql += i == 0 ? "" : ",";
				sql += ids.get(i);
			}
			sql += ")";

            Query query = entityManager.createQuery(sql);
            query.setParameter(1, publish?"S":"N");
            query.executeUpdate();
            
        } catch (RuntimeException re) {
            log.error("publish all musics by ids failed", re);
            throw re;
        }
	}
    
    public void flush() {
		this.entityManager.flush();
	}

}

