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

public class BatchProcesser implements Runnable {
	
	public static JavaPairRDD<String, Integer> reduceByBrand;
	public static JavaPairRDD<Integer, Integer> reduceByYear;
	public static String JsonByBrand;
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
		//Analyze number of accidents per brand
		JavaRDD<Row> javaRDD = df.javaRDD();
		JavaPairRDD<String, Integer> brandMap = javaRDD.mapToPair(row -> new Tuple2<>(row.getString(0), 1));
		reduceByBrand = brandMap.reduceByKey((a, b) -> a + b);
		reduceByBrand = reduceByBrand.sortByKey();
		
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
		
        
        JsonByBrand = gson.toJson(reduceByBrand.collect());
        System.out.println(reduceByBrand.collect());
		//return gson.toJson(reduceByBrand.collect());
        
        //sc.close();
	}
	
	public static String getJsonByBrand(){
		if(JsonByBrand != null) {
			return JsonByBrand;
		}
		else {
			return "Seems to be Null";
		}
		
	}
	
	
}
