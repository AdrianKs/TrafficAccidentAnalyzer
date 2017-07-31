package de.traffic.accident.analyzer;

import static spark.Spark.*;

import org.apache.log4j.BasicConfigurator;

public class SparkWebserver {
	
	public SparkWebserver(){
		 exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
		 staticFiles.location("/public");
		 port(4567);
		 get("/main", (req, res) -> "MAIN");
	}

//	public static void main(String[] args) {
//		BasicConfigurator.configure();
//		get("/test", (req, res) -> "<html><h3>TEST</h3></html>");
//		get("/batch", (req, res) -> BatchProcesser.getData());
//		// TODO Auto-generated method stub
//
//	}

}
