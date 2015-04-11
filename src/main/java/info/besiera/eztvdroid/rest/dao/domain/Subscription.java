package info.besiera.eztvdroid.rest.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="subscription")
public class Subscription {
    @Id
    @Column(name="ID")
    @GeneratedValue
    private int id;

    //@JsonIgnore
	@ManyToOne
    @JoinColumn(name="dev_id")
    private DeviceRegistration deviceRegistration;
    
    @ManyToOne
    @JoinColumn(name="show_id")
    private Show show;
    
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DeviceRegistration getDeviceRegistration() {
		return deviceRegistration;
	}

	public void setDeviceRegistration(DeviceRegistration deviceRegistration) {
		this.deviceRegistration = deviceRegistration;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}


	
}
