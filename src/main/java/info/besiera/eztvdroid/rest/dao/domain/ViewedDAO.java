package info.besiera.eztvdroid.rest.dao.domain;


import info.besiera.eztvdroid.rest.dao.interfaces.IViewed;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ViewedDAO implements IViewed {

	@Autowired
	private SessionFactory sessionFactory;


	@Override
	public void addViewed(Viewed viewed) {
		sessionFactory.getCurrentSession().save(viewed);
	}

	@Override
	public void deleteViewed(Integer id) {
		Viewed viewed = (Viewed) sessionFactory.getCurrentSession().load(
				Viewed.class, id);
        if (null != viewed) {
            sessionFactory.getCurrentSession().delete(viewed);
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Viewed> listViewed(String deviceId) {
		Query q = sessionFactory.getCurrentSession().createQuery("from Viewed where dev_id=:devid");
		q.setParameter("devid", deviceId);
		
		
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Viewed> listViewedByIds(String deviceId, int[] ids) {
		Query q = sessionFactory.getCurrentSession().createQuery("from Viewed where dev_id=:devid AND show_id IN(:ids)");
		q.setParameter("devid", deviceId);
		q.setParameter("ids", ids);
		return q.list();
	}

	@Override
	public Viewed find(String deviceId, int showId, int season, int episode) {
		Query q = sessionFactory.getCurrentSession()
				.createQuery("from Viewed where dev_id=:dev_id AND show_id=:show_id AND season=:season AND episode=:episode");
		q.setParameter("dev_id", deviceId);
		q.setParameter("show_id", showId);
		q.setParameter("season", season);
		q.setParameter("episode", episode);
		q.setMaxResults(1);
		List<Viewed> l = q.list();
		if(l != null){
			if(l.size() > 0){
				return l.get(0);
			}
		}
		
		return null;
	}

}
