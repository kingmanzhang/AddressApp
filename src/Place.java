import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

///////////////////////////////////////////////////////////////////////////////
//
// Title:            Program 4: Places
// Files:            Place.java, PlaceList.java, MyPlacesApp.java 
// Semester:         Fall 2016
//
// Author:           Xingmin Zhang	
// Email:            xzhang66@wisc.edu
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:     none
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates 
// strangers, etc do.
//
// Persons:          none
// Online Sources:   none
//
///////////////////////////////////////////////////////////////////////////////



/**
 * The Place class is responsible for creating places. A place has two fields, 
 * name and address. 
 * @author Xingmin Zhang
 *
 */

public class Place implements Comparable<Place> {
	private String name;      //name of a place
	private String address;   //address of a place
	double latitude;          //latitude of a place
	double longitude;        //longitude of a place
	String addressJSON;       //JSON address of a place
	GeoCoordinates geoCoordinate; //coordinate of a place
	private static Place current; //current place
	
	public Place(String name, String address) {
		this.name  = name;
		this.address = address;
		this.geoCoordinate = Geocoding.findCoordinates(address);
		this.latitude = geoCoordinate.getLatitude();
		this.longitude = geoCoordinate.getLongitude();
		this.addressJSON = geoCoordinate.getFormattedAddress();
		current = null;
	}
	
	/**
	 * A method to return the name of a place.
	 * @return the name of a place
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * A method to return the address of a place.
	 * @return the address of a place
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * A method to get the latitude of a place
	 * @return the latitude of a place
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * A method to get the longitude of a place
	 * @return the longitude of a place
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * A method to set a place as current place
	 * @param place: to be set as current place
	 */
	public static void setCurrent(Place place) {
		current = place;
	}
	
	/**
	 * A method to get the current address
	 * @return current address
	 */
	public static Place getCurrent() {
		return current;
	}
	
	/**
	 * A method to compare two places. The method overrides the equals() method 
	 * of the Object class.  
	 * @return true if two places have the same name. 
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		//The method should take in a place object
		if(obj instanceof Place) {
			Place newPlace = (Place) obj;
			//if two places have the same name (case insensitive), they are 
			//regarded as the same place.
			if (newPlace.name.toLowerCase().equals(newPlace.name.toLowerCase())) {
				isEqual = true;
			}
		}
		return isEqual;
	}
	
	//The constant part of a URL for an address
	private final String URL_PREFIX = "https://www.google.com/maps/place/";
	private static final String CHARSET = "UTF-8";
	
	/**
	 * A method to generate the URL of a place
	 * @return the URL of a place
	 */
	public String getURL() {
		try {
			String addressURL = URL_PREFIX + URLEncoder.encode(addressJSON, CHARSET) + 
					"/@" + latitude + "," + longitude + ",17z/";
			return addressURL;
		} catch (UnsupportedEncodingException e) {
			System.out.println("URL error");
			return null;
		}
	}
	
	/**
	 * A method to get the distance of a place to the current place
	 * @return the distance of a place to the current place
	 */
	
	public double getDistance() {
		if (current != null) {
			return Geocoding.distance(this.getLatitude(), this.getLongitude(), 
					current.getLatitude(), current.getLongitude());
		} else {
			return -1;
		}
	}

	/**
	 * A method to compare two places. If the current place is not set, compare
	 * based on the name by their alphabetic order; if the current place is 
	 * set, compare based on their distance to the current place, closest to
	 * furtherest. 
	 */
	 @Override
	public int compareTo(Place otherPlace) {
		if (current == null) {
			return this.getName().compareTo(otherPlace.getName()); 
		} else {
			return Double.compare(this.getDistance(), otherPlace.getDistance());
		}
	}
}
