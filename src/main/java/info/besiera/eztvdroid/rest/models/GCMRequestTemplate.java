package info.besiera.eztvdroid.rest.models;

import java.util.List;

public class GCMRequestTemplate {
	public GCMRequestTemplate(){};
	
	private List<String> registration_ids;
	private Data data;
	private int time_to_live = 108;
	private boolean delay_while_idle = true;
	
	public List<String> getRegistration_ids() {
		return registration_ids;
	}
	public void setRegistration_ids(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public boolean isDelay_while_idle() {
		return delay_while_idle;
	}
	public void setDelay_while_idle(boolean delay_while_idle) {
		this.delay_while_idle = delay_while_idle;
	}
	public int getTime_to_live() {
		return time_to_live;
	}
	public void setTime_to_live(int time_to_live) {
		this.time_to_live = time_to_live;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	
	
}
