package info.besiera.eztvdroid.rest.dao.services;


import info.besiera.eztvdroid.rest.dao.domain.DeviceRegistration;
import info.besiera.eztvdroid.rest.dao.interfaces.IDeviceRegistration;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceRegistrationService implements IDeviceRegistrationService{
	@Autowired
	private IDeviceRegistration deviceRegistrationDAO;
	 
	@Transactional
	@Override
	public void registerDevice(DeviceRegistration dr) {
		deviceRegistrationDAO.registerDevice(dr);
	}

	@Transactional
	@Override
	public void deleteRegistration(String deviceId) {
		deviceRegistrationDAO.deleteRegistration(deviceId);
	}

	@Transactional
	@Override
	public List<DeviceRegistration> listRegistrations() {
		return deviceRegistrationDAO.listRegistrations();
	}

	@Transactional
	@Override
	public DeviceRegistration getRegistration(String deviceId) {
		return deviceRegistrationDAO.getRegistration(deviceId);
	}

}
