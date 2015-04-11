package info.besiera.eztvdroid.rest.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shows")
public class Show {
	
    @Id
	@Column(name="show_id",updatable=false)
    private int showId;
	
	public int getShowId() {
		return showId;
	}

	public void setShowId(int showId) {
		this.showId = showId;
	}

	@Column(name="title",updatable=false)
    private String title;
	
	@Column(name="latest_season")
    private int season;
	
	@Column(name="latest_episode")
    private int episode;
	
	@Column(name="latest_link")
    private String link;
	
	@Column(name="status")
    private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getEpisode() {
		return episode;
	}

	public void setEpisode(int episode) {
		this.episode = episode;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getHdlink() {
		return hdlink;
	}

	public void setHdlink(String hdlink) {
		this.hdlink = hdlink;
	}

	@Column(name="latest_hdlink")
    private String hdlink;
	
}
