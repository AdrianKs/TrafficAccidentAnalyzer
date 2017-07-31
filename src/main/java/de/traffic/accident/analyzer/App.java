package de.traffic.accident.analyzer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import de.uniluebeck.itm.util.logging.Logging;

public class App 
{
	
	
	SparkWebserver sparkServer = null;
	
	BatchProcesser batchProcessor = null;
	
	MicroBatchProcessor microBatchProcessor = null;
	
	SampleDataProducer sampleDataProducer = null;
	
	final static SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("Batch Processor");
	
	final static JavaSparkContext sc = new JavaSparkContext(conf);

    final static JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(10));
	
	
	
    public static void main( String[] args )
    {
    	App app = new App();
    }
    
    public App(){
    	Logging.setLoggingDefaults();
    	//sparkServer = new SparkWebserver();
		
    	
    	
    	
    	batchProcessor = new BatchProcesser(sc);
    	microBatchProcessor = new MicroBatchProcessor(ssc);
    	sampleDataProducer = new SampleDataProducer();
    	
    	
    	
    	Thread threadBatchProcessor = new Thread(batchProcessor);
    	Thread threadMicroBatchProcessor = new Thread(microBatchProcessor);
    	Thread threadSampleDataProducer = new Thread(sampleDataProducer);
    	
    	
    	threadBatchProcessor.start();
    	//threadMicroBatchProcessor.start();
    	//threadSampleDataProducer.start();
    	
    	
    }
}
