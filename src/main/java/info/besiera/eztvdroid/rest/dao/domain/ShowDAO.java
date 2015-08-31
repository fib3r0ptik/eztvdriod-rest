package info.besiera.eztvdroid.rest.dao.domain;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import info.besiera.eztvdroid.rest.dao.interfaces.IShow;

@Repository
public class ShowDAO implements IShow {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveShow(Show show) {
		//Session s = sessionFactory.openSession();
		//Transaction tx = s.beginTransaction();
		Session s = sessionFactory.getCurrentSession();
		s.saveOrUpdate(show);
		//s.flush();
        //s.clear();
        //tx.commit();
        //s.close();
	}

	@Override
	public void deleteShow(Integer id) {
        Show show = (Show) sessionFactory.getCurrentSession().load(
                Show.class, id);
        if (null != show) {
            sessionFactory.getCurrentSession().delete(show);
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Show> listShows() {
		return sessionFactory.getCurrentSession().createQuery("from Show").list();
	}

	@Override
	public Show find(Integer id) {
		return (Show) sessionFactory.getCurrentSession().get(Show.class, id);
	}

}
