package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import refdiff.core.rm2.model.refactoring.SDRefactoring;

//import org.json.;

public class JsonHandler {
	
	private Map<String,Map<String,String>> refactorings;
	
	public JsonHandler() {
		refactorings = new HashMap<String, Map<String,String>>();
	}
	
	public String getJsonString(File jsonFile) throws IOException {
		if (!jsonFile.exists())
			return null;
		FileReader fr = new FileReader(jsonFile);
		BufferedReader reader = new BufferedReader(fr);
		String s = reader.lines().collect(Collectors.joining());
		reader.close();
		return s;
	}
	
	public JSONArray getJsonArray(File jsonFile) throws IOException {
		String jsonString = getJsonString(jsonFile);
		JSONArray jArray = new JSONArray(jsonString);
		return jArray;
	}
	
	public void load(File jsonFile) throws IOException {
		JSONArray jArray = getJsonArray(jsonFile);
		for(Object o:jArray) {
			JSONObject jObj = (JSONObject)o;
			Map<String,String> refactorings = loadRefactorings(jObj.getJSONArray("refactorings"));
			this.refactorings.put(jObj.getString("url"), refactorings);
		}
	}
	
	private Map<String,String> loadRefactorings(JSONArray jArray){
		Map<String,String> commitRefactorings = new HashMap<String,String>();
		for(Object o:jArray) {
			JSONObject jObj = (JSONObject)o;
			commitRefactorings.put(jObj.getString("description"), jObj.getString("validation"));
		}
		return commitRefactorings;
	}
	
	public boolean isTruePositive(String commitUrl, SDRefactoring refactoring) {
		String refactoringDescription = refactoring.toString();
		Map<String,String> commitRefactorings = refactorings.get(commitUrl);
		if(commitRefactorings != null) {
			String validation = commitRefactorings.get(refactoringDescription);
			return validation.equals("TP");
		}
		return false;
	}
	
}
