package info.besiera.eztvdroid.rest.dao.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="profile")
public class Profile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4756579163459949315L;

	/*
    @Id
	@Column(name="ID")
    @GeneratedValue
    private int id;
    */
	
    @Id
    @JsonIgnore
    @OneToOne
    @JoinColumn(name="dev_id")
    private DeviceRegistration deviceRegistration;
    
	@Column(name="name")
	private String name;
	
	@Column(name="host")
	private String host;
	
	@Column(name="port")
	private int port;
	
	@Column(name="uid")
	private String userName;
	
	@Column(name="pwd")
	private String password;
	
	@Column(name="use_auth")
	private String useAuth;
	
	@Column(name="client_type")
	private String clientType;

	/*
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	*/
	
	public DeviceRegistration getDeviceRegistration() {
		return deviceRegistration;
	}

	public void setDeviceRegistration(DeviceRegistration deviceRegistration) {
		this.deviceRegistration = deviceRegistration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUseAuth() {
		return useAuth;
	}

	public void setUseAuth(String useAuth) {
		this.useAuth = useAuth;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	
}
