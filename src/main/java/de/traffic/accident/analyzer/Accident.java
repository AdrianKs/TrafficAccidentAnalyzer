package de.traffic.accident.analyzer;

public class Accident {
	
	private int timestamp = 0;
	private double latitude = 0;
	private double longitude = 0;
	private String brand = null;
	private String model = null;
	private int constrYear = 0;
	private String color = null;
	private int passenger = 0;
	private Boolean abLeftFront = null;
	private Boolean abRightFront = null;
	private Boolean abLeftMid = null;
	private Boolean abRightMid = null;
	private Boolean tireLeftFront = null;
	private Boolean tireRightFront = null;
	private Boolean tireLeftRear = null;
	private Boolean tireRightRear = null;
	private Boolean windowFront = null;
	private Boolean windowLeftFront = null;
	private Boolean windowRightFront = null;
	private Boolean windowLeftRear = null;
	private Boolean windowRightRear = null;
	private Boolean windowRear = null;
	private int v10SecAgo = 0;
	private int v5SecAgo = 0;
	private int v3SecAgo = 0;
	private int v2SecAgo = 0;
	private int v1SecAgo = 0;
	
	private int accidentType = 0;
	
	
	public Accident(int timestamp, double latitude, double longitude, String brand, String model, int constrYear,
			String color, int passenger, Boolean abLeftFront, Boolean abRightFront, Boolean abLeftMid,
			Boolean abRightMid, Boolean tireLeftFront, Boolean tireRightFront, Boolean tireLeftRear,
			Boolean tireRightRear, Boolean windowFront, Boolean windowLeftFront, Boolean windowRightFront,
			Boolean windowLeftRear, Boolean windowRightRear, Boolean windowRear, int v10SecAgo, int v5SecAgo,
			int v3SecAgo, int v2SecAgo, int v1SecAgo) {
		super();
		this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
		this.brand = brand;
		this.model = model;
		this.constrYear = constrYear;
		this.color = color;
		this.passenger = passenger;
		this.abLeftFront = abLeftFront;
		this.abRightFront = abRightFront;
		this.abLeftMid = abLeftMid;
		this.abRightMid = abRightMid;
		this.tireLeftFront = tireLeftFront;
		this.tireRightFront = tireRightFront;
		this.tireLeftRear = tireLeftRear;
		this.tireRightRear = tireRightRear;
		this.windowFront = windowFront;
		this.windowLeftFront = windowLeftFront;
		this.windowRightFront = windowRightFront;
		this.windowLeftRear = windowLeftRear;
		this.windowRightRear = windowRightRear;
		this.windowRear = windowRear;
		this.v10SecAgo = v10SecAgo;
		this.v5SecAgo = v5SecAgo;
		this.v3SecAgo = v3SecAgo;
		this.v2SecAgo = v2SecAgo;
		this.v1SecAgo = v1SecAgo;
	}
	
