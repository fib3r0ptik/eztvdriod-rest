package info.besiera.eztvdroid.rest.controllers;

import info.besiera.eztvdroid.rest.dao.domain.Show;
import info.besiera.eztvdroid.rest.dao.domain.Subscription;
import info.besiera.eztvdroid.rest.dao.domain.Viewed;
import info.besiera.eztvdroid.rest.dao.services.IDeviceRegistrationService;
import info.besiera.eztvdroid.rest.dao.services.IShowService;
import info.besiera.eztvdroid.rest.dao.services.ISubscriptionService;
import info.besiera.eztvdroid.rest.dao.services.IViewedService;
import info.besiera.eztvdroid.rest.models.ResponseTemplate;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/subscriptions")
@Controller
public class SubscriptionController {

	@Autowired
	ISubscriptionService subscriptionService;

	@Autowired
	IDeviceRegistrationService deviceRegService;

	@Autowired
	IViewedService viewedService;
	
	@Autowired
	IShowService showService;

	
	private boolean hasNewEpisodes(String deviceId,Show show){
		if(show.getSeason() == 0) 
			return false;
		if((show.getHdlink()== null && show.getLink() == null))
			return false;
		
		Viewed v = viewedService.find(deviceId, show.getShowId(), show.getSeason(), show.getEpisode());
		return !(v instanceof Viewed);
	}
	
	@ResponseBody
	@RequestMapping(value = "/{deviceId}/check", method = RequestMethod.GET)
	public ResponseTemplate checkNewEpisodes(
			@PathVariable("deviceId") String deviceId) {
		ResponseTemplate template = new ResponseTemplate();
		try {
			List<Subscription> subscriptions = subscriptionService
					.listSubscriptions(deviceId);
			List<Subscription> filtered = subscriptions.stream()
					.filter(s -> hasNewEpisodes(deviceId,s.getShow()))
					.collect(Collectors.toList());

			template.setSuccess(true);
			template.setData(filtered);
		} catch (Exception e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}
		return template;
	}

	@ResponseBody
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
	public ResponseTemplate getSubscriptions(
			@PathVariable("deviceId") String deviceId) {
		ResponseTemplate template = new ResponseTemplate();
		try {
			List<Subscription> subscriptions = subscriptionService
					.listSubscriptions(deviceId);
			template.setSuccess(true);
			template.setData(subscriptions);
		} catch (Exception e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}
		return template;
	}

	@ResponseBody
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.POST)
	public ResponseTemplate subscribe(
			@PathVariable("deviceId") String deviceId,
			@RequestParam("showId") int showId) {

		ResponseTemplate template = new ResponseTemplate();
		try {
			Subscription subscription = new Subscription();
			subscription.setDeviceRegistration(deviceRegService
					.getRegistration(deviceId));
			subscription.setShow(showService.find(showId));
			subscriptionService.subscribe(subscription);
			template.setSuccess(true);
		} catch (Exception e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}
		return template;
	}

	@ResponseBody
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.DELETE)
	public ResponseTemplate unSubscribe(
			@PathVariable("deviceId") String deviceId,
			@RequestParam("showId") int showId) {

		ResponseTemplate template = new ResponseTemplate();
		try {
			Subscription subscription = subscriptionService.getSubscription(
					deviceId, showId);
			subscriptionService.unsubscribe(subscription.getId());
			template.setSuccess(true);
			template.setData(subscription);
		} catch (Exception e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}

		return template;
	}

	@ResponseBody
	@RequestMapping(value = "/{deviceId}/find/{showId}", method = RequestMethod.GET)
	public ResponseTemplate isSubscribed(
			@PathVariable("deviceId") String deviceId,
			@PathVariable("showId") int showId) {

		ResponseTemplate template = new ResponseTemplate();
		try {
			ResponseTemplate response = getSubscriptions(deviceId);
			@SuppressWarnings("unchecked")
			List<Subscription> subs = (List<Subscription>) response.getData();
			if (response.isSuccess()) {
				List<Subscription> filtered = subs.stream()
						.filter(s -> s.getShow().getShowId() == showId)
						.collect(Collectors.toList());
				template.setSuccess(true);
				template.setData(filtered.get(0));
			} else {
				template.setSuccess(false);
				template.setMessage(response.getMessage());
			}
		} catch (Exception e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}

		return template;
	}

}
