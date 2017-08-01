package de.traffic.accident.analyzer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import de.uniluebeck.itm.util.logging.Logging;

public class App {

	SparkWebserver sparkServer = null;

	BatchProcesser batchProcessor = null;

	MicroBatchProcessor microBatchProcessor = null;

	SampleDataProducer sampleDataProducer = null;

	final static SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("Batch Processor");

	final static JavaSparkContext sc = new JavaSparkContext(conf);


	final static JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(10));

	public static void main(String[] args) {
		App app = new App();
	}

	public App() {
		Logging.setLoggingDefaults();
		sparkServer = new SparkWebserver();

		batchProcessor = new BatchProcesser(sc);
		microBatchProcessor = new MicroBatchProcessor(ssc);
		sampleDataProducer = new SampleDataProducer();

		Thread threadBatchProcessor = new Thread(batchProcessor);
		Thread threadMicroBatchProcessor = new Thread(microBatchProcessor);
		Thread threadSampleDataProducer = new Thread(sampleDataProducer);

		threadBatchProcessor.start();
		threadMicroBatchProcessor.start();
		threadSampleDataProducer.start();

	}

	public static int tiresOrAirbagsDamaged(boolean leftFront, boolean rightFront, boolean leftRear,
			boolean rightRear) {
		int counter = 0;
		if (leftFront) {
			counter++;
		}
		if (rightFront) {
			counter++;
		}
		if (leftRear) {
			counter++;
		}
		if (rightRear) {
			counter++;
		}

		return counter;
	}

	public static int windowsDamaged(boolean windowFront, boolean windowLeftFront, boolean windowRightFront,
			boolean windowLeftRear, boolean windowRightRear, boolean windowRear) {
		int counter = 0;
		if (windowFront) {
			counter++;
		}
		if (windowLeftFront) {
			counter++;
		}
		if (windowRightFront) {
			counter++;
		}
		if (windowLeftRear) {
			counter++;
		}
		if (windowRightRear) {
			counter++;
		}
		if (windowRear) {
			counter++;
		}
		return counter;
	}

	public static AccidentType analyzeAccident(Accident accident) {
		int tiresDamaged = tiresOrAirbagsDamaged(accident.getTireLeftFront(), accident.getTireRightFront(), accident.getTireLeftRear(),
				accident.getTireRightRear());
		int windowsDamaged = windowsDamaged(accident.getWindowFront(), accident.getWindowLeftFront(), accident.getWindowRightFront(),
				accident.getWindowLeftRear(), accident.getWindowRightRear(), accident.getWindowRear());
		int airbagsTriggered = tiresOrAirbagsDamaged(accident.getAbLeftFront(), accident.getAbRightFront(), accident.getAbLeftMid(),
				accident.getAbRightMid());

		if (tiresDamaged == 0 && airbagsTriggered == 0) {
			if (windowsDamaged == 1) {
				return AccidentType.STONE_CHIP;
			}
			if (windowsDamaged > 1) {
				return AccidentType.FLYING_OBJECT;
			}
			else {
				return AccidentType.NOT_ANALYZABLE;
			}
		}
		else if(tiresDamaged >= 1 && airbagsTriggered == 0 && windowsDamaged == 0){
			return AccidentType.RUPTURED_TIRE;
		}
		//schwerer Auffahrunfall
		else if(airbagsTriggered >= 2 && (windowsDamaged+tiresDamaged) >=2){
			return AccidentType.GRAVE_COLLISION;
		}
		//mittlerschwerer Auffahrunfall
		else if(airbagsTriggered > 2){
			return AccidentType.MODERATE_COLLISION;
		}
		//sonstiger Auffahrunfall
		else{
			return AccidentType.LIGHT_COLLISION;
		}
	}
}
