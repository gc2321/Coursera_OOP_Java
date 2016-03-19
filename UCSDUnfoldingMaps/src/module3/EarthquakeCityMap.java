package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(1250, 700, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 1050, 650, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it

		int i=0;
		while (i < earthquakes.size()){

			PointFeature f = earthquakes.get(i);
			markers.add(createMarker(f));
			System.out.println(f.getProperties());

			i++;
		}
		// add list of markers to map
		map.addMarkers(markers);

	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		int yellow = color(255, 255, 0);
		int blue = color(255, 0, 255);
		int red = color(255, 0, 0);

		SimplePointMarker spm = new SimplePointMarker(feature.getLocation());
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		spm.setRadius(mag);
		if (mag<4){
			spm.setColor(blue);
		}else if(mag<4.9){
			spm.setColor(yellow);
		}else{
			spm.setColor(red);
		}
		return spm;

	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	// add legend to map
	private void addKey() 
	{
		int yellow = color(255, 255, 0);
		int blue = color(255, 0, 255);
		int red = color(255, 0, 0);

		fill(color(255,255,255));
		textSize(20);
		text("Earthquake Key", 25, 50);

		fill(blue);
		ellipse(25, 70, 10, 10);
		fill(color(255,255,255));
		textSize(15);
		text("Below 4.0", 35, 75);

		fill(yellow);
		ellipse(25, 90, 10, 10);
		fill(color(255,255,255));
		textSize(15);
		text("4.0+", 35, 95);

		fill(red);
		ellipse(25, 110, 10, 10);
		fill(color(255,255,255));
		textSize(15);
		text("5.0+", 35, 115);

	}
}
