package info.besiera.eztvdroid.rest.dao.services;


import info.besiera.eztvdroid.rest.dao.domain.DeviceRegistration;
import info.besiera.eztvdroid.rest.dao.domain.Profile;

public interface IProfileService {
	public void addProfile(Profile profile);
	public void deleteProfile(DeviceRegistration deviceRegistration);
	public Profile find(String deviceId);
}
