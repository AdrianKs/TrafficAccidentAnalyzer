package de.traffic.accident.analyzer;

import de.uniluebeck.itm.util.logging.Logging;

public class App 
{
	
	
	SparkWebserver sparkServer = null;
	
	
    public static void main( String[] args )
    {
    	App app = new App();
    }
    
    public App(){
    	Logging.setLoggingDefaults();
    	sparkServer = new SparkWebserver();
    	
    }
}
