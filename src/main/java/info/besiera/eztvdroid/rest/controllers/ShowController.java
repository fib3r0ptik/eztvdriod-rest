package info.besiera.eztvdroid.rest.controllers;

import info.besiera.eztvdroid.rest.dao.services.IShowService;
import info.besiera.eztvdroid.rest.dao.services.ISubscriptionService;
import info.besiera.eztvdroid.rest.models.ResponseTemplate;
import info.besiera.eztvdroid.rest.models.Show;

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

@RequestMapping(value = "/shows")
@Controller
public class ShowController {
	final String posterRootURI = "http://hamaksoftware.com/myeztv/tvimg/";

	@Autowired
	ServletContext servletcontext;

	@Autowired
	ISubscriptionService subscriptionService;

	@Autowired
	IShowService showService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public ResponseTemplate getShows(final HttpServletResponse response) {
		response.addHeader("Cache-Control", "max-age=86400");
		ResponseTemplate template = new ResponseTemplate();

		try {
			File folder = new File(servletcontext.getRealPath("/") + "cache/");
			File file = new File(folder.getAbsolutePath() + "/shows.json");
			InputStream in = new FileInputStream(file);
			JsonReader reader = new JsonReader(new InputStreamReader(in,
					"UTF-8"));
			Gson gson = new Gson();
			List<Show> shows = new ArrayList<>();
			reader.beginArray();
			while (reader.hasNext()) {
				Show show = gson.fromJson(reader, Show.class);
				shows.add(show);
			}
			reader.endArray();
			reader.close();

			template.setSuccess(true);
			template.setData(shows);
		} catch (IOException e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}

		return template;
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseTemplate getShow(@PathVariable("id") int id) {
		ResponseTemplate template = new ResponseTemplate();
		try {
			info.besiera.eztvdroid.rest.dao.domain.Show show = showService.find(id);
			template.setSuccess(true);
			template.setData(show);
		} catch (Exception e) {
			template.setSuccess(false);
			template.setMessage(e.getMessage());
		}
		
		return template;
	}

}
