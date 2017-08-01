package de.traffic.accident.analyzer;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class SparkWebserver {

	
	public SparkWebserver(){
		 exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
		 staticFiles.location("/public");
		 port(4567);
		
		 
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



