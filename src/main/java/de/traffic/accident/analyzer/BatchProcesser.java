package de.traffic.accident.analyzer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sun.xml.bind.v2.schemagen.xmlschema.Any;

import de.uniluebeck.itm.util.logging.Logging;
import scala.Tuple2;

public class BatchProcesser implements Runnable {
	
	public static JavaPairRDD<String, Integer> reduceByBrand;
	public static JavaPairRDD<Integer, Integer> reduceByAge;
	public static JavaPairRDD<Integer, Integer> reduceByPassenger;
	public static JsonElement jsonByBrand;
	public static JsonElement jsonByAge;
	public static JsonElement jsonByPassenger;
	private DataFrame df = null;
	private SparkConf conf = null;
	private JavaSparkContext sc = null;
	private SQLContext sqlContext = null;
	
	
	public BatchProcesser(JavaSparkContext jsContext) {
		//Logging.setLoggingDefaults();
		
		this.sc = jsContext;

		this.sqlContext = new SQLContext(sc);

		// Insert code here
		this.df = sqlContext.read()
			    .format("com.databricks.spark.csv")
			    .option("inferSchema", "true")
			    .option("header", "true")
			    .load("src/main/resources/taa.csv");
	}
	
	public void run() {
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
		
		JavaRDD<Row> javaRDD = df.javaRDD();
		//Analyze 
		JavaRDD<Accident> accidentRDD = javaRDD.map(row -> {
			return new Accident(
					 row.getInt(0), 
					 row.getDouble(1), 
					 row.getDouble(2), 
					 row.getString(3), 
					 row.getString(4), 
					 row.getInt(5), 
					 row.getString(6), 
					 row.getInt(7), 
					 row.getBoolean(8), 
					 row.getBoolean(9), 
					 row.getBoolean(10), 
					 row.getBoolean(11), 
					 row.getBoolean(12), 
					 row.getBoolean(13), 
					 row.getBoolean(14), 
					 row.getBoolean(15), 
					 row.getBoolean(16), 
					 row.getBoolean(17), 
					 row.getBoolean(18), 
					 row.getBoolean(19), 
					 row.getBoolean(20), 
					 row.getBoolean(21), 
					 row.getInt(22), 
					 row.getInt(23), 
					 row.getInt(24),
					 row.getInt(25),
					 row.getInt(26));
		});
		//accidentRDD.foreach(accident -> {
		//	App.analyzeAccident(accident);
		//});
		JavaPairRDD<AccidentType, Integer> accidentMap = accidentRDD.mapToPair(accident -> new Tuple2<>(App.analyzeAccident(accident), 1));
		JavaPairRDD<AccidentType, Integer>reduceByAccidentType = accidentMap.reduceByKey((a, b) -> a + b);
		reduceByAccidentType = reduceByAccidentType.sortByKey();
		JsonElement jsonByAccidentType = gson.toJsonTree(reduceByAccidentType.collect());
		Database.setNumbOfDiffAccidentType(jsonByAccidentType);
		System.out.println(jsonByAccidentType);
		
		//Analyze number of accidents per brand
		JavaPairRDD<String, Integer> brandMap = javaRDD.mapToPair(row -> new Tuple2<>(row.getString(3), 1));
		reduceByBrand = brandMap.reduceByKey((a, b) -> a + b);
		reduceByBrand = reduceByBrand.sortByKey();
		jsonByBrand = gson.toJsonTree(reduceByBrand.collect());
		Database.setNumbAccidentsToBrand(jsonByBrand);
		
		//Analyze number of accidents per age of car
      	JavaPairRDD<Integer, Integer> yearMap = javaRDD.mapToPair(row -> new Tuple2<>(row.getInt(5), 1));
      	reduceByAge = yearMap.reduceByKey((a, b) -> a + b);
      	reduceByAge = reduceByAge.sortByKey();
      	jsonByAge = gson.toJsonTree(reduceByAge.collect());
      	Database.setNumbAccidentsToYearOfCar(jsonByAge);
      	
      	//Analyze number of accidents per passenger of car
      	JavaPairRDD<Integer, Integer> passengerMap = javaRDD.mapToPair(row -> new Tuple2<>(row.getInt(7), 1));
      	reduceByPassenger = passengerMap.reduceByKey((a, b) -> a + b);
      	reduceByPassenger = reduceByPassenger.sortByKey();
      	jsonByPassenger = gson.toJsonTree(reduceByPassenger.collect());
      	Database.setNumbAccidentsToNumbPasseger(jsonByPassenger);
        
        System.out.println("Accidents per Brand");
        printDataString(reduceByBrand);
        System.out.println("Accidents per Age of Car");
        printDataInteger(reduceByAge);
        System.out.println("Accidents per passengers in car");
        printDataInteger(reduceByPassenger);
	}
	
	private void printDataInteger(JavaPairRDD<Integer, Integer> data) {
		for(Tuple2 t: data.collect()) {
			System.out.println(t._1 + ": " + t._2);
		}
	}
	
	private void printDataString(JavaPairRDD<String, Integer> data) {
		for(Tuple2 t: data.collect()) {
			System.out.println(t._1 + ": " + t._2);
		}
	}
	
	public static JsonElement getJsonByBrand(){
		if(jsonByBrand != null) {
			return jsonByBrand;
		}
		else {
			return null;
		}	
	}
	
	
}
