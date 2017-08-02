package de.traffic.accident.analyzer;

import static spark.Spark.exception;
import static spark.Spark.*;
import static spark.Spark.staticFiles;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonElement;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class SparkWebserver {

	public SparkWebserver() {
		staticFiles.location("/public");
		exception(Exception.class, (e, req, res) -> e.printStackTrace());

		get("/s2", (request, response) -> {
		 	//JsonElement numbAccidentsToBrand = Database.getNumbAccidentsToBrand();
            Map<String, Object> model = new HashMap<>();
            model.put("test", Database.getHeatMapDataAsString());
            
            
            return new ModelAndView(model, "public/index_test.vm");
        }, new VelocityTemplateEngine());
		
		get("/s3", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ap", Database.getHeatMapDataAsString());
            
            
            return new ModelAndView(model, "public/index.vm");
        }, new VelocityTemplateEngine());
		
		
		post("/state", (req, res) -> {
			res.type("application/json");
			Map<String, Object> model = new HashMap<>();
			model.put("values", Database.getWindowData());
			return new ModelAndView(model, "/public/index_test.vm");
		}, new VelocityTemplateEngine());
		 
		 get("/index", (request, response) -> {
			 	JsonElement numbAccidentsToBrand = Database.getNumbAccidentsToBrand();
			 	JsonElement numbAccidentsToYearOfCar = Database.getNumbAccidentsToYearOfCar();
			 	JsonElement numbAccidentsToNumbPasseger = Database.getNumbAccidentsToNumbPasseger();
			 	JsonElement numbOfDiffAccidentType = Database.getNumbOfDiffAccidentType();
	            Map<String, Object> model = new HashMap<>();
	            model.put("numbAccidentsToBrand", numbAccidentsToBrand);
	            model.put("numbAccidentsToYearOfCar", numbAccidentsToYearOfCar);
	            model.put("numbAccidentsToNumbPasseger", numbAccidentsToNumbPasseger);
	            model.put("numbOfDiffAccidentType", numbOfDiffAccidentType);
	            
	            return new ModelAndView(model, "public/index.vm");
	        }, new VelocityTemplateEngine());
		
	}

}
