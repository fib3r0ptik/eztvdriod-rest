package info.besiera.eztvdroid.rest.controllers;

import info.besiera.eztvdroid.rest.dao.domain.Profile;
import info.besiera.eztvdroid.rest.dao.services.IDeviceRegistrationService;
import info.besiera.eztvdroid.rest.dao.services.IProfileService;
import info.besiera.eztvdroid.rest.models.ResponseTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/profile")
@Controller
public class ProfileController {

	@Autowired
	IProfileService profileService;

	@Autowired
	IDeviceRegistrationService deviceRegistrationService;

	@ResponseBody
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
	public ResponseTemplate getProfile(@PathVariable("deviceId") String deviceId) {
		ResponseTemplate template = new ResponseTemplate();
		try{
			Profile profile = profileService.find(deviceId);
			template.setSuccess(true);
			template.setData(profile);
		}catch(Exception e){
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}
		
		return template;
	}

	@ResponseBody
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	public ResponseTemplate createProfile(
			@PathVariable("deviceId") String deviceId,
			@RequestBody Profile profile) {
		profile.setDeviceRegistration(deviceRegistrationService
				.getRegistration(deviceId));
		ResponseTemplate template = new ResponseTemplate();
		try {
			profileService.addProfile(profile);
			template.setSuccess(true);
		} catch (Exception e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}
		return template;
	}

	@ResponseBody
	@RequestMapping(value = "/{deviceId}", method = RequestMethod.DELETE)
	public ResponseTemplate deleteProfile(
			@PathVariable("deviceId") String deviceId) {
		ResponseTemplate template = new ResponseTemplate();
		try {
			Profile profile = profileService.find(deviceId);
			if (profile != null) {
				profileService.deleteProfile(profile.getDeviceRegistration());
				template.setSuccess(true);
			} else {
				template.setSuccess(false);
				template.setMessage("Can't find entry for " + deviceId);
			}

		} catch (Exception e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}
		return template;

	}
}
