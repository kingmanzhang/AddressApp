
/**
 * This file contains classes and methods for using Google's Geocoding API.
 * The main method contains examples showing use of the findCoordinates and
 * distance methods.
 * 
 * Inspired by Stuart Reges' GeoLocator project (University of Washington).
 * 
 * @author Jim Williams
 */

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import com.google.gson.Gson;

/**
 * Provides methods useful to obtain latitude and longitude for a street address
 * utilizing Google's Geocoding API and Google's Gson library. The Geocoding API
 * returns a JSON response which is parsed using Gson resulting in the
 * GeoCoordinates object returned to the user.
 * 
 * @See https://developers.google.com/maps/documentation/geocoding/intro#Geocoding
 * @See https://github.com/google/gson
 * @See https://github.com/google/gson#google-gson
 * 
 * The GeoCoordinates object has a method to check whether it is valid and
 * if so the object has methods to obtain the latitude, longitude and
 * formatted address.
 * 
 */
public class Geocoding {

	/**
	 * This method demonstrates use of the findCoordinates, distance and
	 * GeoCoordinates methods.
	 * 
	 * @param args  unused
	 */
	public static void main(String[] args) {
		String streetAddress = "1600 Amphitheatre Parkway, Mountain View, CA";
		GeoCoordinates coordinates = Geocoding.findCoordinates(streetAddress);

		if (coordinates != null && coordinates.isValid()) {
			System.out.println("Street Address:" + streetAddress);
			System.out.println("Formatted Address: " + coordinates.getFormattedAddress());
			System.out.println("Latitude: " + coordinates.getLatitude());
			System.out.println("Longitude: " + coordinates.getLongitude());
try {
			System.out.println("URL is: " + lookupAddress(streetAddress));
} catch (IOException e) {
	System.out.println("failed to print address.");
}
		} else {
			System.out.println("Error: findCoordinates failed for address: " + streetAddress);
		}

		System.out.println("\nHow far apart are Good and Evil?");
		GeoCoordinates goodCoords = Geocoding.findCoordinates("Good");

		GeoCoordinates evilCoords = Geocoding.findCoordinates("Evil");

		if (goodCoords != null && goodCoords.isValid() 
				&& evilCoords != null && evilCoords.isValid()) {
			
			//print out formatted addresses
			System.out.println("Good:" + goodCoords.getFormattedAddress());
			System.out.println("Evil:" + evilCoords.getFormattedAddress());
			
			//calculate distance between latitude and longitude of 
			//Good and Evil locations.
			double distanceMiles = Geocoding.distance(goodCoords.getLatitude(), goodCoords.getLongitude(),
					evilCoords.getLatitude(), evilCoords.getLongitude());
			
			System.out.printf("Distance between Good and Evil is: %.2f miles.\n", distanceMiles);
		} else {
			System.out.println("Error: Unable to find Good and Evil.");
		}
	}

	/**
	 * This class method when given a valid street address will return a
	 * GeoCoordinates object containing the latitude and longitude.
	 * 
	 * @param streetAddress
	 * @return A GeoCoordinates object or null on some errors.
	 */
	public static GeoCoordinates findCoordinates(String streetAddress) {
		GeoCoordinates coordinates = null;
		try {
			String jsonResponse = lookupAddress(streetAddress);
			coordinates = new Gson().fromJson(jsonResponse, GeoCoordinates.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return coordinates;
	}

	/**
	 * Utilizes Google's Geocoding API to get location information given an
	 * address. The response is in a JSON format that can be parsed with the
	 * GeoCoordinates class.
	 * 
	 * @param address
	 *            A street address.
	 * @return a JSON formatted response with various location information such
	 *         as latitude and longitude.
	 * 
	 * @throws IOException
	 * @exclude
	 */
	private static String lookupAddress(String address) throws IOException {
		// encode address in a form to pass as part of URL as get request
		String encodedAddress = URLEncoder.encode(address, CHARSET);

		// prepare get request string.
		String httpGetRequest = GEOCODING_URL + encodedAddress;

		// perform get request
		URLConnection connection = new URL(httpGetRequest).openConnection();
		connection.setRequestProperty("Accept-Charset", CHARSET);

		// gather and return JSON response as a String
		InputStream inputStream = connection.getInputStream();
		InputStreamReader inputReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputReader);

		StringBuffer response = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			response.append(line);
			response.append("\n");
		}

		return response.toString();
	}

