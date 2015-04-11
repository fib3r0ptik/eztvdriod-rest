package info.besiera.eztvdroid.rest.dao.interfaces;


import info.besiera.eztvdroid.rest.dao.domain.Subscription;

import java.util.List;

public interface ISubscription {
	public void subscribe(Subscription subscription);
	public void unsubscribe(Integer id);
	public List<Subscription> listSubscriptions(String deviceId);
	public Subscription getSubscription(String deviceId, int showId);
	public int getCount(Integer id);
	public List<Subscription> listAll();
}