	public void printValues(){
		System.out.println("ACCIDENT VALUES");
		System.out.println("------------------------------");
		System.out.println("Timestamp: " + this.timestamp);
		System.out.println("Latitude: " + this.latitude);
		System.out.println("Longitude: " + this.longitude);
		System.out.println("Brand: " + this.brand);
		System.out.println("Model: " + this.model);
		System.out.println("Year of Construction: " + this.constrYear);
		System.out.println("Color: " + this.color);
		System.out.println("Number of passengers: " + this.passenger);
		System.out.println("Airbag Status left front: " + this.abLeftFront);
		System.out.println("Airbag Status right front: " + this.abRightFront);
		System.out.println("Airbag Status left mid: " + this.abLeftMid);
		System.out.println("Airbag Status right mid: " + this.abRightMid);
		System.out.println("Tire Status left front: " + this.tireLeftFront);
		System.out.println("Tire Status right front: " + this.tireRightFront);
		System.out.println("Tire Status left rear: " + this.tireLeftRear);
		System.out.println("Tire Status right rear: " + this.tireRightRear);
		System.out.println("Window Status front: " + this.windowFront);
		System.out.println("Window Status Left front: " + this.windowLeftFront);
		System.out.println("Window Status Right front: " + this.windowRightFront);
		System.out.println("Window Status Left rear: " + this.windowLeftRear);
		System.out.println("Window Status Right rear: " + this.windowRightRear);
		System.out.println("Window Status rear: " + this.windowRear);
		System.out.println("Velocity 10 Seconds before accident: " + this.v10SecAgo);
		System.out.println("Velocity 5 Seconds before accident: " + this.v5SecAgo);
		System.out.println("Velocity 3 Seconds before accident: " + this.v3SecAgo);
		System.out.println("Velocity 2 Seconds before accident: " + this.v2SecAgo);
		System.out.println("Velocity 1 Second before accident: " + this.v1SecAgo);
		System.out.println("------------------------------");
	}


	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getConstrYear() {
		return constrYear;
	}
	public void setConstrYear(int constrYear) {
		this.constrYear = constrYear;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getPassenger() {
		return passenger;
	}
	public void setPassenger(int passenger) {
		this.passenger = passenger;
	}
	public Boolean getAbLeftFront() {
		return abLeftFront;
	}
	public void setAbLeftFront(Boolean abLeftFront) {
		this.abLeftFront = abLeftFront;
	}
	public Boolean getAbRightFront() {
		return abRightFront;
	}
	public void setAbRightFront(Boolean abRightFront) {
		this.abRightFront = abRightFront;
	}
	public Boolean getAbLeftMid() {
		return abLeftMid;
	}
	public void setAbLeftMid(Boolean abLeftMid) {
		this.abLeftMid = abLeftMid;
	}
	public Boolean getAbRightMid() {
		return abRightMid;
	}
	public void setAbRightMid(Boolean abRightMid) {
		this.abRightMid = abRightMid;
	}
	public Boolean getTireLeftFront() {
		return tireLeftFront;
	}
	public void setTireLeftFront(Boolean tireLeftFront) {
		this.tireLeftFront = tireLeftFront;
	}
	public Boolean getTireRightFront() {
		return tireRightFront;
	}
	public void setTireRightFront(Boolean tireRightFront) {
		this.tireRightFront = tireRightFront;
	}
	public Boolean getTireLeftRear() {
		return tireLeftRear;
	}
	public void setTireLeftRear(Boolean tireLeftRear) {
		this.tireLeftRear = tireLeftRear;
	}
	public Boolean getTireRightRear() {
		return tireRightRear;
	}
	public void setTireRightRear(Boolean tireRightRear) {
		this.tireRightRear = tireRightRear;
	}
	public Boolean getWindowFront() {
		return windowFront;
	}
	public void setWindowFront(Boolean windowFront) {
		this.windowFront = windowFront;
	}
	public Boolean getWindowLeftFront() {
		return windowLeftFront;
	}
	public void setWindowLeftFront(Boolean windowLeftFront) {
		this.windowLeftFront = windowLeftFront;
	}
	public Boolean getWindowRightFront() {
		return windowRightFront;
	}
	public void setWindowRightFront(Boolean windowRightFront) {
		this.windowRightFront = windowRightFront;
	}
	public Boolean getWindowLeftRear() {
		return windowLeftRear;
	}
	public void setWindowLeftRear(Boolean windowLeftRear) {
		this.windowLeftRear = windowLeftRear;
	}
	public Boolean getWindowRightRear() {
		return windowRightRear;
	}
	public void setWindowRightRear(Boolean windowRightRear) {
		this.windowRightRear = windowRightRear;
	}
	public Boolean getWindowRear() {
		return windowRear;
	}
	public void setWindowRear(Boolean windowRear) {
		this.windowRear = windowRear;
	}
	public int getV10SecAgo() {
		return v10SecAgo;
	}
	public void setV10SecAgo(int v10SecAgo) {
		this.v10SecAgo = v10SecAgo;
	}
	public int getV5SecAgo() {
		return v5SecAgo;
	}
	public void setV5SecAgo(int v5SecAgo) {
		this.v5SecAgo = v5SecAgo;
	}
	public int getV3SecAgo() {
		return v3SecAgo;
	}
	public void setV3SecAgo(int v3SecAgo) {
		this.v3SecAgo = v3SecAgo;
	}
	public int getV2SecAgo() {
		return v2SecAgo;
	}
	public void setV2SecAgo(int v2SecAgo) {
		this.v2SecAgo = v2SecAgo;
	}
	public int getV1SecAgo() {
		return v1SecAgo;
	}
	public void setV1SecAgo(int v1SecAgo) {
		this.v1SecAgo = v1SecAgo;
	}
	public int getAccidentType(){
		return accidentType;
	}
	public void setAccidentType(int accidentType){
		this.accidentType = accidentType;
	}
	
	

}
