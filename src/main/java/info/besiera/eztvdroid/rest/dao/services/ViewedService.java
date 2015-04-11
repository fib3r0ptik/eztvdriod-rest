package info.besiera.eztvdroid.rest.dao.services;


import info.besiera.eztvdroid.rest.dao.domain.Viewed;
import info.besiera.eztvdroid.rest.dao.interfaces.IViewed;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewedService implements IViewedService{
	 @Autowired
	 private IViewed viewedDAO;
	 

	@Transactional
	@Override
	public void addViewed(Viewed viewed) {
		viewedDAO.addViewed(viewed);
		
	}

	@Transactional
	@Override
	public void deleteViewed(Integer id) {
		viewedDAO.deleteViewed(id);
		
	}

	@Transactional
	@Override
	public List<Viewed> listViewed(String deviceId) {
		return viewedDAO.listViewed(deviceId);
	}
	
	@Transactional
	@Override
	public List<Viewed> listViewedByIds(String deviceId, int[] ids) {
		return viewedDAO.listViewedByIds(deviceId, ids);
	}

	@Transactional
	@Override
	public Viewed find(String deviceId, int showId, int season, int episode) {
		return viewedDAO.find(deviceId, showId, season, episode);
	}

}
