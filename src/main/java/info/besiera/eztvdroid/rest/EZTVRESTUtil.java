package info.besiera.eztvdroid.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.besiera.eztvdroid.rest.dao.services.IShowService;
import info.besiera.eztvdroid.rest.dao.services.ISubscriptionService;
import info.besiera.eztvdroid.rest.models.Episode;

@Service
public class EZTVRESTUtil {

	@Autowired
	ISubscriptionService subscriptionService;

	@Autowired
	IShowService showService;

	public List<Episode> parseEpisodes(Elements rows, final boolean update) {
		final long KB = 1024l;
		final long MB = KB * KB;
		final long GB = MB * MB;

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();

		ArrayList<Episode> episodes = new ArrayList<Episode>();
		
		for(Element row:rows){
			Elements tds = row.select("td");

			try{
				Elements anchors = tds.get(0).select("a");
				String title = "";
				String showId = "";
				if (anchors.size() > 0) {
					String href = anchors.get(0).attr("href");
					String[] parts = href.split("/");
					showId = parts[2];
				}

				anchors = tds.get(1).select("a");

				title = anchors.get(0).text().trim();

				String[] _parts = anchors.get(0).attr("title")
						.split(" ");

				String size = _parts[_parts.length - 2]
						.replace("(", "");
				String suffix = _parts[_parts.length - 1].replace(")",
						"");

				double _size = 0.0;

				if (suffix.equalsIgnoreCase("kb")) {
					_size = Double.parseDouble(size) * KB;
				}

				if (suffix.equalsIgnoreCase("mb")) {
					_size = Double.parseDouble(size) * MB;
				}

				if (suffix.equalsIgnoreCase("gb")) {
					_size = Double.parseDouble(size) * GB;
				}

				String[] elapseParts = tds.get(3).text().split(" ");
				if (tds.get(3).text().contains("week")) {
					elapseParts[0] = "7d";
					elapseParts[1] = "0h";
				}

				DateTime dt = new DateTime(DateTimeZone.UTC);
				for (String _elapse : elapseParts) {
					if (_elapse.contains("m")) {
						dt = dt.minusMinutes(Integer.parseInt(_elapse
								.replace("m", "")));
					}

					if (_elapse.contains("h")) {
						dt = dt.minusHours(Integer.parseInt(_elapse
								.replace("h", "")));
					}

					if (_elapse.contains("d")) {
						dt = dt.minusDays(Integer.parseInt(_elapse
								.replace("d", "")));
					}

				}

				//DateTimeFormatter fmt = DateTimeFormat
				//		.forPattern("E, dd MMM y HH:mm:ss Z");

				Episode episode = new Episode();
				episode.setShow_id(showId);
				episode.setTitle(title);
				episode.setSize(_size);

				ArrayList<String> links = new ArrayList<>();

				Elements linkAnchors = tds.get(2).select("a");
				for (Element linkAnchor : linkAnchors) {
					String _href = linkAnchor.attr("href");
					if ((_href.contains("magnet") || (_href.contains(".torrent") && _href.contains("http:")))){
						links.add(_href.substring(0, 2).equalsIgnoreCase("//")?"http:"+_href:_href);
					}
				}

				episode.setPubdate(dt.getMillis());
				episode.setLinks(links);

				if (!showId.equalsIgnoreCase("187")) {
					if (update) {
						info.besiera.eztvdroid.rest.dao.domain.Show _show = showService
								.find(Integer.parseInt(showId));
						if (_show != null) {
							Pattern p = Pattern
									.compile("(.*?)S?(\\d{1,2})E?(\\d{2})(.*)");
							Matcher m = p.matcher(title);

							if (m.matches()) {
								if (m.group(2) != null)
									_show.setSeason(Integer.parseInt(m
											.group(2)));
								if (m.group(3) != null)
									_show.setEpisode(Integer.parseInt(m
											.group(3)));
							}
							// update this show info.
							/*
							if (episode.getLinks().size() > 0) {
								for (String link : episode.getLinks()) {
									if (!link.contains("magnet")) {
										if (link.contains("720p")
												|| link.contains("HDTV")) {
											if (_show.getHdlink() == null)
												_show.setHdlink(link);
										} else {
											if (_show.getLink() == null)
												_show.setLink(link);
										}
									}
								}

							}*/
							
							if(links.size() > 0){
								for(String link: links){
									if (!link.contains("magnet")) {
										if (link.contains("720p") || link.toLowerCase().contains("HDTV")) {
											_show.setHdlink(link);
										} else {
											_show.setLink(link);
										}
									}
								}
							}
							
							

							showService.saveShow(_show);
							
							System.out.println(dateFormat.format(cal.getTime()) + ": Updated Show Id: "
									+ showId + " (" + _show.getTitle() + ")");
						}
					}
				}

				episodes.add(episode);
			}catch(Exception e){
				return null;
			}
		}
		
		
		return episodes;
	}
}
