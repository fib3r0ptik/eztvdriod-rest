package info.besiera.eztvdroid.rest.controllers;

import info.besiera.eztvdroid.rest.EZTVRESTUtil;
import info.besiera.eztvdroid.rest.models.Episode;
import info.besiera.eztvdroid.rest.models.ResponseTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

@RequestMapping(value = "/search")
@Controller
public class SearchController {

	@Autowired
	EZTVRESTUtil util;

	@Autowired
	ServletContext servletcontext;

	@ResponseBody
	@RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
	public ResponseTemplate searchById(@PathVariable("id") int showId) {
		ResponseTemplate template = new ResponseTemplate();

		File folder = new File(servletcontext.getRealPath("/")
				+ "cache/searches/");
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File file = new File(folder.getAbsolutePath() + "/" + showId + ".json");

		List<Episode> episodes = null;
		try {
			if (!file.exists()) {
				Document doc = Jsoup
						.connect("https://eztv.ch/search/")
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
						.referrer(
								"https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&ved=0CCcQFjAA&url=http%3A%2F%2Feztv.ch%2F&ei="
										+ UUID.randomUUID().toString()
										+ "&bvm=bv.60983673,d.cGU")
						.timeout(120000).data("SearchString", showId + "").data("search","Search")
						.post();
				Elements rows = doc
						.select("tr[name=hover]tr.forum_header_border");
				episodes = util.parseEpisodes(rows, false);
				FileWriter fw = new FileWriter(file.getAbsolutePath(), false);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(new Gson().toJson(episodes));
				bw.flush();
				bw.close();
			} else {
				System.out.println("Fetching from cache");
				InputStream in = new FileInputStream(file);
				JsonReader reader = new JsonReader(new InputStreamReader(in,
						"UTF-8"));
				Type dataType = new TypeToken<List<Episode>>() {
				}.getType();
				episodes = new Gson().fromJson(reader, dataType);
			}

			System.out.println(file.getAbsolutePath());

			template.setSuccess(true);
			template.setData(episodes);

		} catch (IOException e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}

		return template;
	}

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseTemplate searchByKeyword(
			@RequestParam("keyword") String keyword) {
		ResponseTemplate template = new ResponseTemplate();

		File folder = new File(servletcontext.getRealPath("/")
				+ "cache/searches/");
		if (!folder.exists()) {
			folder.mkdirs();
		}

		List<Episode> episodes = null;
		File file = new File(folder.getAbsolutePath() + "/"
				+ keyword.toLowerCase().trim().replace(" ", "_") + ".json");

		System.out.println(folder.getAbsolutePath());
		try {
			if (!file.exists()) {
				Document doc = Jsoup
						.connect("https://eztv.ch/search/")
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
						.referrer(
								"https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&ved=0CCcQFjAA&url=http%3A%2F%2Feztv.ch%2F&ei="
										+ UUID.randomUUID().toString()
										+ "&bvm=bv.60983673,d.cGU")
						.timeout(120000).data("SearchString1", keyword).data("search","Search").post();
				Elements rows = doc
						.select("tr[name=hover]tr.forum_header_border");
				episodes = util.parseEpisodes(rows, false);
				FileWriter fw = new FileWriter(file.getAbsolutePath(), false);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(new Gson().toJson(episodes));
				bw.flush();
				bw.close();
			} else {
				System.out.println("Fetching from cache");
				InputStream in = new FileInputStream(file);
				JsonReader reader = new JsonReader(new InputStreamReader(in,
						"UTF-8"));
				Type dataType = new TypeToken<List<Episode>>() {
				}.getType();
				episodes = new Gson().fromJson(reader, dataType);
			}

			template.setSuccess(true);
			template.setData(episodes);

		} catch (IOException e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}

		return template;
	}
}
