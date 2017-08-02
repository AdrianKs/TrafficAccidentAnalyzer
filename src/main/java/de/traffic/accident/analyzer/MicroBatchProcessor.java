package de.traffic.accident.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.google.common.collect.Lists;

import kafka.serializer.StringDecoder;

public class MicroBatchProcessor implements Runnable {


	private JavaStreamingContext streamingCont = null;
	private HashMap<String, String> kafkaParams = null;
	private ArrayList<String[]> emptyList = new ArrayList<String[]>();

	public MicroBatchProcessor(JavaStreamingContext jsContext) {

		this.streamingCont = jsContext;
		this.kafkaParams = new HashMap<>();

		this.kafkaParams.put("bootstrap.servers", "uoit.farberg.de:9092");
		this.kafkaParams.put("zookeeper.connect", "uoit.farberg.de:2181");
		this.kafkaParams.put("group.id", "taa_mbprocessor_1");
	}

	@Override
	public void run() {
		Set<String> topics = new HashSet<>();
		topics.add("accidents");
		JavaPairInputDStream<String, String> lines = KafkaUtils.createDirectStream(this.streamingCont, String.class,
				String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);
		JavaDStream<Accident> accidents = lines.window(Durations.seconds(1000/10), Durations.seconds(100/10)).map(x -> {
			 ArrayList<String> elements = Lists.newArrayList(x._2.split(","));
			 return new Accident(
					 Integer.parseInt(elements.get(0)), 
					 Double.parseDouble(elements.get(1)), 
					 Double.parseDouble(elements.get(2)), 
					 elements.get(3), 
					 elements.get(4), 
					 Integer.parseInt(elements.get(5)), 
					 elements.get(6), 
					 Integer.parseInt(elements.get(7)), 
					 Boolean.parseBoolean(elements.get(8)), 
					 Boolean.parseBoolean(elements.get(9)), 
					 Boolean.parseBoolean(elements.get(10)), 
					 Boolean.parseBoolean(elements.get(11)), 
					 Boolean.parseBoolean(elements.get(12)), 
					 Boolean.parseBoolean(elements.get(13)), 
					 Boolean.parseBoolean(elements.get(14)), 
					 Boolean.parseBoolean(elements.get(15)), 
					 Boolean.parseBoolean(elements.get(16)), 
					 Boolean.parseBoolean(elements.get(17)), 
					 Boolean.parseBoolean(elements.get(18)), 
					 Boolean.parseBoolean(elements.get(19)), 
					 Boolean.parseBoolean(elements.get(20)), 
					 Boolean.parseBoolean(elements.get(21)), 
					 Integer.parseInt(elements.get(22)), 
					 Integer.parseInt(elements.get(23)), 
					 Integer.parseInt(elements.get(24)),
			 		 Integer.parseInt(elements.get(25)),
				 	 Integer.parseInt(elements.get(26)));
		});
		
		
		
		
		accidents.foreachRDD(rdd -> {
			emptyList.removeAll(emptyList);
			Database.setWindowData(emptyList);
			rdd.foreach(accident -> {
				String[] tmp = {Double.toString(accident.getLatitude()), Double.toString(accident.getLongitude())};
				Database.addArrayToWindowData(tmp);
				accident.printValues();
			});
		});
		
		this.streamingCont.start();

		this.streamingCont.awaitTermination();
		this.streamingCont.close();

	}
	

}
