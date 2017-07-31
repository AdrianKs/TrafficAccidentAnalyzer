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
import com.sun.xml.bind.v2.schemagen.xmlschema.Any;

import de.uniluebeck.itm.util.logging.Logging;
import scala.Tuple2;

public class BatchProcesser implements Runnable {
	
	public static JavaPairRDD<String, Integer> reduceByBrand;
	public static JavaPairRDD<Integer, Integer> reduceByAge;
	public static JavaPairRDD<Integer, Integer> reduceByPassenger;
	public static String jsonByBrand;
	public static String jsonByAge;
	public static String jsonByPassenger;
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
		
		//Analyze number of accidents per brand
		JavaRDD<Row> javaRDD = df.javaRDD();
		JavaPairRDD<String, Integer> brandMap = javaRDD.mapToPair(row -> new Tuple2<>(row.getString(3), 1));
		reduceByBrand = brandMap.reduceByKey((a, b) -> a + b);
		reduceByBrand = reduceByBrand.sortByKey();
		jsonByBrand = gson.toJson(reduceByBrand.collect());
		
		//Analyze number of accidents per age of car
      	JavaPairRDD<Integer, Integer> yearMap = javaRDD.mapToPair(row -> new Tuple2<>(row.getInt(5), 1));
      	reduceByAge = yearMap.reduceByKey((a, b) -> a + b);
      	reduceByAge = reduceByAge.sortByKey();
      	jsonByAge = gson.toJson(reduceByAge.collect());
      	
      	//Analyze number of accidents per passenger of car
      	JavaPairRDD<Integer, Integer> passengerMap = javaRDD.mapToPair(row -> new Tuple2<>(row.getInt(7), 1));
      	reduceByPassenger = passengerMap.reduceByKey((a, b) -> a + b);
      	reduceByPassenger = reduceByPassenger.sortByKey();
      	jsonByPassenger = gson.toJson(reduceByPassenger.collect());
        
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
	
	public static String getJsonByBrand(){
		if(jsonByBrand != null) {
			return jsonByBrand;
		}
		else {
			return "Seems to be Null";
		}	
	}
	
	
}
