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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * The MyPlacesApp managers a list of places. It asks user to add, delete, 
 * show places, or read and write the place list. 
 * @author Xingmin Zhang
 *
 */

public class MyPlacesApp {
	public static void main(String[] args) {
		//create a new scanner for user input
		Scanner scnr = new Scanner(System.in);
		//create a PlaceList for the app
		PlaceList places = new PlaceList();
		//keep track whether user want to quit the app
		boolean quit = false;
		//store what action the user chooses to do
		String actionChoice; 
		//keep track of items in the menu
		String[] menu; 
		String FILE_EXTENSION = "mpl";
		String DATA_PATH = "./";
		//repeatedly ask users to choose actions until user chooses to quit.
		do {
			System.out.println("\nMy Places 2016");
			places.printPlaces();  //list all places in My Places 2016
			menu = menuList(places.size()); //list a menu for users to choose
			//let user choose an action
			actionChoice = scnr.nextLine().toLowerCase(); 
			//if user chooses a wrong action, let user press enter to choose again
			if (!checkInput(menu, actionChoice)) {
				enterKeyPressed(scnr); 
			//otherwise, do what the user chooses
			} else {
					switch (actionChoice) {
						case "q": {  //if user chooses to quit, quit the program
							//if the placelist is changed, ask user to save changes
							if(places.getIsChanged()) { 
							saveBeforeExit(places, scnr, FILE_EXTENSION, DATA_PATH);
							}
							quit = true;
							break;
						}
						case "a": { //if user types in "a" or "A", add a new place 
							addPlace(places, scnr);
							break;
						}
						case "s": { //if user types in "s" or "S", show a place in 
							         //the list
							showPlace(places, scnr);
							break;
						}
						case "d": { //if user types in "d" or "D", delete a place 
							         //in the list
							deletePlace(places, scnr);
							break;
						}
						case "r": { //if user types in "r" or "R", read places from 
							         //a file
							fileList(FILE_EXTENSION, DATA_PATH);//list all files with a "mp" 
							                      //extension in the default directory
							readInPlace(places, scnr); //add places from a file
							break;
						}
						case "w": { //if user types in "w" or "W", write out places
							         //to a file
							fileList(FILE_EXTENSION, DATA_PATH);//list all files with a "mp" 
							                      //extension in the default directory
							writePlaces(places, scnr);//write out places to a file
							break;
						}
						case "c": { //if user types in "c" or "C", let user set the 
										//current place
							setCurrent(places, scnr);
							break;
						}
					}
				}
		}while(!quit);
		
		//print out a closing sentence before exiting the app
		System.out.println("Thank you for using My Places 2016!");
	}
	
	/**
	 * A method to print out the menu of actions that a user can choose from.
	 * @param numPlaces: The method takes in an integer to determine what 
	 * actions should be in the menu. 
	 * @return the content of the menu after displaying the menu to the user.
	 */
	public static String[] menuList(int numPlaces) {
		String[] str;
		//if there is no place in my place list, display the following menu
		if(numPlaces == 0) { 
			System.out.print("A)dd R)ead Q)uit: ");
			str = new String[]{"a", "r", "q"};
	   //otherwise, display another menu
		} else {
			System.out.print("A)dd S)how D)elete C)urrent R)ead W)rite Q)uit: ");
			str = new String[]{"a", "s", "d", "c", "r","w", "q"};
		}
		return str;
	}
	

	/**
	 * A method to add places to my place list
	 * @param places: a place list to which new places are added to
	 * @param scnr: a scanner instance
	 * The method asks user to input the name and address of a place, 
	 * and then add the new place to the place list.
	 */
	public static void addPlace(PlaceList places, Scanner scnr) {
		String name;   //a variable for the name of new places
		String address;//a variable for the address of new places
		System.out.print("Enter the name: ");
		name = scnr.nextLine(); //let user type in the name
		System.out.print("Enter the address: ");
		address = scnr.nextLine();//let user type in the address
		if(Geocoding.findCoordinates(address).isValid()) {
			Place placeToAdd = new Place(name, address); //create a new place
			places.add(placeToAdd); //add the new place to the place list
			System.out.println("Adding: " + name);
		}
		enterKeyPressed(scnr); //ask user to press enter to end the action
	}
	
	
	/**
	 * A method to show a place in the place list. The users types in the 
	 * number of the place to show. 
	 * @param Places: a list of places 
	 * @param scnr: a scanner instance
	 */
	private static final String CHARSET = "UTF-8";
	
