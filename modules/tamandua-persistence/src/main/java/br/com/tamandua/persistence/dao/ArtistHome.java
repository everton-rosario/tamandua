package br.com.tamandua.persistence.dao;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA


import static org.hibernate.criterion.Example.create;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.ejb.EntityManagerImpl;
import org.springframework.stereotype.Component;

import br.com.tamandua.persistence.Artist;

/**
 * Home object for domain model class Artist.
 * @see br.com.tamandua.persistence.Artist
 * @author Hibernate Tools
 */
@Component
public class ArtistHome {

    private static final Log log = LogFactory.getLog(ArtistHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Artist transientInstance) {
        log.debug("persisting Artist instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Artist persistentInstance) {
        log.debug("removing Artist instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Artist merge(Artist detachedInstance) {
        log.debug("merging Artist instance");
        try {
            Artist result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Artist findById(Long id) {
        log.debug("getting Artist instance with id: " + id);
        try {
            Artist instance = entityManager.find(Artist.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public Artist findByUri(String uri) {
        log.debug("getting Artist instance with uri: " + uri);
        Artist instance = null;
        try {
            Query query = entityManager.createQuery("from Artist where uri = ?");
            query.setParameter(1, uri);
            List resultList = query.getResultList();
            if (resultList != null && !resultList.isEmpty()) {
                instance = (Artist) resultList.get(0);
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

    public Artist findByName(String name) {
        log.debug("getting Artist instance with name: " + name);
        Artist instance = null;
        try {
            Query query = entityManager.createQuery("from Artist where name = ?");
            query.setParameter(1, name.toUpperCase());
            List resultList = query.getResultList();
            if (resultList != null && !resultList.isEmpty()) {
                instance = (Artist) resultList.get(0);
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

    public List<Artist> findByExample(Artist instance) {
        log.debug("finding Artist instance by example");
        try {
            List<Artist> results = (List<Artist>) getSession().createCriteria("br.com.tamandua.dao.Artist")
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

    public List<Artist> findAll() {
        log.debug("getting all Artists");
        try {
            Query query = entityManager.createQuery("from Artist");
            List resultList = query.getResultList();
            log.debug("get successful returned " + resultList.size() + " artists.");
            return resultList;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public Artist findByImageId(Long idImage) {
        log.debug("getting Artist instance with idImage: " + idImage);
        Artist instance = null;
        try {
            Query query = entityManager.createQuery("select a from Artist a join a.allImages images where images.idImage = ?");
            query.setParameter(1, idImage);
            instance = (Artist) query.getSingleResult();
            log.debug("get successful");
            return instance;
        } catch (NonUniqueResultException nure) {
            return null;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<Artist> findByLetter(String letter) {
        log.debug("getting Artist instance with letter: " + letter);
        List<Artist> instances = null;
        try {
            Query query = entityManager.createQuery("from Artist a where a.letters like ?");
            query.setParameter(1, "%" + letter + "%");
            instances = query.getResultList();
            log.debug("get successful");
            return instances;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List<Artist> findWithLyricsPublishedByLetter(String letter) {
        log.debug("getting Artist instance with lyrics published and letter: " + letter);
        List<Artist> instances = null;
        try {
            Query query = entityManager.createNativeQuery("select * from artist where letters like ? and id_artist in " +
            		"(select id_artist from music where flag_publication = 'S' and id_music in " +
            		"(select id_music from lyric where original_flag = 'S' and text is not null and text != ''))", Artist.class);
            query.setParameter(1, "%" + letter + "%");
            instances = query.getResultList();
            log.debug("get successful");
            return instances;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
	
	public void moderateAllByLetters(Boolean moderate, String letters) {
		log.debug("update artists with letters ["+letters+"] to flag_moderate: " + moderate);
        try {
            Query query = entityManager.createQuery("update Artist a set a.flag_moderate = ? where a.letters like ?");
            query.setParameter(1, moderate?"S":"N");
            query.setParameter(2, "%" + letters + "%");
            query.executeUpdate();
            
        } catch (RuntimeException re) {
            log.error("moderate all artists by letters failed", re);
            throw re;
        }
    }
	
	public void moderateAllByIds(Boolean moderate, List<Long> ids){
		log.debug("update artists ["+ids+"] to flag_moderate: " + moderate);
		try {
			String sql = "update Artist a set a.flag_moderate = ? where a.idArtist in (";
			for(int i=0; i<ids.size(); i++){
				sql += i == 0 ? "" : ",";
				sql += ids.get(i);
			}
			sql += ")";

            Query query = entityManager.createQuery(sql);
            query.setParameter(1, moderate?"S":"N");
            query.executeUpdate();
            
        } catch (RuntimeException re) {
            log.error("moderate all artists by ids failed", re);
            throw re;
        }
	}
	
	public void publishAllByLetters(Boolean publish, String letters) {
		log.debug("update artists with letters ["+letters+"] to flag_public: " + publish);
        try {
            Query query = entityManager.createQuery("update Artist a set a.flag_public = ? where a.letters like ?");
            query.setParameter(1, publish?"S":"N");
            query.setParameter(2, "%" + letters + "%");
            query.executeUpdate();
            
        } catch (RuntimeException re) {
            log.error("publish all artists by letters failed", re);
            throw re;
        }
    }
	
	public void publishAllByIds(Boolean publish, List<Long> ids){
		log.debug("update artists ["+ids+"] to flag_public: " + publish);
		try {
			String sql = "update Artist a set a.flag_public = ? where a.idArtist in (";
			for(int i=0; i<ids.size(); i++){
				sql += i == 0 ? "" : ",";
				sql += ids.get(i);
			}
			sql += ")";

            Query query = entityManager.createQuery(sql);
            query.setParameter(1, publish?"S":"N");
            query.executeUpdate();
            
        } catch (RuntimeException re) {
            log.error("publish all artists by ids failed", re);
            throw re;
        }
	}
}
