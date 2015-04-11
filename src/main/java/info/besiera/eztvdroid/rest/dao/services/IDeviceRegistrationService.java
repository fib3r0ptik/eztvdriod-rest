package info.besiera.eztvdroid.rest.dao.services;


import info.besiera.eztvdroid.rest.dao.domain.DeviceRegistration;

import java.util.List;

public interface IDeviceRegistrationService {
	public void registerDevice(DeviceRegistration dr);
	public void deleteRegistration(String deviceId);
	public List<DeviceRegistration> listRegistrations();
	public DeviceRegistration getRegistration(String deviceId);
}
