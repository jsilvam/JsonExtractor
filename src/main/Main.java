package main;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
	
	
	
	public static void main(String[] args) throws Exception {
		File jsonFile = new File("data.json");
		Finder finder = new Finder(jsonFile);
		JSONArray array= finder.getJsonHandler().getJsonArray(jsonFile);
		for(Object o: array) {
			JSONObject jObj = (JSONObject)o;
			String url = jObj.getString("url");
			finder.analise(url);
		}
		finder.close();
	}
}
