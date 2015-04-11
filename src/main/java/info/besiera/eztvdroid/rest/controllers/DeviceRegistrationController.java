package info.besiera.eztvdroid.rest.controllers;

import info.besiera.eztvdroid.rest.dao.domain.DeviceRegistration;
import info.besiera.eztvdroid.rest.dao.services.IDeviceRegistrationService;
import info.besiera.eztvdroid.rest.models.ResponseTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/registrations")
@Controller
public class DeviceRegistrationController {
	@Autowired
	IDeviceRegistrationService deviceRegService;

	@ResponseBody
	@RequestMapping(value = "/{deviceid}/", method = RequestMethod.POST)
	public ResponseTemplate registerDevice(
			@PathVariable("deviceid") String deviceId,
			@RequestParam("regId") String registrationId) {

		DeviceRegistration dr = new DeviceRegistration();
		dr.setDeviceId(deviceId);
		dr.setRegistrationId(registrationId);
		ResponseTemplate response = new ResponseTemplate();
		try {
			deviceRegService.registerDevice(dr);
			response.setSuccess(true);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}

		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{deviceid}/", method = RequestMethod.DELETE)
	public ResponseTemplate unRegisterDevice(
			@PathVariable("deviceid") String deviceId) {

		ResponseTemplate response = new ResponseTemplate();
		try {
			DeviceRegistration dr  = deviceRegService.getRegistration(deviceId);
			deviceRegService.deleteRegistration(dr.getDeviceId());
			response.setSuccess(true);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}

		return response;
	}
	

}
