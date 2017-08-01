package de.traffic.accident.analyzer;

import com.google.gson.JsonElement;

public final class Database {

	private static JsonElement NumbAccidentsToBrand = null;
	private static JsonElement NumbAccidentsToYearOfCar = null;
	private static JsonElement NumbAccidentsToNumbPasseger = null;
	private static JsonElement NumbOfDiffAccidentType = null;
	
	
	private Database() {
		// TODO Auto-generated constructor stub
	}


	public static JsonElement getNumbAccidentsToBrand() {
		return NumbAccidentsToBrand;
	}


	public static void setNumbAccidentsToBrand(JsonElement numbAccidentsToBrand) {
		NumbAccidentsToBrand = numbAccidentsToBrand;
	}


	public static JsonElement getNumbAccidentsToYearOfCar() {
		return NumbAccidentsToYearOfCar;
	}


	public static void setNumbAccidentsToYearOfCar(JsonElement numbAccidentsToYearOfCar) {
		NumbAccidentsToYearOfCar = numbAccidentsToYearOfCar;
	}


	public static JsonElement getNumbAccidentsToNumbPasseger() {
		return NumbAccidentsToNumbPasseger;
	}


	public static void setNumbAccidentsToNumbPasseger(JsonElement numbAccidentsToNumbPasseger) {
		NumbAccidentsToNumbPasseger = numbAccidentsToNumbPasseger;
	}


	public static JsonElement getNumbOfDiffAccidentType() {
		return NumbOfDiffAccidentType;
	}


	public static void setNumbOfDiffAccidentType(JsonElement numbOfDiffAccidentType) {
		NumbOfDiffAccidentType = numbOfDiffAccidentType;
	}
	
	
}
