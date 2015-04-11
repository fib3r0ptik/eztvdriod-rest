package info.besiera.eztvdroid.rest.controllers;



import info.besiera.eztvdroid.rest.Util;
import info.besiera.eztvdroid.rest.dao.domain.Subscription;
import info.besiera.eztvdroid.rest.dao.services.IShowService;
import info.besiera.eztvdroid.rest.dao.services.ISubscriptionService;
import info.besiera.eztvdroid.rest.models.Data;
import info.besiera.eztvdroid.rest.models.Episode;
import info.besiera.eztvdroid.rest.models.GCMRequestTemplate;
import info.besiera.eztvdroid.rest.models.Message;
import info.besiera.eztvdroid.rest.models.Show;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.management.timer.Timer;
import javax.servlet.ServletContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class CacheController {

	@Autowired
	Util util;
	
	@Autowired
	ServletContext servletcontext;

	@Autowired
	ISubscriptionService subscriptionService;

	@Autowired
	IShowService showService;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	final String baseurl = "https://eztv.ch/";
//	final String baseurl = "http://10.0.0.6:8080/";
	
	@Scheduled(fixedRate = 3600000)
	public void checkSearchCache(){
		try{
		File folder = new File(servletcontext.getRealPath("/") + "cache/searches/");
		File[] listings = folder.listFiles();
		for(File file: listings){
			if(file.isFile() && !file.isHidden()){
				long elapseMillis = System.currentTimeMillis() - file.lastModified();
				//System.out.println("elapsedmillis: " + elapseMillis);
				final long SEC = 1000L;
				final long MIN = 60L * SEC;
				final long HOUR = MIN * 60;
				//delete if more than an hour
				if(elapseMillis > HOUR){
					file.delete();
				}
			}
		}
		}catch(Exception e){
			System.out.println("checking search cache: " + e.getMessage());
		}
	}
	
	// every 1 day
	//@Transactional
	@Scheduled(fixedRate = 86400000)
	public void cacheShows() {
		final String posterRootURI = "http://besiera.info/tvimg/";
		System.out.println("Caching Shows...");
		File folder = new File(servletcontext.getRealPath("/")
				+ "cache/");
		if (!folder.exists()) {
			folder.mkdirs();
		}

		
		File logFolder = new File(servletcontext.getRealPath("/") + "logs/");
		if (!logFolder.exists()) {
			logFolder.mkdirs();
		}
		
		FileHandler fh = null;
		Logger logger = null;
		String logFilename = new SimpleDateFormat("yyyy_MM_dd-hh_mm_ss'.txt'").format(new Date());
		try{
			fh = new FileHandler(logFolder.getCanonicalPath() + "/" + logFilename);
			logger = Logger.getLogger(CacheController.class.getName());
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			
		}catch(Exception e){

		}finally{
			fh.close();
		}
		

		try {
			Document doc = Jsoup
					.connect(baseurl + "showlist")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36")
					.referrer(
							"https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&ved=0CCcQFjAA&url=http%3A%2F%2Feztv.ch%2F&ei="
									+ UUID.randomUUID().toString()
									+ "&bvm=bv.60983673,d.cGU").get();
			File file = new File(folder.getAbsolutePath() + "/shows.json");

			Elements rows = doc.select("tr[name=hover]");


			Timer t = new Timer();
			long startExec = System.currentTimeMillis();
			t.start();
			List<Show> shows = rows
					.stream()
					.map(row -> {
						Elements tds = row.select("td");
						Element anchor = tds.get(1).select("a").get(0);
						String href = anchor.attr("href");
						String[] parts = href.split("/");
						String showId = parts[2];
						String title = anchor.text().trim();
						String status = tds.get(2).text().trim();

						Show show = new Show();
						show.setShowId(Integer.parseInt(showId));
						show.setStatus(status);
						show.setTitle(title);
						show.setPosterUri(posterRootURI + showId + ".jpg");
						System.out.print("saving " + show.getTitle() + "["+show.getShowId()+"]");
						info.besiera.eztvdroid.rest.dao.domain.Show _show = showService.find(show.getShowId());
						if(_show == null) {
							_show = new info.besiera.eztvdroid.rest.dao.domain.Show();
							_show.setShowId(show.getShowId());
						}
						
						show.setSubscriberCount(subscriptionService.getCount(show.getShowId()));
						
						_show.setTitle(show.getTitle());
						_show.setStatus(show.getStatus());
						showService.saveShow(_show);
						System.out.print("...saved.");
						System.out.println("");

						return show;
						
					}).parallel().collect(Collectors.toList());

			t.stop();
			long endExec = System.currentTimeMillis();
			if(logger != null){
				logger.info("caching shows took " + ((endExec - startExec)/1000) + " secs.");
			}
			
			System.out.println("It took " + (endExec - startExec) + " ms");
			
			
			Gson gson = new Gson();
			FileWriter fw = new FileWriter(file.getAbsolutePath(), false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(gson.toJson(shows));
			bw.flush();
			bw.close();
			fw.flush();
			fw.close();

			System.out.println("Shows Cache written on "
					+ file.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("caching shows: " + e.getMessage());
		}
	}
	
	
	@Scheduled(fixedRate = 7200000) //every 2 hours
	//@Transactional
	public void cacheLatest() {
		
		StringBuilder sqlEmptyLinks = new StringBuilder("Update Show ");
		sqlEmptyLinks.append("Set link = null, hdlink = null ");

		Session s = sessionFactory.openSession();
		
		Query q = s.createQuery(sqlEmptyLinks.toString());
		
		int affectedCount = q.executeUpdate();
		
		s.flush();
		s.close();
		
		System.out.println(affectedCount);
		
		for (int page = 4; page >= 0; page--) {
			try {
				File folder = new File(servletcontext.getRealPath("/")
						+ "cache/");
				if (!folder.exists()) {
					folder.mkdirs();
				}

				Document doc = null;
				doc = Jsoup
						.connect(baseurl + "page_" + page)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36")
						.referrer(
								"https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&ved=0CCcQFjAA&url=http%3A%2F%2Feztv.ch%2F&ei="
										+ UUID.randomUUID().toString()
										+ "&bvm=bv.60983673,d.cGU")
						.timeout(15000).get();


				Elements rows = doc
						.select("tr[name=hover]tr.forum_header_border");

				List<Episode> episodes = util.parseEpisodes(rows,(page == 0 || page == 1));
				
				File file = new File(folder.getAbsolutePath() + "/" + page
						+ ".json");
				if(!file.exists()) file.createNewFile();
				
				Gson gson = new Gson();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath(), false));
				try{
					bw.write(gson.toJson(episodes));
				}finally{
					//bw.flush();
					bw.close();
				}

				
				Thread.sleep(2000);

			} catch (IOException | InterruptedException e) {
				System.out.println("caching page " + page + ": " + e.getMessage());
				continue;
			}

		}
		
		//GCMPush();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void GCMPush() {
		
		Timer t = new Timer();
		long startExec = System.currentTimeMillis();
		t.start();
		System.out.println("pushing gcm message");

		StringBuilder sql = new StringBuilder("Select distinct sub from Subscription sub ");
		sql.append("Inner Join sub.deviceRegistration dr ");
		sql.append("Inner Join sub.show show ");
		sql.append(" Where sub.show.showId NOT IN (");
		sql.append("Select v from Viewed v ");
		sql.append("Inner Join v.show vshow ");
		sql.append(" Where v.show.showId = sub.show.showId AND v.episode >=vshow.episode AND ");
		sql.append("v.season <= vshow.season AND v.deviceRegistration.deviceId = sub.deviceRegistration.deviceId) ");
		sql.append(" AND lower(sub.show.status) like '%airing%' ");
		//sql.append(" AND dr.deviceId=:devid");

		
		System.out.println(sql.toString());
		
		Session _s = sessionFactory.openSession();
		
		Query q = _s.createQuery(sql.toString());
		//q.setParameter("devid", "740a27fe1fe50608");
		List<Subscription> subscriptions = q.list();
		
		_s.flush();
		_s.close();

		System.out.println("There are " + subscriptions.size() + " found.");
		
		List<String> regIds = subscriptions.stream().map(s -> {
			return s.getDeviceRegistration().getRegistrationId();
		}).distinct().parallel().collect(Collectors.toList());

		System.out.println("There are " + regIds.size() + " registration id's found.");
		
		List<String> _subs = new ArrayList<String>(0);
		_subs = subscriptions.stream().map(s -> {
			if (s == null) {
				return null;
			} else {
				info.besiera.eztvdroid.rest.dao.domain.Show show = s.getShow();
				if (show == null) {
					return null;
				}
				return s.getShow().getShowId() + "";
			}
		}).distinct().parallel().collect(Collectors.toList());

		_subs.removeAll(Collections.singleton(null));

		if (_subs.size() > 0) {

			final MediaType JSON = MediaType
					.parse("application/json; charset=utf-8");

			GCMRequestTemplate template = new GCMRequestTemplate();
			template.setRegistration_ids(regIds);

			Data data = new Data();
			Message message = new Message();
			message.setType("new_episode");
			message.setData(_subs);
			data.setMessage(message);
			template.setData(data);

			String json = new Gson().toJson(template, GCMRequestTemplate.class);
			System.out.println("JSON payload for GCM is: " + json);

			RequestBody body = RequestBody.create(JSON, json);
			Request request = new Request.Builder()
					.url("https://android.googleapis.com/gcm/send")
					.addHeader("Authorization",
							"key=AIzaSyDyIoNp18do2Sxq67YFYpYWkORyYF_GFW0")
					.post(body).build();

			final OkHttpClient client = new OkHttpClient();
			try {
				Response response = client.newCall(request).execute();
				System.out.println("response is: " + response.body().string());
			} catch (IOException e) {
				System.out.println("error push call: " + e.getMessage());
			}
			
			long endExec = System.currentTimeMillis();
			System.out.println("It took " + ((endExec - startExec) / 1000)
					+ " seconds for GCMPUSH");
			
		}else{
			System.out.println("No records founds for PUSH");
		}


	}
	



}