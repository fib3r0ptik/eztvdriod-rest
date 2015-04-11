package info.besiera.eztvdroid.rest.dao.services;

import info.besiera.eztvdroid.rest.dao.domain.Show;

import java.util.List;

public interface IShowService {
	public void saveShow(Show show);
	public void deleteShow(Integer id);
	public List<Show> listShows();
	public Show find(Integer id);
}
