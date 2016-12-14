import java.util.Scanner;

public class testClass {
	public static void main(String [] args) {
		String name;
		String address;
		Scanner scnr = new Scanner(System.in);
		System.out.print("Enter the name: ");
		name = scnr.nextLine();
		System.out.print("Enter the address: ");
		address = scnr.nextLine();
		
		System.out.println(name + " " + address);
	}

}
