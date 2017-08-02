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
		 
		 get("/", (request, response) -> {
			 	JsonElement numbAccidentsToBrand = Database.getNumbAccidentsToBrand();
			 	JsonElement numbAccidentsToYearOfCar = Database.getNumbAccidentsToYearOfCar();
			 	JsonElement numbAccidentsToNumbPasseger = Database.getNumbAccidentsToNumbPasseger();
			 	JsonElement numbOfDiffAccidentType = Database.getNumbOfDiffAccidentType();
			 	String numbAccidentToVelocityToAccidentType = Database.getNumbAccidentToVelocityToAccidentType();
	            Map<String, Object> model = new HashMap<>();
	            model.put("ap", Database.getHeatMapDataAsString());
	            model.put("numbAccidentsToBrand", numbAccidentsToBrand);
	            model.put("numbAccidentsToYearOfCar", numbAccidentsToYearOfCar);
	            model.put("numbAccidentsToNumbPasseger", numbAccidentsToNumbPasseger);
	            model.put("numbOfDiffAccidentType", numbOfDiffAccidentType);
	            model.put("numbAccidentToVelocityToAccidentType", numbAccidentToVelocityToAccidentType);
	            
	            return new ModelAndView(model, "public/index.vm");
	        }, new VelocityTemplateEngine());
		 
		 post("/refreshTrafficData", (request, response) -> {
			 response.type("application/json");
			 return Database.getHeatMapDataAsString();
		 });
		 
		
	}

}
