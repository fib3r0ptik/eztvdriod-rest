package info.besiera.eztvdroid.rest.models;

import java.util.List;

public class Episode {
	private String title;
	private String show_id;
	private double size;
	private long pubdate;
	private List<String> links;


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getShow_id() {
		return show_id;
	}

	public void setShow_id(String show_id) {
		this.show_id = show_id;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public long getPubdate() {
		return pubdate;
	}

	public void setPubdate(long pubdate) {
		this.pubdate = pubdate;
	}

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

}
