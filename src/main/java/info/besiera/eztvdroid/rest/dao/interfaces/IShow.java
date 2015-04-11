package info.besiera.eztvdroid.rest.dao.interfaces;



import info.besiera.eztvdroid.rest.dao.domain.Show;

import java.util.List;

public interface IShow {
	public void saveShow(Show show);
	public void deleteShow(Integer id);
	public List<Show> listShows();
	public Show find(Integer showId);
}
