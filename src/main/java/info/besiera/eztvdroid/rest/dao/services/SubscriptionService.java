package info.besiera.eztvdroid.rest.dao.services;


import info.besiera.eztvdroid.rest.dao.domain.Subscription;
import info.besiera.eztvdroid.rest.dao.interfaces.ISubscription;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService implements ISubscriptionService{
	@Autowired
	private ISubscription subscriptionDAO;
	 
	@Override
	@Transactional 
	public void subscribe(Subscription subscription) {
		subscriptionDAO.subscribe(subscription);
	}
	
	@Override
	@Transactional 
	public void unsubscribe(Integer id) {
		subscriptionDAO.unsubscribe(id);
		
	}
	
	@Override
	@Transactional 
	public List<Subscription> listSubscriptions(String deviceId) {
		return subscriptionDAO.listSubscriptions(deviceId);
	}
	
	@Override
	@Transactional 
	public int getCount(Integer id) {
		return subscriptionDAO.getCount(id);
	}

	@Override
	@Transactional 
	public Subscription getSubscription(String deviceId, int showId) {
		return subscriptionDAO.getSubscription(deviceId, showId);
	}
	
	@Transactional 
	@Override
	public List<Subscription> listAll() {
		return subscriptionDAO.listAll(); 
	}

}
