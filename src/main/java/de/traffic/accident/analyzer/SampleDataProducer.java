package de.traffic.accident.analyzer;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SampleDataProducer implements Runnable {

	private final Logger log = LoggerFactory.getLogger(SampleDataProducer.class);
	private final KafkaProducer<String, String> producer;
	private final String topic;
	static String csvFile = "src/main/resources/taa.csv";

	public SampleDataProducer() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "uoit.farberg.de:9092");
		props.put("group.id", "taa_mbprocessor");
		props.put("client.id", this.getClass().getSimpleName());
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());

		this.topic = "accidents";
		producer = new KafkaProducer<String, String>(props);
		log.info("Producer is configured.");
	}

	@Override
	public void run() {
		try {
			Reader in = new FileReader(csvFile);
			CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader());
			ArrayList<CSVRecord> list = (ArrayList<CSVRecord>) parser.getRecords();
			AtomicInteger messageNo = new AtomicInteger(0);

			int counter = 0;
			int deltaTime = 0;
			for (CSVRecord record : list) {

				String messageRecord = createMessage(record);

				if (counter != 0) {
					int tmpTimestamp = Integer.parseInt(record.get("Timestamp"));
					CSVRecord tmpRecord = list.get(counter - 1);
					deltaTime = tmpTimestamp - Integer.parseInt(tmpRecord.get("Timestamp"));
				}

				producer.send(new ProducerRecord<String, String>(topic, "" + messageNo, messageRecord),
						(RecordMetadata metadata, Exception exception) -> {

							// Display some data about the message transmission
							if (metadata != null) {
								log.info("Sent message(" + messageNo + ", " + messageRecord + ") sent to partition("
										+ metadata.partition() + "), " + "offset(" + metadata.offset() + ")");
							} else {
								exception.printStackTrace();
							}

						});

				System.out.println(deltaTime);
				Thread.sleep(deltaTime * 1000);
				counter++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private String createMessage(CSVRecord record) {
		String myResult = "";
		
		for (int i = 0; i < record.size(); i++) {
			if(i != record.size() -1){
				myResult += record.get(i) + ",";
			}else{
				myResult += record.get(i);
			}
		}
		
		return myResult;
	}

	/*public static void main(String[] args) {
		BasicConfigurator.configure();
		Set<Service> services = Sets.newHashSet(new SampleDataProducer());
		ServiceManager serviceManager = new ServiceManager(services);
		serviceManager.startAsync().awaitHealthy();

		serviceManager.awaitStopped();
	}*/

}
