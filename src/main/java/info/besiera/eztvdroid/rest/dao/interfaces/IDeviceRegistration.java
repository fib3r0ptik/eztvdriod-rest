package info.besiera.eztvdroid.rest.dao.interfaces;

import info.besiera.eztvdroid.rest.dao.domain.DeviceRegistration;

import java.util.List;

public interface IDeviceRegistration {
	public void registerDevice(DeviceRegistration dr);
	public void deleteRegistration(String deviceId);
	public List<DeviceRegistration> listRegistrations();
	public DeviceRegistration getRegistration(String deviceId);
}
