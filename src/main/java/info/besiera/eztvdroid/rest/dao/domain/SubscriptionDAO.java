package info.besiera.eztvdroid.rest.dao.domain;

import info.besiera.eztvdroid.rest.dao.interfaces.ISubscription;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDAO implements ISubscription {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void subscribe(Subscription subscription) {
		sessionFactory.getCurrentSession().saveOrUpdate(subscription);
	}

	@Override
	public void unsubscribe(Integer id) {
		Subscription subscription = (Subscription) sessionFactory.getCurrentSession().load(
				Subscription.class, id);
        if (null != subscription) {
            sessionFactory.getCurrentSession().delete(subscription);
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Subscription> listSubscriptions(String deviceId) {
		Query q = sessionFactory.getCurrentSession().createQuery("from Subscription s where dev_id=:deviceid");
		q.setParameter("deviceid", deviceId);
		return q.list();
	}


	@Override
	public int getCount(Integer id) {
		Query q = sessionFactory.getCurrentSession().createQuery("select count(*) from Subscription s where show_id=:showid");
		q.setParameter("showid", id);
		return ((Long)q.uniqueResult()).intValue();
	}

	
	@Override
	public Subscription getSubscription(String deviceId, int showId) {
		Query q = sessionFactory.getCurrentSession().createQuery("from Subscription where dev_id=:deviceid AND show_id=:showid");
		q.setParameter("deviceid", deviceId);
		q.setParameter("showid", showId);
		q.setMaxResults(1);
		return (Subscription)q.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Subscription> listAll() {
		String sql = "select s from Subscription s Inner Join s.deviceRegistration";
		Query q = sessionFactory.getCurrentSession().createQuery(sql);
		return q.list();
	}
	
	
}
