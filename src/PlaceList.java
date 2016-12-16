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


import java.util.ArrayList;
import java.util.Collections;

/**
 * The PlaceList is an ArrayList of Places. The class manages a list of places. 
 * @author Xingmin Zhang
 *
 */

public class PlaceList {
	private ArrayList<Place> places; //The class has an ArrayList of places
	private boolean isChanged; //whether new places are added to or removed 
	//from the list
	
	/**
	 * The constructor initializes the place to a new ArrayList of places
	 */
	public PlaceList() {
		this.places = new ArrayList<>();
		this.isChanged = false;
	}
	
	/**
	 * A method to add new places to the list
	 * @param place: a new place to be added.
	 */
	public void add(Place place) {
		places.add(place);
		isChanged = true;
	}
	
	/**
	 * A method to remove places at the specified index. 
	 * @param index: the index of the place to be removed from the list. 
	 */
	public void remove(int index) {
		places.remove(index);
		isChanged = true;
	}
	
	/**
	 * A method to determine the size of the place list. 
	 * @return how many places in the list.
	 */
	public int size() {
		return places.size();
	}
	
	/**
	 * A method to determine whether the place list is empty.
	 * @return true is the list has 0 places. 
	 */
	public boolean hasPlaces() {
		if (places.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * A method to get a place at specified index.
	 * @param index: the index of place to be returned
	 * @return the place at the specified index. 
	 */
	public Place get(int index) {
		return places.get(index);
	}
	
	/**
	 * A method to get whether the placelist has changed
	 * @return whether the placelist is changed
	 */
	public boolean getIsChanged() {
		return isChanged;
	}
	/**
	 * A method to determine whether a place is contained in the list. 
	 * @param place: a place that needs to be checked. 
	 * @return true is the name of the place matches any place already in 
	 * the list. 
	 */
	public boolean contains(Place place) {
		boolean outcome = false;
		for (int i = 0; i < places.size(); i++) {
			if(places.get(i) != null & 
					places.get(i).getName().equals(place.getName())) {
				outcome = true;
				break;
			} 
		}
		return outcome;
	}
	
	/**
	 * A method to print out all the places in the list. 
	 * If the place list is empty, it prints out "No places in memory". 
	 * Otherwise, print the names of places. 
	 */
	public void printPlaces() {
		System.out.println("--------------------------");
		//If there is no places in the list, print out "No places in memory". 
		if(this.size() == 0) {
			System.out.println("No places in memory.");
		//Otherwise, print out the names of places alphabetically if Current 
			//is not set. 
		} else if (Place.getCurrent() == null) {
			this.sort();
			for(int i = 0; i < this.size(); i++) {
				System.out.println((i + 1) + ") " + places.get(i).getName());
			}
		//if Current is already set, then print out names and distances to 
		//Current from closest to the furtherest
		} else{
			this.sort();
			for(int i = 0; i < this.size(); i++) {
				System.out.printf("%d) %s (%.2f miles)\n", i + 1, 
						places.get(i).getName(), places.get(i).getDistance());
			}
		}
		System.out.println("--------------------------");
	}
	
	/**
	 * A method to sort places in the place list. It relies on the compareTo 
	 * method in Place class.
	 */
	public void sort() {
		Collections.sort(places);
	}

}
