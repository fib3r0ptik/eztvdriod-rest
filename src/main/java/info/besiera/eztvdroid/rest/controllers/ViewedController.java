package info.besiera.eztvdroid.rest.controllers;

import info.besiera.eztvdroid.rest.dao.domain.Viewed;
import info.besiera.eztvdroid.rest.dao.services.IDeviceRegistrationService;
import info.besiera.eztvdroid.rest.dao.services.IShowService;
import info.besiera.eztvdroid.rest.dao.services.IViewedService;
import info.besiera.eztvdroid.rest.models.ResponseTemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/viewed")
@Controller
public class ViewedController {

	@Autowired
	IViewedService viewedService;
	@Autowired
	IDeviceRegistrationService deviceRegService;
	@Autowired
	IShowService showService;
	
	
	@ResponseBody
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.POST)
	public ResponseTemplate markViewed(@PathVariable("deviceId") String deviceId,
			@RequestParam("show_id") int showId,
			@RequestParam("season") int season,
			@RequestParam("episode") int episode) {
		ResponseTemplate template = new ResponseTemplate();
		Viewed viewed = new Viewed();
		try{
			viewed.setDeviceRegistration(deviceRegService.getRegistration(deviceId));
			viewed.setShow(showService.find(showId));
			viewed.setSeason(season);
			viewed.setEpisode(episode);
			viewedService.addViewed(viewed);
			template.setSuccess(true);
			template.setData(viewed);
		}catch(Exception e){
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}
		
		return template;
	}

	@ResponseBody
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
	public List<Viewed> getAllViewed(@PathVariable("deviceId") String deviceId) {
		return viewedService.listViewed(deviceId);
	}

	@ResponseBody
	@RequestMapping(value = "/{deviceId}/filter", method = RequestMethod.GET)
	public ResponseTemplate getViewed(
			@PathVariable("deviceId") String deviceId,
			@RequestParam(value = "showId[]") int[] ids) {
		ResponseTemplate template = new ResponseTemplate();
		try {
			List<Viewed> viewed = viewedService.listViewedByIds(deviceId, ids);
			template.setSuccess(true);
			template.setData(viewed);
		} catch (Exception e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}

		return template;
	}

}
