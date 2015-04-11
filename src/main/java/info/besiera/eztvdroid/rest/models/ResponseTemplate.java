package info.besiera.eztvdroid.rest.models;

public class ResponseTemplate {
	private boolean success;
	private String message;
	private Object data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public void setData(Object data){
		this.data = data;
	}
	
	public Object getData(){
		return this.data;
	}
	
}
