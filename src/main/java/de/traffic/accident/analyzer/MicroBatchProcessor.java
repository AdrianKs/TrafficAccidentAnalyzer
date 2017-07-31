package de.traffic.accident.analyzer;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.google.common.collect.Lists;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

public class MicroBatchProcessor {

	public static void main(String[] args) {
		SparkConf sparkContext = new SparkConf().setAppName("TrafficAccidentAnalyzer").setMaster("local[2]");
		JavaStreamingContext streamingCont = new JavaStreamingContext(sparkContext, Durations.seconds(10));
		
		HashMap<String, String> kafkaParams = new HashMap<>();
		//BOOTSTRAP SCHREIBEN
		kafkaParams.put("bootstrap.servers", "uoit.farberg.de:9092");
		//ZOOKEEPER AUSLESEN
		kafkaParams.put("zookeeper.connect", "uoit.farberg.de:2181");
		//kafkaParams.put("key.deserializer", StringDeserializer.class);
		//kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "taa_mbprocessor_1");
		
		Set<String> topics = new HashSet<>();
		topics.add("accidents");
		JavaPairInputDStream<String, String> lines = 
			     KafkaUtils.createDirectStream(streamingCont,
			    		 String.class, String.class,StringDecoder.class, StringDecoder.class,
			        kafkaParams,topics);
		
		JavaDStream<String> words = lines.flatMap(x -> Lists.newArrayList(x._2.split(" ")));
		
		words.print();
		
		
	
//		JavaPairDStream<String, Integer> wordCounts = words.mapToPair(word -> new Tuple2<String, Integer>(word, 1))
//				.reduceByKey((i1, i2) -> i1 + i2);
//		
//		wordCounts.print();
		
		streamingCont.start();

		streamingCont.awaitTermination();
		streamingCont.close();
	
		
	}

}
