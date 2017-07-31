package de.traffic.accident.analyzer;

import static spark.Spark.*;

import org.apache.log4j.BasicConfigurator;

public class SparkWebserver {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		get("/test", (req, res) -> "<html><h3>TEST</h3></html>");
		//get("/batch", (req, res) -> BatchProcesser.createData());
		// TODO Auto-generated method stub

	}

}
