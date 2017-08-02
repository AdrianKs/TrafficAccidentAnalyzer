package de.traffic.accident.analyzer;

import java.util.ArrayList;

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
import scala.Tuple2;
import scala.Tuple3;

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
	
	private static ArrayList<String> clusterData = new ArrayList<>();
	private static ArrayList<JavaRDD<Tuple3<String, AccidentType, Integer>>> clusterRDD = new ArrayList<>();
	
	
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
		JavaPairRDD<AccidentType, Integer> accidentMap = accidentRDD.mapToPair(accident -> new Tuple2<>(App.analyzeAccident(accident), 1));
		JavaPairRDD<AccidentType, Integer>reduceByAccidentType = accidentMap.reduceByKey((a, b) -> a + b);
		reduceByAccidentType = reduceByAccidentType.sortByKey();
		JsonElement jsonByAccidentType = gson.toJsonTree(reduceByAccidentType.collect());
		Database.setNumbOfDiffAccidentType(jsonByAccidentType);
		
		
		//Analyze number of accidents per velocity (v10, v5)
		JavaPairRDD<String, Integer> velocityMap = javaRDD.mapToPair(row -> new Tuple2<>(calculateVelocityCluster(row.getInt(23), row.getInt(22)), 1));
		JavaPairRDD<String, Integer>reduceByVelocity = velocityMap.reduceByKey((a, b) -> a + b);
		reduceByVelocity = reduceByVelocity.sortByKey();
		JsonElement jsonByVelocity = gson.toJsonTree(reduceByVelocity.collect());
		Database.setNumbAccidentsToVelocity(jsonByVelocity);
		
		
		//Analyze number of accidents per velocity per accidenttype
		JavaRDD<Tuple3<String, AccidentType, Integer>> velToAccTypeToNumb = accidentRDD.map(accident -> {
			return new Tuple3<>(calculateVelocityCluster(accident.getV5SecAgo(), accident.getV10SecAgo()), App.analyzeAccident(accident),  1);
		});
		JavaRDD<Tuple3<String, AccidentType, Integer>> cluster1 = velToAccTypeToNumb.filter(t -> t._1().equals("0-30"));
		JavaRDD<Tuple3<String, AccidentType, Integer>> cluster2 = velToAccTypeToNumb.filter(t -> t._1().equals("31-60"));
		JavaRDD<Tuple3<String, AccidentType, Integer>> cluster3 = velToAccTypeToNumb.filter(t -> t._1().equals("61-90"));
		JavaRDD<Tuple3<String, AccidentType, Integer>> cluster4 = velToAccTypeToNumb.filter(t -> t._1().equals("91-120"));
		JavaRDD<Tuple3<String, AccidentType, Integer>> cluster5 = velToAccTypeToNumb.filter(t -> t._1().equals("121-150"));
		JavaRDD<Tuple3<String, AccidentType, Integer>> cluster6 = velToAccTypeToNumb.filter(t -> t._1().equals("151-180"));
		JavaRDD<Tuple3<String, AccidentType, Integer>> cluster7 = velToAccTypeToNumb.filter(t -> t._1().equals("181-210"));
		JavaRDD<Tuple3<String, AccidentType, Integer>> cluster8 = velToAccTypeToNumb.filter(t -> t._1().equals(">210"));
		//JavaRDD<Tuple3<String, AccidentType, Integer>> cluster9 = velToAccTypeToNumb.filter(t -> t._1().equals(">240"));
		clusterRDD.add(cluster1);
		clusterRDD.add(cluster2);
		clusterRDD.add(cluster3);
		clusterRDD.add(cluster4);
		clusterRDD.add(cluster5);
		clusterRDD.add(cluster6);
		clusterRDD.add(cluster7);
		clusterRDD.add(cluster8);
		//clusterRDD.add(cluster9);
		
		for(JavaRDD<Tuple3<String, AccidentType, Integer>> cl : clusterRDD) {
			clusterData.add(gson.toJson(cl.mapToPair(row -> new Tuple2<>(row._2(), row._3()))
			.reduceByKey((a,b) -> a+b).collect()));
		}
		Database.setNumbAccidentToVelocityToAccidentType(createDataString(clusterData));
		
		
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
        
//        System.out.println("Accidents per Brand");
//        printDataString(reduceByBrand);
//        System.out.println("Accidents per Age of Car");
//        printDataInteger(reduceByAge);
//        System.out.println("Accidents per passengers in car");
//        printDataInteger(reduceByPassenger);
//        System.out.println("Accidents Velocity");
//        printDataString(reduceByVelocity);
	}
	
	
	private static String calculateVelocityCluster(int v5, int v10) {
		int average = (v5 + v10)/2;
		if(average <= 30) {
			return "0-30";
		}
		else if( average <= 60) {
			return "31-60";
		}
		else if(average <= 90) {
			return "61-90";
		}
		else if(average <= 120) {
			return "91-120";
		}
		else if(average <= 150) {
			return "121-150";
		}
		else if(average <= 180) {
			return "151-180";
		}
		else if(average <= 210) {
			return "181-210";
		}
//		else if(average <= 240) {
//			return "211-240";
//		}
		else {
			return ">210";
		}
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
	
	private static String createDataString(ArrayList<String> data) {
		String s = "[";
		for (int i = 0; i < data.size(); i++) {
			s += data.get(i);
			if(i<data.size()-1) {
				s+=",";
			}
		}
		s+="]";
		//System.out.println(s);
		return s;
	}
	
	
}
