package info.besiera.eztvdroid.rest.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="viewed")
public class Viewed {
    @Id
    @Column(name="ID")
    @GeneratedValue
    private int id;
    
	//@JsonIgnore
    @Column(name="season")
    private int season;
    
	//@JsonIgnore
    @Column(name="episode")
    private int episode;
    

	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="dev_id")
    private DeviceRegistration deviceRegistration;
    
	//@JsonIgnore
    @ManyToOne
    @JoinColumn(name="show_id")
    private Show show;
	
    public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEpisode() {
		return episode;
	}

	public void setEpisode(int episode) {
		this.episode = episode;
	}

	public DeviceRegistration getDeviceRegistration() {
		return deviceRegistration;
	}

	public void setDeviceRegistration(DeviceRegistration deviceRegistration) {
		this.deviceRegistration = deviceRegistration;
	}
	
}
