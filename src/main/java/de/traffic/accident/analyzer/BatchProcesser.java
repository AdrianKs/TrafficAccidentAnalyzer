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

import de.uniluebeck.itm.util.logging.Logging;
import scala.Tuple2;

public class BatchProcesser {
	
	public static JavaPairRDD<String, Integer> reduceByBrand;
	public static JavaPairRDD<Integer, Integer> reduceByYear;
	public static String JsonByBrand;
	
	
	public BatchProcesser() {
		createData();
	}
	
	public static String getJsonByBrand(){
		if(JsonByBrand != null) {
			return JsonByBrand;
		}
		else {
			return "Seems to be Null";
		}
		
	}
	
	public static void main(String[] args) {
		createData();
	}
	
	public static void createData(){
		Logging.setLoggingDefaults();
		String fileName = "src/main/resources/sumo-sim-out.csv";
		// String fileName = "src/main/resources/test.csv"; //Should return 10 for vehicle id 0

		SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("Batch Processor");
		JavaSparkContext sc = new JavaSparkContext(conf);

		SQLContext sqlContext = new SQLContext(sc);

		// Insert code here
		DataFrame df = sqlContext.read()
			    .format("com.databricks.spark.csv")
			    .option("inferSchema", "true")
			    .option("header", "true")
			    .load("src/main/resources/taa.csv");
		
		
		//Analyze number of accidents per brand
		JavaRDD<Row> javaRDD = df.javaRDD();
		JavaPairRDD<String, Integer> brandMap = javaRDD.mapToPair(row -> new Tuple2<>(row.getString(0), 1));
		reduceByBrand = brandMap.reduceByKey((a, b) -> a + b);
		reduceByBrand = reduceByBrand.sortByKey();
		
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
		
        
        JsonByBrand = gson.toJson(reduceByBrand.collect());
		//return gson.toJson(reduceByBrand.collect());
        
        sc.close();
	}

}
