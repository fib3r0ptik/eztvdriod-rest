package info.besiera.eztvdroid.rest.controllers;

import info.besiera.eztvdroid.rest.models.Episode;
import info.besiera.eztvdroid.rest.models.ResponseTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@Controller
public class LatestController {

	@Autowired
	ServletContext servletcontext;

	@ResponseBody
	@RequestMapping(value = "/latest/{page}", method = RequestMethod.GET)
	public ResponseTemplate getLatest(@PathVariable("page") int page, final HttpServletResponse response) {
		response.addHeader("Cache-Control", "max-age=1800");
		if (page < 0) {
			page = 0;
		}

		ResponseTemplate template= new ResponseTemplate();
		
		try {
			File folder = new File(servletcontext.getRealPath("/")
					+ "cache/");
			File file = new File(folder.getAbsolutePath() + "/" + page
					+ ".json");
			InputStream in = new FileInputStream(file);
			JsonReader reader = new JsonReader(new InputStreamReader(in,
					"UTF-8"));
			Gson gson = new Gson();
			List<Episode> episodes = new ArrayList<>();
			reader.beginArray();
			while (reader.hasNext()) {
				Episode episode = gson.fromJson(reader, Episode.class);
				episodes.add(episode);
			}
			reader.endArray();
			reader.close();
			template.setSuccess(true);
			template.setData(episodes);
		} catch (IOException e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}
		
		return template;
		
	}
	
	
	
	
		
}
