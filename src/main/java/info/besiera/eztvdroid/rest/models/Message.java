package info.besiera.eztvdroid.rest.models;

import java.util.List;

public class Message {
	private String type;
	private List<String> content;
	
	public Message(){}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getData() {
		return content;
	}

	public void setData(List<String> content) {
		this.content = content;
	}
	
}
