package de.traffic.accident.analyzer;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;


import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class SparkWebserver {

	
	public SparkWebserver(){
		 exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
		 staticFiles.location("/public/css");
		 port(4567);
		
		 
		 get("/index", (request, response) -> {
	            Map<String, Object> model = new HashMap<>();
	            model.put("name", "World");
	            
	            return new ModelAndView(model, "public/index.vm");
	        }, new VelocityTemplateEngine());

	}
}