	// URL for the Google Maps Geocoding API
	private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";

	// charset used for encoding the address
	private static final String CHARSET = "UTF-8";

	/**
	 * Calculate the distance between two points on the Earth's surface using
	 * the Spherical Law of Cosines.
	 * 
	 * @param latitude1
	 *            Latitude of point 1
	 * @param longitude1
	 *            Longitude of point 1
	 * @param latitude2
	 *            Latitude of point 2
	 * @param longitude2
	 *            Longitude of point 2
	 * @return the distance in miles between the two points.
	 */
	public static double distance(double latitude1, double longitude1, double latitude2, double longitude2) {

		// Latitude and longitude are points that describe a location on the
		// Earth's surface but are also angles measured in degrees.
		// http://www-istp.gsfc.nasa.gov/stargaze/Slatlong.htm
		// Convert latitude and longitude for the two points into radians
		double lat1Radians = Math.toRadians(latitude1);
		double long1Radians = Math.toRadians(longitude1);
		double lat2Radians = Math.toRadians(latitude2);
		double long2Radians = Math.toRadians(longitude2);

		// Utilize the Spherical Law of Cosines. The triangle is the 2 points
		// plus the North pole.
		// https://en.wikipedia.org/wiki/Spherical_law_of_cosines
		// http://helpdesk.objects.com.au/java/distance-between-two-latitude-longitude-points
		double arc = Math.acos(Math.sin(lat1Radians) * Math.sin(lat2Radians)
				+ Math.cos(lat1Radians) * Math.cos(lat2Radians) * Math.cos(long1Radians - long2Radians));
		return arc * EARTHS_RADIUS_IN_MILES;
	}

	// Earth's radius in miles
	// https://en.wikipedia.org/wiki/Earth_radius
	private static final double EARTHS_RADIUS_IN_MILES = 3959.0;
	
	/**
	 * Starts a browser page with the specified url, if on a supported Desktop.
	 * @param url  A url such as https://www.google.com/maps/
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void openBrowser(String url) throws IOException, URISyntaxException {
		if( Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(new URI( url));
		}		
	}
}

/**
 * These classes and their fields match the structure of the Geocoding JSON
 * formatted response such that Gson can convert from JSON to instances of these
 * classes.
 *
 */
class GeoCoordinates {
	private String status;
	private List<GResult> results;
	private String error_message;

	GeoCoordinates() {
		status = null;
		results = null;
		error_message = null;
	}

	public String getFormattedAddress() {
		return this.results.get(0).formatted_address;
	}

	public double getLongitude() {
		return Double.parseDouble(this.results.get(0).geometry.location.lng);
	}

	public double getLatitude() {
		return Double.parseDouble(this.results.get(0).geometry.location.lat);
	}

	public boolean isValid() {
		return status != null && this.status.equals("OK") && this.results != null && this.results.size() > 0;
	}

	public String toString() {
		if ( this.isValid()) {
			return this.status + "\n" + this.getFormattedAddress() + "\n" + this.getLatitude() + "\n"
					+ this.getLongitude() + "\n";
		} else {
			return this.status + ": " + this.error_message;
		}
	}
}

class GResult {
	List<GAddressComponent> address_components;
	String formatted_address;
	GGeometry geometry;
	Boolean partial_match;
	String place_id;
	List<String> types;
}

class GGeometry {
	GViewport bounds;
	GCoordinates location;
	String location_type;
	GViewport viewport;
}

class GViewport {
	GCoordinates northeast;
	GCoordinates southwest;
}

class GAddressComponent {
	String long_name;
	String short_name;
	List<String> types;
}

class GCoordinates {
	String lat;
	String lng;
}