	public static void showPlace(PlaceList places, Scanner scnr) {
		System.out.print("Enter number of place to Show: ");
		int numPlace = scnr.nextInt(); //let user type in a number 
		try { //if users types a valid number, display the name and 
			   //address of the place
			System.out.println(places.get(numPlace - 1).getName());
			System.out.println(places.get(numPlace - 1).getAddress());
			System.out.print(places.get(numPlace - 1).getLatitude() + ",");
			System.out.println(places.get(numPlace - 1).getLongitude());
			System.out.println(places.get(numPlace - 1).getURL());
			Geocoding.openBrowser(places.get(numPlace - 1).getURL());
		} catch (IndexOutOfBoundsException e) {
			//otherwise, report an error message
			System.out.printf("Expected a number between %d and %d.\n", 1, places.size());
		} catch (IOException e) {
			System.out.println("Error in URL");
		} catch (URISyntaxException e) {
			System.out.println("Invalid URL address");
		}
//		catch (IOException e) {
//			System.out.print("print failed"); //just a test
//		} 
		finally {
				scnr.nextLine();
				enterKeyPressed(scnr);//let users to press enter to complete the action
			}
	}
	
	
	/**
	 * A method to delete a place in the place list. The user types in which 
	 * place to delete by type in the places number in the list. 
	 * @param places: a place list from which to delete a place
	 * @param scnr: a scanner instance for user input
	 */
	public static void deletePlace(PlaceList places, Scanner scnr) {
		System.out.print("Enter number of place to Delete: ");
		int numPlace = scnr.nextInt();//let user type a number. The place at this 
		                          //position will be deleted
		try {
			System.out.println("Deleting: " + places.get(numPlace - 1).getName());
			places.remove(numPlace - 1);
		} catch (IndexOutOfBoundsException e) {
			System.out.printf("Expected a number between %d and %d.\n", 1, places.size());
		} finally {
			scnr.nextLine();
			enterKeyPressed(scnr);//let user press enter to complete the action
		}
	}
	
	/**
	 * A method to write places to a file
	 * @param places: a place list to write to a file
	 * @param scnr: a scanner to deal with user input
	 */
	public static void writePlaces(PlaceList places, Scanner scnr) {
		System.out.print("Enter filename: ");
		String filename = scnr.next(); //let user type in a file name
		System.out.println("Writing file: " + filename);
	   try {
	   	//try to create a FileOutputStream to the file
			FileOutputStream fileStringStream = new FileOutputStream(filename);
			PrintWriter output = new PrintWriter(fileStringStream);
			//print the names and address of each place to the file. Separate them with ";"
			for (int i = 0; i < places.size(); i++) {
				output.print(places.get(i).getName() + "; ");
				output.println(places.get(i).getAddress());
			}
			output.close();
			//if user types in a invalid file, deal the exception
		} catch (FileNotFoundException e) {
			System.out.println("Unable to write to file: " + filename);
		}
	   scnr.nextLine();
		enterKeyPressed(scnr);//let user press enter to complete the action
	}
	
	/**
	 * A method to save changes before exit the program
	 * @param places: list of places
	 * @param scnr: a scanner for user input
	 */
	public static void saveBeforeExit(PlaceList places, Scanner scnr, 
			String FILE_EXTENSION, String DATA_PATH) {
		System.out.println("Exit without saving changes? Y/N");
		String userChoice = scnr.nextLine();
		if(userChoice.toLowerCase().charAt(0) == 'n') {
			fileList(FILE_EXTENSION, DATA_PATH);
			System.out.println("Save to a current place list or a new list");
			writePlaces(places, scnr);
		}
	}
	
	public static void setCurrent(PlaceList places, Scanner scnr) {
		System.out.print("Enter number of place to be Current place: ");
		try {
			int numCurrent = scnr.nextInt();
			Place.setCurrent(places.get(numCurrent - 1));
			System.out.println(places.get(numCurrent - 1).getName() + " set as Current place.");
		} catch (IndexOutOfBoundsException e) {
			System.out.printf("Expected a number between %d and %d.\n", 1, places.size());
		} finally {
			scnr.nextLine();
			enterKeyPressed(scnr);//let user press enter to complete the action
		}
	}

