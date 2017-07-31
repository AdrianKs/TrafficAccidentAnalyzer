package de.traffic.accident.analyzer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import de.uniluebeck.itm.util.logging.Logging;
import scala.Tuple2;

public class BatchProcesser {
	
	public static JavaPairRDD<String, Integer> reduceByBrand;
	public static JavaPairRDD<Integer, Integer> reduceByYear;

	public static void main(String[] args) {
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
		
		
		//Analyze number of accidents per age of car
		JavaPairRDD<Integer, Integer> yearMap = javaRDD.mapToPair(row -> new Tuple2<>(row.getInt(5), 1));
		reduceByYear = yearMap.reduceByKey((a, b) -> a + b);
		reduceByYear = reduceByYear.sortByKey();
		
		
		System.out.println("brandMap: " + brandMap.collect());
		System.out.println("reduceByBrand: " + reduceByBrand.collect());
		
		for(Tuple2 t: reduceByBrand.collect()) {
			System.out.println(t._1 + ": " + t._2);
		}
		
		for(Tuple2 t: reduceByYear.collect()) {
			System.out.println(t._1 + ": " + t._2);
		}

		sc.close();

	}

}
