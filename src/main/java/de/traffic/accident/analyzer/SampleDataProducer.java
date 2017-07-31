package de.traffic.accident.analyzer;

import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class SampleDataProducer {
	
	static String csvFile = "src/main/resources/taa.csv";

	public static void main(String[] args) {
		
		try {
			Reader in = new FileReader(csvFile);
			Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
			for (CSVRecord record : records) {
				System.out.println(record);
				System.out.println(record.get("Marke"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
	}

}