	/**
	 * A method to ask user to press the enter key.
	 * @param scnr: a scanner instance for user input
	 */
	public static void enterKeyPressed(Scanner scnr) {
		System.out.print("Press Enter to continue.");
	   scnr.nextLine(); //let user type in anything (or nothing) and press enter
	}
	
	/**
	 * A method to check whether a string is an element of a string array
	 * @param strList: an array of strings
	 * @param str: a string
	 * @return true is the string is an element of a string array
	 */
	public static boolean checkInput(String[] strList, String str) {
		boolean passed = false; //a variable to track whether the test is passed
		//if the string (str) is within the array, the test is passed.
		for(int i = 0; i < strList.length; i++) {
			if(str.equals(strList[i])) {
				passed = true;
				break;
			}
		}
		//if the test is failed, give user a feedback. 
		if(!passed) {
			System.out.println("Unrecognized choice: " + str);
		}
		return passed;
	}
	
	
	/**
	 * A method to print out all files with a specified extension in a folder.
	 * @param FILE_EXTENSION: the file extension to check
	 * @param DATA_PATH: folder to check
	 */

	public static void fileList(String FILE_EXTENSION, String DATA_PATH) {
		File folder = new File(DATA_PATH);//open a folder in the specified path
		System.out.println("My Places Files: ");
		for (File file: folder.listFiles()) { //print out all files
			if (file.getName().endsWith(FILE_EXTENSION)) {
				System.out.println("    " + file.getName());
			}
		}
		System.out.println("");
	}
	
	/**
	 * A method to read places from a file
	 * @param places: a list of places
	 * @param scnr: a scanner to deal with user input
	 */
	public static void readInPlace(PlaceList places, Scanner scnr) {
		System.out.print("Enter filename: ");
		String filename = scnr.next(); //let user type in file name
		scnr.nextLine();
		System.out.println("Reading file: " + filename);
		try{	// try to open a fileinputstream with the filename.
			//if the filename is legitimate, parse the text and add to places
			FileInputStream filePlaceStream = new FileInputStream(filename);
			parsePlacesToAdd(filePlaceStream, places);//parse text
			filePlaceStream.close();
		} catch (IOException excpt) { //if the user types in a file name that 
			                           //does not correspond to any file, 
			System.out.println("Unable to read from file: " + filename);
		} finally {
			enterKeyPressed(scnr);//let user press enter to complete the action
		}	
	}
	/**
	 * A method to parse text and add to places
	 * @param placeText: a FileInputStream instance that contains text 
	 * for places
	 * @param places: a place list to which new places are added
	 */
	public static void parsePlacesToAdd(FileInputStream placeText, PlaceList places) {
		//Create a scanner to parse the text
		Scanner placeParser = new Scanner(placeText); 
		String newLine; //a string variable for a new line of strings
		String[] strs;  //a string array to store parsed strings in a line
		String name;    //a string variable for the name of a new place
		String address = " "; //a string variable for the address of a new place
		Place newPlace; //a variable to a new place from each line
		while(placeParser.hasNextLine()) {
			newLine = placeParser.nextLine();
			strs = newLine.split(";"); //split each line at the ";"
			name = strs[0];            //the first part is the name of a place
			//An address should start with a number. So get a substring from the first
			//integer to the end
			for (int i = 0; i < strs[1].length(); i++) {
				if(Character.isDigit(strs[1].charAt(i))) {
					address = strs[1].substring(i);
					break;
				}
			}
			//If the address is valid, create a new place and add to the place list
			if (Geocoding.findCoordinates(address).isValid()) {
				newPlace = new Place(name, address);
				//add the new place to the list if it is not already there
				if(places.contains(newPlace)) { 
					System.out.println(newPlace.getName() + "already in list.");
				} else {
					places.add(newPlace);
				}
			//Report an error message to user if the address is not recognized
			} else {
				System.out.println(name + "'s address is not recognized");	
			}
		}
		placeParser.close();
	}	
}
