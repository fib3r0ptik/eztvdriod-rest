package info.besiera.eztvdroid.rest.dao.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="device_registration")
public class DeviceRegistration implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7397552606504415598L;

	@Id
	@Column(name="deviceId")
    private String deviceId;
    
	@Column(name="regId")
    private String registrationId;
	

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
}
