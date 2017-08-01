package de.traffic.accident.analyzer;

import java.util.ArrayList;

import com.google.gson.JsonElement;

public final class Database {

	private static ArrayList<String[]> WindowData = new ArrayList<String[]>();
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


	public static ArrayList<String[]> getWindowData() {
		return WindowData;
	}


	public static void setWindowData(ArrayList<String[]> windowData) {
		WindowData = windowData;
	}
	
	public static void addArrayToWindowData(String[] array){
		WindowData.add(array);
		printElements();
	}
	
	private static void printElements(){
		for (int i = 0; i < WindowData.size(); i++) {
			System.out.println("ELEMENT-CONTENTS-LAT: " + WindowData.get(i)[0] + ", LONG: " + WindowData.get(i)[1]);
		}
	}
	
	public static void setHeatMapData(){
		//SparkWebserver.setNewAccidentData(WindowData);
	}
	
	public static String getString() {
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
	
	
	
	
}
