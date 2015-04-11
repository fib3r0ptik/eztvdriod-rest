package info.besiera.eztvdroid.rest.dao.interfaces;


import info.besiera.eztvdroid.rest.dao.domain.DeviceRegistration;
import info.besiera.eztvdroid.rest.dao.domain.Profile;

public interface IProfile {
	public void addProfile(Profile profile);
	public void deleteProfile(DeviceRegistration deviceRegistration);
	public Profile find(String deviceId);
}
