package de.traffic.accident.analyzer;

import java.util.ArrayList;

import com.google.gson.JsonElement;

public final class Database {

	private static ArrayList<String[]> WindowData = new ArrayList<String[]>();
	private static JsonElement NumbAccidentsToBrand = null;
	private static JsonElement NumbAccidentsToYearOfCar = null;
	private static JsonElement NumbAccidentsToNumbPasseger = null;
	private static JsonElement NumbOfDiffAccidentType = null;
	private static JsonElement NumbAccidentsToVelocity = null;
	
	private static final int WINDOW_SIZE = 1000;
	private static final int SLIDE_SIZE = 100;
	private static final int FACTOR = 10;
	
	
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

	public static ArrayList<String[]> getWindowData() {
		return WindowData;
	}


	public static void setWindowData(ArrayList<String[]> windowData) {
		WindowData = windowData;
	}
	
	public static void addArrayToWindowData(String[] array){
		WindowData.add(array);
	}
	
	public static String getHeatMapDataAsString() {
		String s = "[";
		for (int i = 0; i < WindowData.size(); i++) {
			s += "[" + WindowData.get(i)[0] + ", " + WindowData.get(i)[1] + "]";
			if(i<WindowData.size()-1) {
				s+=",";
			}
		}
		s += "]";
		return s;
	}
	
	public static JsonElement getNumbAccidentsToVelocity() {
		return NumbAccidentsToVelocity;
	}


	public static void setNumbAccidentsToVelocity(JsonElement numbAccidentsToVelocity) {
		NumbAccidentsToVelocity = numbAccidentsToVelocity;
	}


	public static int getWindowSize() {
		return WINDOW_SIZE;
	}


	public static int getSlideSize() {
		return SLIDE_SIZE;
	}


	public static int getFactor() {
		return FACTOR;
	}
	
}
