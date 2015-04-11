package info.besiera.eztvdroid.rest.dao.domain;


import info.besiera.eztvdroid.rest.dao.interfaces.IDeviceRegistration;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceRegistrationDAO implements IDeviceRegistration {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void registerDevice(DeviceRegistration dr) {
		sessionFactory.getCurrentSession().saveOrUpdate(dr);
	}

	@Override
	public void deleteRegistration(String deviceId) {
		DeviceRegistration dr = (DeviceRegistration) sessionFactory.getCurrentSession().load(
				DeviceRegistration.class, deviceId);
        if (null != dr) {
            sessionFactory.getCurrentSession().delete(dr);
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceRegistration> listRegistrations() {
		return sessionFactory.getCurrentSession().createQuery("from DeviceRegistration").list();
	}
	
	@Override
	public DeviceRegistration getRegistration(String deviceId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DeviceRegistration.class)
			    .add(Restrictions.eq("deviceId", deviceId))
			    .setMaxResults(1);
		return (DeviceRegistration)criteria.list().get(0);
	}

}
