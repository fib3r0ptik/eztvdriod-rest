package info.besiera.eztvdroid.rest.dao.domain;

import info.besiera.eztvdroid.rest.dao.interfaces.IProfile;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileDAO implements IProfile {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addProfile(Profile profile) {
		sessionFactory.getCurrentSession().saveOrUpdate(profile);
	}

	@Override
	public void deleteProfile(DeviceRegistration deviceRegistration) {
		Profile profile = (Profile) sessionFactory.getCurrentSession().load(
				Profile.class, deviceRegistration);
        if (null != profile) {
            sessionFactory.getCurrentSession().delete(profile);
        }
	}

	@Override
	public Profile find(String deviceId) {
		Query q = sessionFactory.getCurrentSession().createQuery(" from Profile where dev_id=:deviceId");
		q.setParameter("deviceId", deviceId);
		q.setMaxResults(1);
		Profile p = (Profile)q.list().get(0);
		
		return p;
	}


}
