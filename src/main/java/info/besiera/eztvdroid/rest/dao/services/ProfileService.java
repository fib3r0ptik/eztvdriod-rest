package info.besiera.eztvdroid.rest.dao.services;


import info.besiera.eztvdroid.rest.dao.domain.DeviceRegistration;
import info.besiera.eztvdroid.rest.dao.domain.Profile;
import info.besiera.eztvdroid.rest.dao.interfaces.IProfile;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProfileService implements IProfileService{
	 @Autowired
	 private IProfile profileDAO;

	@Transactional
	@Override
	public void addProfile(Profile profile) {
		profileDAO.addProfile(profile);
	}

	@Transactional
	@Override
	public void deleteProfile(DeviceRegistration deviceRegistration) {
		profileDAO.deleteProfile(deviceRegistration);
	}

	@Transactional
	@Override
	public Profile find(String deviceId) {
		return profileDAO.find(deviceId);
	}

	





}
