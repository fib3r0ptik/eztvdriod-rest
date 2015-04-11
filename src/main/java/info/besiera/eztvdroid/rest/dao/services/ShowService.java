package info.besiera.eztvdroid.rest.dao.services;


import info.besiera.eztvdroid.rest.dao.domain.Show;
import info.besiera.eztvdroid.rest.dao.interfaces.IShow;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowService implements IShowService{
	 @Autowired
	 private IShow showDAO;
	 
	@Transactional 
	@Override
	public void saveShow(Show show) {
		showDAO.saveShow(show);
	}

	@Transactional
	@Override
	public void deleteShow(Integer id) {
		showDAO.deleteShow(id);
	}

	@Transactional
	@Override
	public List<Show> listShows() {
		return showDAO.listShows();
	}

	@Transactional
	@Override
	public Show find(Integer id) {
		return showDAO.find(id);
	}

}
