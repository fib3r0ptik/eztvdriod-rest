package info.besiera.eztvdroid.rest.models;

public class Show {
	private int showId;
	private String title;
	private String status;
	private int subscriberCount;
	private String posterUri;
	
	public String getPosterUri() {
		return posterUri;
	}
	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}
	public int getShowId() {
		return showId;
	}
	public void setShowId(int showId) {
		this.showId = showId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getSubscriberCount() {
		return subscriberCount;
	}
	public void setSubscriberCount(int subscriberCount) {
		this.subscriberCount = subscriberCount;
	}


}
