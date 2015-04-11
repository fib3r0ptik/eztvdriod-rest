package info.besiera.eztvdroid.rest.dao.services;


import info.besiera.eztvdroid.rest.dao.domain.Viewed;

import java.util.List;

public interface IViewedService {
	public void addViewed(Viewed viewed);
	public void deleteViewed(Integer id);
	public List<Viewed> listViewed(String deviceId);
	public List<Viewed> listViewedByIds(String deviceId, int[] ids);
	public Viewed find(String deviceId, int showId, int season, int episode);
}
